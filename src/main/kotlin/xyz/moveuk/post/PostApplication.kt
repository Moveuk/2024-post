package xyz.moveuk.post

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PostApplication

fun main(args: Array<String>) {
	runApplication<PostApplication>(*args)
}
