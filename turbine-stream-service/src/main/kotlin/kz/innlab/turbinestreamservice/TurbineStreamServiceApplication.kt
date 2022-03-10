package kz.innlab.turbinestreamservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream

@SpringBootApplication
@EnableDiscoveryClient
@EnableTurbineStream
class TurbineStreamServiceApplication

fun main(args: Array<String>) {
    runApplication<TurbineStreamServiceApplication>(*args)
}
