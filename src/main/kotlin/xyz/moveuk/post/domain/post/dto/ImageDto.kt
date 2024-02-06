package xyz.moveuk.post.domain.post.dto

import com.querydsl.core.annotations.QueryProjection

data class ImageDto @QueryProjection constructor(
    val imageUrl: String,
) {
    companion object {
        fun of(url: String): ImageDto = ImageDto(url)
    }
}
