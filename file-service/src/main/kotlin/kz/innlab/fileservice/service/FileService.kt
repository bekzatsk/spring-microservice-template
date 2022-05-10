package kz.innlab.fileservice.service

import kz.innlab.fileservice.repository.FileRepository
import kz.innlab.fileservice.model.File as FileModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.experimental.and

/**
 * @project edm-spring
 * @author Bekzat Sailaubayev on 12.02.2022
 */
@Service
class FileService {
    @Value("\${spring.servlet.multipart.location}")
    private val uploadPath: String = ""

    @Autowired
    lateinit var fileRepository: FileRepository

    fun getFile(id: UUID): Optional<FileModel> {
        return fileRepository.findByIdAndDeletedAtIsNull(id)
    }

    fun getFullPath(fileModel: FileModel): String {
        return getFilesPath(uploadPath, fileModel.createdAt) + "/" + fileModel.id
    }

    fun saveFile(multipartFile: MultipartFile): UUID? {
        val file: File = multipartFileToFile(multipartFile, "$uploadPath/tmp/")
        val hashFile = sha256Checksum(file)
        val fileCandidate = fileRepository.findByHashCodeAndDeletedAtIsNull(hashFile)
        if (fileCandidate.isPresent) {
            return fileCandidate.get().id!!
        }

        val newFile = FileModel()
        newFile.fileName = multipartFile.originalFilename!!.substringBeforeLast(".")
        newFile.fileFormat = multipartFile.originalFilename!!.substringAfterLast(".")
        newFile.mime = multipartFile.contentType
        newFile.hashCode = hashFile
        newFile.size = multipartFile.size

        fileRepository.save(newFile)

        if (newFile.id != null) {
            try {
                createFile(file, newFile.id!!, newFile.createdAt)
            } catch (e: Exception) {
                return null
            }
        }

        return  newFile.id
    }

    @Throws(IOException::class)
    fun multipartFileToFile(
        multipart: MultipartFile,
        dir: String
    ): File {
        val filepath: Path = Paths.get(dir, "tmp_${UUID.randomUUID()}")
        Files.createDirectories(Paths.get(dir))
        multipart.transferTo(filepath)
        return File(filepath.toString())
    }

    fun sha256Checksum(file: File): String = getHashOfFiles("SHA-256", file)

    private fun getHashOfFiles(typeHash: String, input: File): String {
        val digest = MessageDigest.getInstance(typeHash)
        val fileInputStream = FileInputStream(input)

        val byteArray = ByteArray(1024)
        var bytesCount = 0

        while (fileInputStream.read(byteArray).also { bytesCount = it } != -1) {
            digest.update(byteArray, 0, bytesCount)
        }
        fileInputStream.close()
        val bytes = digest.digest()

        val sb = StringBuilder()
        for (i in bytes.indices) {
            sb.append(((bytes[i].and(0xff.toByte())) + 0x100).toString(16).substring(1))
        }
        return sb.toString()
    }

    fun createFile(tmpFile: File, id: UUID, createdDate: Timestamp = Timestamp(System.currentTimeMillis())): String {
        val newPath = getFilesPath(uploadPath, createdDate) + id
        tmpFile.let { sourceFile ->
            if (!fileExists(getFilesPath(uploadPath, createdDate) + id)) {
                sourceFile.copyTo(File(newPath))
            }
            sourceFile.delete()
        }
        if (!fileExists(newPath))
            throw FileNotFoundException("File doesn't created")
        return newPath
    }

    companion object {
        private fun getFilesPath(pathFiles: String, createdDate: Timestamp): String {
            val directory = "$pathFiles/files/${SimpleDateFormat("yyyy/MM/dd").format(createdDate)}"
            Files.createDirectories(Paths.get(directory))
            return "$directory/"
        }

        fun fileExists(path: String): Boolean {
            return File(path).exists()
        }

        fun contentTypeFromMime(contentMime: String?, default: MediaType = MediaType.IMAGE_JPEG): MediaType {
            var contentType: MediaType = default
            if (
                contentMime != null
                && contentMime.isNotEmpty()
                && contentMime.split("/").size == 2
            ) {
                val types = contentMime.split("/")
                contentType = MediaType(types[0], types[1])
            }
            return contentType
        }
    }
}
