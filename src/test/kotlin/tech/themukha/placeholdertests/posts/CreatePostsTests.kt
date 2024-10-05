package tech.themukha.placeholdertests.posts

import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import tech.themukha.placeholdertests.api.PostsApi
import tech.themukha.placeholdertests.dto.PostDto

@DisplayName("Create Post tests")
class CreatePostsTest {

    @ParameterizedTest(name = "Create post {index} successfully")
    @MethodSource("tech.themukha.placeholdertests.posts.PostsDataProvider#validCreatePostProvider")
    fun `Create post successfully`(newPost: PostDto) {
        val lastPostId = PostsApi.`Get all posts`()
            ?.sortedByDescending { it.id }
            ?.first()?.id
            ?: 0
        val createdPost = PostsApi.`Create a new post`(newPost, HttpStatus.SC_CREATED)

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