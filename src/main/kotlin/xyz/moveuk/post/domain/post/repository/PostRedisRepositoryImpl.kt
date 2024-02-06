package xyz.moveuk.post.domain.post.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class PostRedisRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>
) : PostRedisRepository {
    override fun saveRecentPostIds(memberId: Long, todoId: Long) {
        val key = "recentPost_$memberId"

        redisTemplate.opsForList().let {
            it.leftPush(key, todoId.toString())

            if (it.size(key)!! > RECENT_POST_COUNT) {
                it.rightPop(key)
            }
        }
    }

    override fun getRecentPostIds(memberId: Long): List<Long>? {
        val key = "recentPost_$memberId"

        return redisTemplate.opsForList().range(key, 0, RECENT_POST_COUNT + 1)?.map { it.toLong() }
    }

    companion object {
        const val RECENT_POST_COUNT = 5L
    }
}