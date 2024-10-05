package tech.themukha.placeholdertests.posts

import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tech.themukha.placeholdertests.api.PostsApi
import tech.themukha.placeholdertests.dto.PostDto
import tech.themukha.placeholdertests.flow.TestFlow

@DisplayName("Get Posts tests")
@Feature("Posts")
@Story("Get Posts")
class GetPostsTests {

    @Nested
    @DisplayName("Get posts with valid parameters")
    inner class GetPostsValidTest {
        @Test
        @DisplayName("Get all posts successfully")
        fun `Get all posts successfully`() {
            val expectedPostsSize = 100
            var posts: List<PostDto>? = null

            TestFlow()
                .step("Getting all posts") {
                    posts = PostsApi.`Get all posts`()!!
                }
                .step("Check all posts were successful got") {
                    assertAll(
                        { assertTrue(posts!!.isNotEmpty(), "No posts found") },
                        { assertEquals(expectedPostsSize, posts!!.size, "Expected $expectedPostsSize posts, but got ${posts!!.size}") }
                    )
                }
        }

        @ParameterizedTest(name = "Get post with valid ID {0}")
        @ValueSource(ints = [1, 50, 100])
        @DisplayName("Get post with valid ID")
        fun `Get post with valid ID`(postId: Int) {
            var post: PostDto? = null

            TestFlow()
                .step("Getting post by ID") {
                    post = PostsApi.`Get post by ID`(postId)!!
                }
                .step("Check post was successful got") {
                    assertEquals(postId, post?.id, "Expected post ID $postId, but got ${post?.id}")
                }
        }
    }

    @Nested
    @DisplayName("Get posts with invalid parameters")
    inner class GetPostsInvalidTest {
        @ParameterizedTest(name = "Try to get non-existing post by ID {0}")
        @ValueSource(ints = [0, 101, -1])
        @DisplayName("Try to get non-existing post by ID")
        fun `Try to get non-existing post by ID`(invalidPostId: Int) {
            TestFlow()
                .step("Try to get post with wrong ID") {
                    PostsApi.`Get post by ID`(
                        postId = invalidPostId,
                        expectedResponseCode = HttpStatus.SC_NOT_FOUND
                    )
                }
        }
    }

}