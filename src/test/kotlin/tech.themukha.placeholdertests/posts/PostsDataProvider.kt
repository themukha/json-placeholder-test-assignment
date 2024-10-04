package tech.themukha.placeholdertests.posts

import org.junit.jupiter.params.provider.Arguments
import tech.themukha.placeholdertests.api.PostsApi
import tech.themukha.placeholdertests.dto.PostDto
import java.util.stream.Stream

object PostsDataProvider {
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

    @JvmStatic
    fun validGetPostsFilteringProvider(): Stream<Arguments> {
        val takePosts: Int = 4
        val posts: List<PostDto> = PostsApi
            .`Get all posts`()!!
            .shuffled().take(takePosts)
        return Stream.of(
            Arguments.of("id", posts[0]),
            Arguments.of("userId", posts[1]),
            Arguments.of("title", posts[2]),
            Arguments.of("body", posts[3]),
        )
    }
}