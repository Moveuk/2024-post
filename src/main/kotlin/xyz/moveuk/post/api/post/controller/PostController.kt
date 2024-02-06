package xyz.moveuk.post.api.post.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import xyz.moveuk.post.api.post.dto.CreatePostRequest
import xyz.moveuk.post.api.post.dto.CreatePostResponse
import xyz.moveuk.post.api.post.dto.PostSearchCondition
import xyz.moveuk.post.application.post.service.PostService
import xyz.moveuk.post.domain.post.dto.PostDto
import xyz.moveuk.post.domain.post.dto.PostListDto
import xyz.moveuk.post.infra.security.UserPrincipal

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
    private val postService: PostService
) {
    @GetMapping
    fun getPosts(
        condition: PostSearchCondition
    ): ResponseEntity<MutableList<PostListDto?>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getAllPosts(condition))
    }

    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @GetMapping("/{postId}")
    fun getPost(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable(value = "postId") postId: Long
    ): ResponseEntity<PostDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getPost(userPrincipal.id, postId))
    }

    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @GetMapping("/recent-posts")
    fun getRecentPosts(
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<MutableList<PostListDto?>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getRecentPosts(userPrincipal.id))
    }

    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @PostMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createPost(
        @Valid @ModelAttribute createPostRequest: CreatePostRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CreatePostResponse> {
        return postService.createPost(createPostRequest, userPrincipal)
            .let {
                ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(it)
            }
    }

}