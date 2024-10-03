package tech.themukha.placeholdertests.posts

import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import tech.themukha.placeholdertests.api.PostsApi
import tech.themukha.placeholdertests.dto.PostDto
import java.util.stream.Stream

class PostsTest {
    @Nested
    @DisplayName("Create Post tests")
    inner class CreatePostsTest {

        @ParameterizedTest(name = "Create post {index} successfully")
        @MethodSource("tech.themukha.placeholdertests.posts.PostsTest#validCreatePostProvider")
        fun `Create post successfully`(newPost: PostDto) {
            val createdPost = PostsApi.`Create a new post`(newPost, HttpStatus.SC_CREATED)
            val lastPostId = PostsApi.`Get all posts`()
                ?.sortedByDescending { it.id }
                ?.first()?.id
                ?: 0

            assertAll(
                { assertEquals(lastPostId.plus(1), createdPost?.id, "Post id should be generated on backend") },
                { assertEquals(newPost.userId, createdPost?.userId) },
                { assertEquals(newPost.title, createdPost?.title) },
                { assertEquals(newPost.body, createdPost?.body) }
            )
        }

        /** Ожидаем, что параметры userId, title и body в запросе не могут быть == null.
         * Но в случае с данным API мы получаем 201 ответ и "валидное" сохранение ресурса.
         * Так как в гайде (буду считать за техническую спецификацию) API отсутствует какая-либо информация о валидации,
         * Я бы обратился к аналитику с вопросом - как себя должна вести система при таких входных данных,
         * Вполне возможно что это некорректное поведение, а кейсы не были учтены при системном анализе.
         * Если мои опасения подтвердились - заводим один баг репорт и описываем некорректную валидацию входящего тела запроса.
         * Но вопрос об отсутствии правил валидации данных у меня возник бы ещё на этапе статического тестирования требований.
         */
        @ParameterizedTest(name = "Create post {index} failure")
        @MethodSource("tech.themukha.placeholdertests.posts.PostsTest#invalidCreatePostProvider")
        fun `Create post failure`(invalidPost: PostDto) {
            PostsApi.`Create a new post`(
                newPost = invalidPost,
                expectedResponseCode = HttpStatus.SC_BAD_REQUEST
            )
        }
    }

    @Nested
    @DisplayName("Get Post tests")
    inner class GetPostsTests {
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
        fun `Get existing post by ID successfully`(postId: Int) {
            val post = PostsApi.`Get post by ID`(postId)!!

            assertEquals(postId, post.id, "Expected post ID $postId, but got ${post.id}")
        }

        @ParameterizedTest(name = "Get post with invalid ID {0} should fail")
        @ValueSource(ints = [0, 101, -1]) // Граничные значения и отрицательное значение
        fun `Get non-existing post by ID should returns 404`(invalidPostId: Int) {
            PostsApi.`Get post by ID`(
                postId = invalidPostId,
                expectedResponseCode = HttpStatus.SC_NOT_FOUND
            )
        }

    }

    companion object {
        @JvmStatic
        fun validCreatePostProvider(): Stream<PostDto> = Stream.of(
            PostDto(id = 0, userId = 1, title = "Test title 1", body = "Test body 1"),
            PostDto(id = -1, userId = 10, title = "Test title 2", body = "Test body 2"),
            PostDto(id = 100, userId = 3, title = "", body = "Test body 3"),
            PostDto(id = 101, userId = 4, title = "Test title 4", body = ""),
            PostDto(id = 1, userId = 5, title = "", body = ""),
            PostDto(id = 1, userId = 6, title = " ", body = " "),
            PostDto(id = 1, userId = 7, title = "Very long title".repeat(100), body = "Very long body".repeat(100)),
            PostDto(id = null, userId = 8, title = "Test title 8", body = "Test body 8"),
        )

        @JvmStatic
        fun invalidCreatePostProvider(): Stream<PostDto> = Stream.of(
            PostDto(id = 1, userId = null, title = "Test title 1", body = "Test body 1"),
            PostDto(id = 1, userId = 1, title = null, body = "Test body 2"),
            PostDto(id = 1, userId = 1, title = "Test title 3", body = null),
        )
    }
}