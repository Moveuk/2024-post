package xyz.moveuk.post.application.post.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import xyz.moveuk.post.api.post.dto.CreatePostRequest
import xyz.moveuk.post.api.post.dto.CreatePostResponse
import xyz.moveuk.post.api.post.dto.PostSearchCondition
import xyz.moveuk.post.domain.image.model.Image
import xyz.moveuk.post.domain.image.repository.ImageRepository
import xyz.moveuk.post.domain.member.repository.MemberRepository
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
