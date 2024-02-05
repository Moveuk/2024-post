package xyz.moveuk.post.api.post.dto

import xyz.moveuk.post.domain.post.model.Post

data class CreatePostResponse(
    val postId: Long,
    val title: String,
    val content: String,
    val postPicUrls: MutableList<String>,
) {
    companion object {
        fun of(post: Post): CreatePostResponse = CreatePostResponse(
            postId = post.id!!,
            title = post.title,
            content = post.content,
            postPicUrls = post.images.map { it.url }.toMutableList()
        )
    }
}