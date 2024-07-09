package com.azilzor.chatterchain

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ChatterChainApplication

fun main(args: Array<String>) {
    SpringApplication.run(ChatterChainApplication::class.java, *args)
}
