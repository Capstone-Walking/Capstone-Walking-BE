package com.walking.traffic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WalkingTrafficApplication

fun main(args: Array<String>) {
    runApplication<WalkingTrafficApplication>(*args)
}