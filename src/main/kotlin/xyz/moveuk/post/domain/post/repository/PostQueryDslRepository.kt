package xyz.moveuk.post.domain.post.repository

import xyz.moveuk.post.api.post.dto.PostSearchCondition
import xyz.moveuk.post.domain.post.dto.PostListDto

interface PostQueryDslRepository {
    fun searchByWhere(condition: PostSearchCondition): MutableList<PostListDto?>
}