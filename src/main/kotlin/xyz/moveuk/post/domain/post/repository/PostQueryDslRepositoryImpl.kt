package xyz.moveuk.post.domain.post.repository

import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils
import xyz.moveuk.post.api.post.dto.PostSearchCondition
import xyz.moveuk.post.domain.member.model.QMemberEntity
import xyz.moveuk.post.domain.post.dto.PostListDto
import xyz.moveuk.post.domain.post.dto.QPostListDto
import xyz.moveuk.post.domain.post.model.QPost
import xyz.moveuk.post.infra.querydsl.QueryDslSupport


@Repository
class PostQueryDslRepositoryImpl : PostQueryDslRepository, QueryDslSupport() {
    private val post = QPost.post
    private val member = QMemberEntity.memberEntity

    override fun searchByWhere(condition: PostSearchCondition): MutableList<PostListDto?> {
        return queryFactory
            .select(
                QPostListDto(
                    post.id.`as`("postId"),
                    post.title,
                    post.member.id.`as`("memberId"),
                    post.member.nickname,
                    post.createdAt
                )
            )
            .from(post)
            .leftJoin(post.member, member)
            .where(
                postIdLt(condition.lastPostId),
                titleLike(condition.title),
                contentLike(condition.content),
                nicknameLike(condition.nickname),
            ).orderBy(post.id.desc())
            .limit(condition.limit ?: DEFAULT_LIMIT)
            .fetch()
    }

    //제목 (포함)
    private fun titleLike(title: String?): BooleanExpression? {
        return if (StringUtils.hasText(title)) post.title.containsIgnoreCase(title) else null
    }

    //내용 (포함)
    private fun contentLike(content: String?): BooleanExpression? {
        return if (StringUtils.hasText(content)) post.content.containsIgnoreCase(content) else null
    }

    //글쓴이 (포함)
    private fun nicknameLike(nickname: String?): BooleanExpression? {
        return if (StringUtils.hasText(nickname)) post.member.nickname.containsIgnoreCase(nickname) else null
    }

    //페이징 개선을 위한 마지막 게시글 id NoOffset 쿼리 사용
    private fun postIdLt(lastPostId: Long?): BooleanExpression? {
        return if (lastPostId != null) post.id.lt(lastPostId) else null
    }

    companion object {
        const val DEFAULT_LIMIT = 10L
    }
}