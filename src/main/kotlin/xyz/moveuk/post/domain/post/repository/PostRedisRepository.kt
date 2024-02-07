package xyz.moveuk.post.domain.post.repository

interface PostRedisRepository {
    fun saveRecentPostIds(memberId: Long, todoId: Long)
    fun getRecentPostIds(memberId: Long): List<Any>?
    fun incrementHit(authenticatedMemberId: Long, postId: Long): Long
    fun getHitsByInPostId(postIds: List<Long>): List<Long>
}