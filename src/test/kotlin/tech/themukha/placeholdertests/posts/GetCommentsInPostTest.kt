package tech.themukha.placeholdertests.posts

import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import tech.themukha.placeholdertests.api.PostsApi
import tech.themukha.placeholdertests.dto.CommentDto
import tech.themukha.placeholdertests.flow.TestFlow

@DisplayName("Get Comments In Post tests")
@Feature("Posts")
@Story("Get Comments")
class GetCommentsInPostTest {

    @ParameterizedTest(name = "Get comments with filtering {index}")
    @MethodSource("tech.themukha.placeholdertests.posts.PostsDataProvider#validGetCommentsProvider")
    @DisplayName("Get comments with filtering")
    fun `Get comments with filtering`(postId: Int, filter: CommentDto, expectedComment: CommentDto) {
        var comments: List<CommentDto>? = null

        TestFlow()
            .step("Getting comments with filtering") {
                comments = PostsApi.`Get comments for post`(postId = postId, expectedResponseCode = HttpStatus.SC_OK,
                    filterCommentId = filter.id,
                    filterEmail = filter.email,
                    filterName = filter.name
                )
            }
            .step("Check comments") {
                assertNotNull(comments) { "Comments list should not be null" }
                assertTrue(comments!!.isNotEmpty()) { "No comments found" }
                assertEquals(1, comments!!.size) { "Expected 1 comment, but got ${comments!!.size}" } // Expecting exactly one comment

                val actualComment = comments!![0]
                assertAll(
                    { assertEquals(expectedComment.id, actualComment.id, "Comment ID mismatch") },
                    { assertEquals(expectedComment.name, actualComment.name, "Comment name mismatch") },
                    { assertEquals(expectedComment.email, actualComment.email, "Comment email mismatch") },
                    { assertEquals(expectedComment.body, actualComment.body, "Comment body mismatch") }
                )
            }

    }

    /**
     * Получаем 200 ответ по несуществующим постам, у которых не может быть комментариев.
     * Идем к аналитикам и обсуждаем как должна вести себя система.
     * ЕСли система должна возвращать 404 или любой другой отличный от 200 ответ - заводим баг-репорт.
     * */
    @ParameterizedTest(name = "Get comments for non-existing post by ID should fail {index}")
    @MethodSource("tech.themukha.placeholdertests.posts.PostsDataProvider#invalidPostIdProvider")
    @DisplayName("Get comments for non-existing post by ID")
    fun `Get comments for non-existing post by ID`(nonExistingPostId: Int) {
        var comments: List<CommentDto>? = null

        TestFlow()
            .step("Get comments with filtering for non-existing post by ID expecting 404") {
                comments = PostsApi.`Get comments for post`(
                    nonExistingPostId,
                    expectedResponseCode = HttpStatus.SC_NOT_FOUND
                )
            }
    }
}