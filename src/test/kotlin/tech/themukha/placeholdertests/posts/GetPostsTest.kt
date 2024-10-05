package tech.themukha.placeholdertests.posts

import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tech.themukha.placeholdertests.api.PostsApi

@DisplayName("Get Post tests")
class GetPostsTests {
    @Test
    fun `Get all posts successfully`() {
        val expectedPostsSize = 100
        val posts = PostsApi.`Get all posts`()!!

        assertAll(
            { assertTrue(posts.isNotEmpty(), "No posts found") },
            { assertEquals(expectedPostsSize, posts.size, "Expected $expectedPostsSize posts, but got ${posts.size}") }
        )
    }

    @ParameterizedTest(name = "Get post with valid ID {0}")
    @ValueSource(ints = [1, 50, 100])
    fun `Get post with valid ID`(postId: Int) {
        val post = PostsApi.`Get post by ID`(postId)!!

        assertEquals(postId, post.id, "Expected post ID $postId, but got ${post.id}")
    }

    @ParameterizedTest(name = "Try to get non-existing post by ID {0}")
    @ValueSource(ints = [0, 101, -1])
    fun `Try to get non-existing post by ID`(invalidPostId: Int) {
        PostsApi.`Get post by ID`(
            postId = invalidPostId,
            expectedResponseCode = HttpStatus.SC_NOT_FOUND
        )
    }

}