package xyz.moveuk.post.api.post.dto

data class PostSearchCondition (
    val title: String?,
    val content: String?,
    val nickname: String?,
    val limit: Long?,
    val lastPostId: Long?,
)
