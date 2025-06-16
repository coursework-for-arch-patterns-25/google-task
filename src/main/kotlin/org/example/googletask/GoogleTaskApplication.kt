package org.example.googletask

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GoogleTaskApplication

fun main(args: Array<String>) {
    runApplication<GoogleTaskApplication>(*args)
}
