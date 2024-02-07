package xyz.moveuk.post.domain.post.repository

import jakarta.annotation.PostConstruct
import org.springframework.data.redis.core.ListOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.SetOperations
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository
import java.util.*


@Repository
class PostRedisRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>
) : PostRedisRepository {
    lateinit var valueOperations: ValueOperations<String, String>
    lateinit var listOperations: ListOperations<String, String>
    lateinit var setOperations: SetOperations<String, String>

    @PostConstruct
    fun init() {
        valueOperations = redisTemplate.opsForValue()
        listOperations = redisTemplate.opsForList()
        setOperations = redisTemplate.opsForSet()
    }

    override fun saveRecentPostIds(memberId: Long, todoId: Long) {
        val key = "$RECENT_POST_KEY$memberId"

        listOperations.remove(key, 0, todoId.toString())

        listOperations.leftPush(key, todoId.toString())

        if (listOperations.size(key)!! > RECENT_POST_COUNT) {
            listOperations.rightPop(key)
        }
    }

    override fun getRecentPostIds(memberId: Long): List<Long>? {
        val key = "$RECENT_POST_KEY$memberId"

        return listOperations.range(key, 0, RECENT_POST_COUNT)?.map { it.toLong() }
    }

    override fun incrementHit(authenticatedMemberId: Long, postId: Long): Long {
        //TODO: 확장 가능하지 않음. POST의 경우에만 되고 다른 서비스 추가시 조회수 가능을 공유하지 않는가에 대한 고민 필요.
        val postKey = "$POST_HIT_KEY$postId"
        val memberKey = "$POST_HIT_MEMBER_KEY$postId"
        //금일 조회수 올린 사람 체크
        //true - 기존에 조회를 했던 사람이면(set에 값이 있으면) 기존 조회수 반환
        //false - 기존에 조회를 하지 않았던 사람이면 조회 목록에 넣고 조회수 올려서 반환
        return if (setOperations.isMember(memberKey, authenticatedMemberId.toString())!!)
            valueOperations.get(postKey)!!.toLong()
        else {
            setOperations.add(memberKey, authenticatedMemberId.toString())
            return valueOperations.increment(postKey)!!
        }
    }

    /*
    * 받은 postId 리스트의 조회수 반환
    * */
    override fun getHitsByInPostId(postIds: List<Long>): List<Long> {
        return postIds.map { POST_HIT_KEY + it }
            .let { valueOperations.multiGet(it) }!!
            .map {
                if (Objects.isNull(it)) return@map 0L else it.toLong()
            }
    }

    companion object {
        const val RECENT_POST_COUNT = 5L
        const val RECENT_POST_KEY = "recentPost_"
        const val POST_HIT_KEY = "postHit_"
        const val POST_HIT_MEMBER_KEY = "postHitMember_"
    }
}