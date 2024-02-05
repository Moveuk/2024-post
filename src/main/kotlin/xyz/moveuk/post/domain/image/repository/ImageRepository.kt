package xyz.moveuk.post.domain.image.repository

import org.springframework.data.jpa.repository.JpaRepository
import xyz.moveuk.post.domain.image.model.Image

interface ImageRepository : JpaRepository<Image, Long> {
}