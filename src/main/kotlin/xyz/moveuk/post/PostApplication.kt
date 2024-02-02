package xyz.moveuk.post

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class PostApplication

fun main(args: Array<String>) {
	runApplication<PostApplication>(*args)
}
