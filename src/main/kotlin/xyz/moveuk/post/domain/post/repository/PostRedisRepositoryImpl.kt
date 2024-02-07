package xyz.moveuk.post.domain.post.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class PostRedisRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>
) : PostRedisRepository {
    override fun saveRecentPostIds(memberId: Long, todoId: Long) {
        val key = "$RECENT_POST_KEY$memberId"

        redisTemplate.opsForList().let {
            it.remove(key, 0, todoId.toString())

            it.leftPush(key, todoId.toString())

            if (it.size(key)!! > RECENT_POST_COUNT) {
                it.rightPop(key)
            }
        }
    }

    override fun getRecentPostIds(memberId: Long): List<Long>? {
        val key = "$RECENT_POST_KEY$memberId"

        return redisTemplate.opsForList().range(key, 0, RECENT_POST_COUNT)?.map { it.toLong() }
    }

    companion object {
        const val RECENT_POST_COUNT = 5L
        const val RECENT_POST_KEY = "recentPost_"
    }
}