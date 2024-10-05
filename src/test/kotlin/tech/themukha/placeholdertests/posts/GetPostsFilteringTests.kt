package tech.themukha.placeholdertests.posts

import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import tech.themukha.placeholdertests.api.PostsApi
import tech.themukha.placeholdertests.dto.PostDto
import tech.themukha.placeholdertests.flow.TestFlow

@DisplayName("Get Posts with Filtering Tests")
@Feature("Posts")
@Story("Get Posts With Filtering")
class GetPostsFilteringTests {

    @ParameterizedTest(name = "Get posts with filtering by {0} successfully")
    @MethodSource("tech.themukha.placeholdertests.providers.PostsDataProvider#validGetPostsFilteringProvider")
    @DisplayName("Get posts with filtering")
    fun `Get posts with filtering`(filterParam: String, expectedPost: PostDto) {
        var filteredPosts: List<PostDto>? = null

        TestFlow()
            .step("Get posts with filtering by $filterParam") {
                filteredPosts = when (filterParam) {
                    "id" -> PostsApi.`Get all posts`(filterId = expectedPost.id)
                    "userId" -> PostsApi.`Get all posts`(filterUserId = expectedPost.userId)
                    "title" -> PostsApi.`Get all posts`(filterTitle = expectedPost.title)
                    "body" -> PostsApi.`Get all posts`(filterBody = expectedPost.body)
                    else -> throw IllegalArgumentException("Invalid filter parameter: $filterParam")
                }
            }
            .step("Chech that filtered posts are not null and not empty") {
                assertNotNull(filteredPosts, "Filtered posts should not be null")
                assertTrue(filteredPosts?.isNotEmpty() ?: false, "No posts found")
            }
            .step("Check each filtered post") {
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

    @ParameterizedTest(name = "Try to get posts with invalid filtering by {0}")
    @MethodSource("tech.themukha.placeholdertests.providers.PostsDataProvider#invalidGetPostsFilteringProvider")
    @DisplayName("Try to get posts with invalid filtering")
    fun `Try to get posts with invalid filtering`(filterParam: String, expectedPost: PostDto) {
        var filteredPosts: List<PostDto>? = null

        TestFlow()
            .step("Get posts with invalid filtering by $filterParam expecting empty list") {
                filteredPosts = when (filterParam) {
                    "id" -> PostsApi.`Get all posts`(filterId = expectedPost.id)
                    "userId" -> PostsApi.`Get all posts`(filterUserId = expectedPost.userId)
                    "title" -> PostsApi.`Get all posts`(filterTitle = expectedPost.title)
                    "body" -> PostsApi.`Get all posts`(filterBody = expectedPost.body)
                    else -> throw IllegalArgumentException("Invalid filter parameter: $filterParam")
                }
                assertNotNull(filteredPosts, "Filtered posts should not be null")
                assertTrue(filteredPosts!!.isEmpty(), "Filtered posts should be empty, but got ${filteredPosts?.size} objects")
            }
    }

}