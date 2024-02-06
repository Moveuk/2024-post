package xyz.moveuk.post.domain.post.repository

import xyz.moveuk.post.api.post.dto.PostSearchCondition
import xyz.moveuk.post.domain.post.dto.PostDto
import xyz.moveuk.post.domain.post.dto.PostListDto

interface PostQueryDslRepository {
    fun findFetchJoinPostById(postId: Long): PostDto?
    fun getRecentPosts(recentPostIds: List<Long>): MutableList<PostListDto?>
    fun searchByWhere(condition: PostSearchCondition): MutableList<PostListDto?>
}