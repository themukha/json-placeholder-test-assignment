package tech.themukha.placeholdertests.posts

import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.apache.http.HttpStatus
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import tech.themukha.placeholdertests.flow.TestFlow

@DisplayName("Delete Posts tests")
@Feature("Posts")
@Story("Delete Posts")
class DeletePostsTests {

    @ParameterizedTest(name = "Delete existing post with ID {0} successfully")
    @MethodSource("tech.themukha.placeholdertests.providers.PostsDataProvider#validDeletePostProvider")
    @DisplayName("Delete existing post successfully")
    fun `Delete existing post successfully`(postId: Int) {

        TestFlow()
            .step("Delete post with ID $postId") {
                `Delete an existing post`(
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
    @MethodSource("tech.themukha.placeholdertests.providers.PostsDataProvider#invalidPostIdProvider")
    @DisplayName("Delete non-existing post by ID should fail")
    fun `Delete non-existing post by ID should fail`(invalidPostId: Int) {

        TestFlow()
            .step("Try to delete a non-existing post") {
                `Delete an existing post`(
                    invalidPostId,
                    HttpStatus.SC_NOT_FOUND
                )
            }
    }

}