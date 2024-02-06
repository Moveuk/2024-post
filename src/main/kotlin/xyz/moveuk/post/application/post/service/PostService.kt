package xyz.moveuk.post.application.post.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import xyz.moveuk.post.api.post.dto.CreatePostRequest
import xyz.moveuk.post.api.post.dto.CreatePostResponse
import xyz.moveuk.post.api.post.dto.PostSearchCondition
import xyz.moveuk.post.domain.image.model.Image
import xyz.moveuk.post.domain.image.repository.ImageRepository
import xyz.moveuk.post.domain.member.repository.MemberRepository
import xyz.moveuk.post.domain.post.dto.PostDto
import xyz.moveuk.post.domain.post.dto.PostListDto
import xyz.moveuk.post.domain.post.repository.PostRepository
import xyz.moveuk.post.global.exception.RestApiException
import xyz.moveuk.post.global.exception.dto.CommonErrorCode
import xyz.moveuk.post.infra.aws.S3ClientService
import xyz.moveuk.post.infra.security.UserPrincipal

@Service
class PostService(
    private val memberRepository: MemberRepository,
    private val imageRepository: ImageRepository,
    private val postRepository: PostRepository,
    private val s3ClientService: S3ClientService
) {
    fun getAllPosts(condition: PostSearchCondition): MutableList<PostListDto?> {
        return postRepository.searchByWhere(condition)
    }

    fun getPost(authenticatedMemberId: Long, postId: Long): PostDto {
        //최근 본 글에 추가
        postRepository.saveRecentPostIds(authenticatedMemberId, postId)

        return postRepository.findFetchJoinPostById(postId)
            ?: throw RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND)
    }

    fun getRecentPosts(authenticatedMemberId: Long): MutableList<PostListDto?> {
        val recentPostIds = postRepository.getRecentPostIds(authenticatedMemberId)?.map { it as Long } ?: listOf()

        //in 연산자로 조회
        val recentPosts = postRepository.getRecentPosts(recentPostIds)

        //querydsl에서 ORDER BY FIND_IN_SET()을 지원하지 않아 순서가 다르기 떄문에 application 계층에서 정렬해주기로 결정
        val orderedRecentPosts = mutableListOf<PostListDto?>()

        recentPostIds.forEachIndexed {
                index, postId ->
            orderedRecentPosts.add(index, recentPosts.first { it?.postId == postId })
        }

        return orderedRecentPosts
    }

    fun createPost(req: CreatePostRequest, userPrincipal: UserPrincipal): CreatePostResponse {
        val findMember = memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND)

        val savedImages: MutableList<Image> =
            if (!req.isPicsEmpty()) {
                s3ClientService.upload(req.postPics, "post").map {
                    Image.of(it)
                }.let {
                    imageRepository.saveAll(it)
                }
            } else mutableListOf()

        return req
            .member(findMember)
            .images(savedImages)
            .toEntity().let {
                postRepository.save(it)
            }.let {
                CreatePostResponse.of(it)
            }

    }

}
