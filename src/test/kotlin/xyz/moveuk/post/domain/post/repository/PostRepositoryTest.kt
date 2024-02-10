package xyz.moveuk.post.domain.post.repository

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import xyz.moveuk.post.api.post.dto.PostSearchCondition
import xyz.moveuk.post.domain.member.model.MemberEntity
import xyz.moveuk.post.domain.member.model.MemberRole
import xyz.moveuk.post.domain.member.repository.MemberRepository
import xyz.moveuk.post.domain.post.model.Post
import xyz.moveuk.post.infra.redis.RedisConfig
import xyz.moveuk.post.infra.security.PasswordEncoderConfig

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(
    value = [
        RedisConfig::class,
        PasswordEncoderConfig::class]
)
@ActiveProfiles("test")
class PostRepositoryTest @Autowired constructor(
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Test
    fun `PostSearchCondition의 쿼리 조건이 없을 경우 전체 데이터 조회되는지 확인`() {
        // GIVEN
        memberRepository.saveAllAndFlush(DEFAULT_MEMBER_LIST.map {
            it.encodePassword(passwordEncoder)
        })
        postRepository.saveAllAndFlush(DEFAULT_POST_LIST)
        val condition1 = PostSearchCondition(
            title = null,
            content = null,
            nickname = null,
            limit = null,
            lastPostId = null
        )
        val condition2 = PostSearchCondition(
            title = null,
            content = null,
            nickname = null,
            limit = 3,
            lastPostId = null
        )
        val condition3 = PostSearchCondition(
            title = null,
            content = null,
            nickname = null,
            limit = 15,
            lastPostId = null
        )

        // WHEN
        val result1 = postRepository.searchByWhere(condition1)
        val result2 = postRepository.searchByWhere(condition2)
        val result3 = postRepository.searchByWhere(condition3)

        // THEN
        result1.size shouldBe 10
        result2.size shouldBe 3
        result3.size shouldBe 10
    }

    companion object {
        private val DEFAULT_MEMBER_LIST = listOf(
            MemberEntity(
                email = "sample1@naver.com",
                rawPassword = "aaaa",
                nickname = "사용자1",
                role = MemberRole.MEMBER
            ),
            MemberEntity(
                email = "sample2@gmail.com",
                rawPassword = "aaaa",
                nickname = "member2",
                role = MemberRole.MEMBER
            ),
        )
        private val DEFAULT_POST_LIST = listOf(
            Post(title = "제목1", content = "content1", member = DEFAULT_MEMBER_LIST[0], images = mutableListOf()),
            Post(title = "제목2", content = "content2", member = DEFAULT_MEMBER_LIST[0], images = mutableListOf()),
            Post(title = "제목3", content = "content3", member = DEFAULT_MEMBER_LIST[0], images = mutableListOf()),
            Post(title = "title4", content = "내용4", member = DEFAULT_MEMBER_LIST[0], images = mutableListOf()),
            Post(title = "title5", content = "내용5", member = DEFAULT_MEMBER_LIST[0], images = mutableListOf()),
            Post(title = "title6", content = "내용6", member = DEFAULT_MEMBER_LIST[1], images = mutableListOf()),
            Post(title = "title7", content = "content7", member = DEFAULT_MEMBER_LIST[1], images = mutableListOf()),
            Post(title = "title8", content = "content8", member = DEFAULT_MEMBER_LIST[1], images = mutableListOf()),
            Post(title = "title9", content = "content9", member = DEFAULT_MEMBER_LIST[1], images = mutableListOf()),
            Post(title = "title10", content = "content10", member = DEFAULT_MEMBER_LIST[1], images = mutableListOf()),
        )
    }
}