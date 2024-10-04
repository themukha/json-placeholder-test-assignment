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

class PostsTest {
    @Nested
    @DisplayName("Create Post tests")
    inner class CreatePostsTest {

        @ParameterizedTest(name = "Create post {index} successfully")
        @MethodSource("tech.themukha.placeholdertests.posts.PostsDataProvider#validCreatePostProvider")
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
        @ParameterizedTest(name = "Try to create post {index} with invalid data")
        @MethodSource("tech.themukha.placeholdertests.posts.PostsDataProvider#invalidCreatePostProvider")
        fun `Try to create post with invalid data`(invalidPost: PostDto) {
            PostsApi.`Create a new post`(
                newPost = invalidPost,
                /**
                 * Или другой ожидаемый код ответа.
                 * Также, если бы был определенный формат у тела ответа с ошибкой, условный ErrorResponse,
                 * я бы добавил валидацию кода ошибки, например expectedErrorCode = "INVALID_VALUE"
                **/
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

    @Nested
    @DisplayName("Get Posts with Filtering Tests")
    inner class GetPostsFilteringTests {

        @ParameterizedTest(name = "Get posts with filtering by {0} successfully")
        @MethodSource("tech.themukha.placeholdertests.posts.PostsDataProvider#validGetPostsFilteringProvider")
        fun `Get posts with filtering`(filterParam: String, expectedPost: PostDto) {
            val filteredPosts = when (filterParam) {
                "id" -> PostsApi.`Get all posts`(filterId = expectedPost.id)
                "userId" -> PostsApi.`Get all posts`(filterUserId = expectedPost.userId)
                "title" -> PostsApi.`Get all posts`(filterTitle = expectedPost.title)
                "body" -> PostsApi.`Get all posts`(filterBody = expectedPost.body)
                else -> throw IllegalArgumentException("Invalid filter parameter: $filterParam")
            }

            assertTrue(filteredPosts?.isNotEmpty() ?: false, "No posts found")
            assertAll({
                filteredPosts?.forEachIndexed { index, post ->
                    when (filterParam) {
                        "id" -> assertEquals(expectedPost.id, post.id, "Expected post ID ${expectedPost.id}, but got ${post.id} at index $index")
                        "userId" -> assertEquals(expectedPost.userId, post.userId, "Expected post userId ${expectedPost.userId}, but got ${post.userId} at index $index")
                        "title" -> assertEquals(expectedPost.title, post.title, "Expected post title ${expectedPost.title}, but got ${post.title} at index $index")
                        "body" -> assertEquals(expectedPost.body, post.body, "Expected post body ${expectedPost.body}, but got ${post.body} at index $index")
                        else -> throw IllegalArgumentException("Invalid filter parameter: $filterParam")
                    }
                }
            })
        }

    }

}