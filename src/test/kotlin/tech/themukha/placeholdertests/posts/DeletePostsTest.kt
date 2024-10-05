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
import org.junit.jupiter.params.provider.ValueSource
import tech.themukha.placeholdertests.api.PostsApi
import tech.themukha.placeholdertests.dto.PostDto
import tech.themukha.placeholdertests.flow.TestFlow
import tech.themukha.placeholdertests.utils.DataClassExtensions.getRandomPost

@DisplayName("Delete Posts tests")
@Feature("Posts")
@Story("Delete Posts")
class DeletePostsTests {

    @ParameterizedTest(name = "Delete existing post with ID {0} successfully")
    @MethodSource("tech.themukha.placeholdertests.posts.PostsDataProvider#validDeletePostProvider")
    fun `Delete existing post successfully`(postId: Int) {

        TestFlow()
            .step("Delete post with ID $postId") {
                PostsApi.`Delete an existing post`(
                    postId,
                    HttpStatus.SC_OK
                )
        }
    }

    /**
     * Получаем 200 ответ по несуществующим постам.
     * Заводим баг-репорт.
     * */
    @ParameterizedTest(name = "Fail to delete a non-existing post with ID {0}")
    @MethodSource("tech.themukha.placeholdertests.posts.PostsDataProvider#invalidPostIdProvider")
    fun `Delete non-existing post by ID should fail`(invalidPostId: Int) {

        TestFlow()
            .step("Try to delete a non-existing post") {
                PostsApi.`Delete an existing post`(
                    invalidPostId,
                    HttpStatus.SC_NOT_FOUND
                )
            }
    }

}