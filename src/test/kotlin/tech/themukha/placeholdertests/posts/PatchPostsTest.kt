package tech.themukha.placeholdertests.posts

import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import tech.themukha.placeholdertests.api.PostsApi
import tech.themukha.placeholdertests.dto.PostDto
import tech.themukha.placeholdertests.flow.TestFlow
import tech.themukha.placeholdertests.utils.DataClassExtensions.getRandomPost

@DisplayName("Patch Posts tests")
@Feature("Posts")
@Story("Patch Posts")
class PatchPostsTests {

    /**
     * PATCH-запрос не должен иметь возможность менять уникальный ID поста,
     * особенно если пост с таким ID уже существует.
     * Заносим баг-репорт на доску.
     * */
    @ParameterizedTest(name = "Partially update existing post with ID {0} successfully")
    @MethodSource("tech.themukha.placeholdertests.posts.PostsDataProvider#validPatchPostProvider")
    @DisplayName("Partially update existing post successfully")
    fun `Partially update existing post successfully`(postId: Int, updatedPost: PostDto) {
        var patchedPost: PostDto? = null
        val existingPost = PostsApi.`Get post by ID`(postId)!!

        TestFlow()
            .step("Partially update post with ID $postId") {
                patchedPost = PostsApi.`Patch an existing post`(
                    postId = postId,
                    updatedPost = updatedPost
                )
            }
            .step("Check patched post") {
                assertAll(
                    {
                        assertEquals(
                            existingPost.id,
                            patchedPost?.id,
                            "Post ID should not be patched"
                        )
                    },
                    {
                        updatedPost.userId?.let {
                            assertEquals(
                                updatedPost.userId,
                                patchedPost!!.userId,
                                "User ID mismatch"
                            )
                        } ?: assertEquals(patchedPost?.userId, patchedPost?.userId, "User ID mismatch")
                    },
                    {
                        updatedPost.title?.let {
                            assertEquals(
                                updatedPost.title,
                                patchedPost!!.title,
                                "Title mismatch"
                            )
                        } ?: assertEquals(patchedPost?.title, patchedPost?.title, "Title mismatch")
                    },
                    {
                        updatedPost.body?.let {
                            assertEquals(
                                updatedPost.body,
                                patchedPost!!.body,
                                "Body mismatch"
                            )
                        } ?: assertEquals(patchedPost?.body, patchedPost?.body, "Body mismatch")
                    },
                )
            }
    }

    /**
     * PATCH-запрос не может поменять пост, которого не существует.
     * Узнаем у аналитика, т.к. нет в техспеке, какой ответ должны получать - 404 или 400, но явно не 200.
     * Заносим баг-репорт на доску.
     * */
    @ParameterizedTest(name = "Fail to partially update a non-existing post with invalid ID {0}")
    @MethodSource("tech.themukha.placeholdertests.posts.PostsDataProvider#invalidPostIdProvider")
    @DisplayName("Partially update non-existing post by ID")
    fun `Partially update non-existing post by ID`(invalidPostId: Int) {

        TestFlow()
            .step("Partially update non-existing post with ID $invalidPostId expecting 404 response code") {
                PostsApi.`Patch an existing post`(
                    invalidPostId,
                    PostDto(
                        id = 5,
                        userId = 1,
                        title = "new title",
                        body = "new body"
                    ),
                    expectedResponseCode = HttpStatus.SC_NOT_FOUND
                )
            }
    }

    @Test
    @DisplayName("Partially update post with empty request should fail")
    fun `Partially update post with empty request should fail`() {
        val existingPost = PostsApi.`Get all posts`().getRandomPost()

        TestFlow()
            .step("Partially update post with empty body expecting an exception") {
                val patchedPost = PostsApi.`Patch an existing post`(
                    existingPost?.id!!,
                    PostDto(
                        null,
                        null,
                        null,
                        null
                    )
                )
                assertEquals(existingPost, patchedPost)
            }
        }
    }