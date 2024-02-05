package xyz.moveuk.post.api.post.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.validation.annotation.Validated
import org.springframework.web.multipart.MultipartFile
import xyz.moveuk.post.domain.image.model.Image
import xyz.moveuk.post.domain.member.model.MemberEntity
import xyz.moveuk.post.domain.post.model.Post

@Validated
data class CreatePostRequest(
    @field:Size(min = 1, max = 100, message = "게시글 제목의 글자수는 {min}자 이상 {max} 자 이하여야 합니다.")
    @field: NotBlank(message = "제목을 입력해주세요")
    val title: String,
    @field:Size(min = 1, max = 10000, message = "게시글 내용의 글자수는 {min}자 이상 {max} 자 이하여야 합니다.")
    @field: NotBlank(message = "내용을 입력해주세요")
    val content: String,
    val postPics: MutableList<MultipartFile>,
) {
    lateinit var member: MemberEntity
    val images: MutableList<Image> = mutableListOf()

    fun isPicsEmpty(): Boolean {
        return postPics[0].originalFilename == "" && postPics.size == 1
    }

    fun member(member: MemberEntity): CreatePostRequest {
        this.member = member
        return this
    }

    fun images(images: MutableList<Image>): CreatePostRequest {
        this.images.addAll(images)
        return this
    }

    fun toEntity(): Post = Post(
        title = title,
        content = content,
        member = member,
        images = images
    )
}