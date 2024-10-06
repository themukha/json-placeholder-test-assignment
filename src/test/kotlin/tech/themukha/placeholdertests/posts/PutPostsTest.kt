package tech.themukha.placeholdertests.posts

import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import tech.themukha.placeholdertests.dto.PostDto
import tech.themukha.placeholdertests.flow.TestFlow
import tech.themukha.placeholdertests.utils.DataClassExtensions.getRandomPost

@DisplayName("Put Posts tests")
@Feature("Posts")
@Story("Put Posts")
class PutPostsTests {

    @ParameterizedTest(name = "Update existing post with ID {0} successfully")
    @MethodSource("tech.themukha.placeholdertests.providers.PostsDataProvider#validPutPostProvider")
    @DisplayName("Update existing post successfully")
    fun `Update existing post successfully`(postId: Int, updatedPost: PostDto) {
        var putPost: PostDto? = null

        TestFlow()
            .step("Update existing post with ID $postId") {
                putPost = `Put update an existing post`(
                    postId = postId,
                    updatedPost = updatedPost
                )
            }
            .step("Check updated post") {
                assertNotNull(putPost,"Updated post should not be null")
                assertAll(
                    {
                        assertEquals(
                            postId,
                            putPost?.id,
                            "Post ID should not be updated"
                        )
                    },
                    {
                        assertEquals(
                            updatedPost.userId,
                            putPost!!.userId,
                            "User ID mismatch"
                        )
                    },
                    {
                        assertEquals(
                            updatedPost.title,
                            putPost!!.title,
                            "Title mismatch"
                        )
                    },
                    {
                        assertEquals(
                            updatedPost.body,
                            putPost!!.body,
                            "Body mismatch"
                        )
                    }
                )
            }
    }

    /**
     * Получаем 500 ответы. Не обрабатываем какое-то исключение.
     * Смотрим логи бэкенда, определяем источник.
     * Узнаем у аналитика, т.к. нет в техспеке, какой ответ должны получать - 404 или 400, но явно не 500.
     * Заносим баг-репорт на доску.
     * */
    @ParameterizedTest(name = "Fail to update a non-existing post with ID {0}")
    @MethodSource("tech.themukha.placeholdertests.providers.PostsDataProvider#invalidPostIdProvider")
    @DisplayName("Put non-existing post by ID")
    fun `Put non-existing post by ID`(invalidPostId: Int) {
        TestFlow()
            .step("Update non-existing post with ID $invalidPostId expecting an exception") {
                `Put update an existing post`(
                    invalidPostId,
                    PostDto(
                        id = null,
                        userId = 1,
                        title = "new title",
                        body = "new body"),
                    expectedResponseCode = HttpStatus.SC_NOT_FOUND
                )
            }
    }

    @Test
    @DisplayName("Partially update post with empty request should fail")
    fun `Partially update post with empty request should fail`() {
        var existingPost: PostDto? = null
        val newPost = PostDto(
            null,
            null,
            null,
            null
        )

        TestFlow()
            .step("Getting the existing post by ID") {
                existingPost = `Get all posts`().getRandomPost()
            }
            .step("Partially update post with empty body expecting an exception") {
                val patchedPost = `Put update an existing post`(
                    existingPost?.id!!,
                    newPost
                )

                assertEquals(newPost.copy(id = existingPost?.id), patchedPost)
            }
        }
    }