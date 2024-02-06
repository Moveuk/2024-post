package xyz.moveuk.post.domain.post.repository

import org.springframework.data.jpa.repository.JpaRepository
import xyz.moveuk.post.domain.post.model.Post

interface PostRepository : JpaRepository<Post, Long>, PostQueryDslRepository, PostRedisRepository
