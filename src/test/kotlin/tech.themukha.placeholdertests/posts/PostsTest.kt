package tech.themukha.placeholdertests.posts

import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import tech.themukha.placeholdertests.api.PostsApi
import tech.themukha.placeholdertests.dto.PostDto

class PostsTest {
    @Nested
    @DisplayName("Create Post tests")
    inner class CreatePostsTest {
        @Test
        fun `Create post successfully`() {
            val newPost = PostDto(
                id = 0,
                userId = 1,
                title = "Test 1 title",
                body = "Test 1 body"
            )
            val createdPost = PostsApi.`Create a new post`(newPost, HttpStatus.SC_CREATED)

            assertAll(
                { assertNotEquals(newPost.id, createdPost?.id, "Post id should be generated on backend") },
                { assertEquals(newPost.userId, createdPost?.userId) },
                { assertEquals(newPost.title, createdPost?.title) },
                { assertEquals(newPost.body, createdPost?.body) }
            )
        }
    }

    @Nested
    @DisplayName("Get Post tests")
    inner class GetPostsTests {
        @Test
        fun `Get all posts successfully`() {
            val posts = PostsApi.`Get all posts`()!!

            assertTrue(posts.isNotEmpty(), "No posts found")
        }

        @Test
        fun `Get existing post by ID successfully`() {
            val postId = 1
            val post = PostsApi.`Get post by ID`(postId)!!

            assertEquals(postId, post.id, "Expected post ID $postId, but got ${post.id}")
        }

        @Test
        fun `Get non-existing post by ID returns 404`(){
            PostsApi.`Get post by ID`(101, 404)
        }

    }
}