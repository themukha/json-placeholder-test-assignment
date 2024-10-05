package tech.themukha.placeholdertests.posts

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import tech.themukha.placeholdertests.api.PostsApi
import tech.themukha.placeholdertests.dto.PostDto

@DisplayName("Get Posts with Filtering Tests")
class GetPostsFilteringTests {

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