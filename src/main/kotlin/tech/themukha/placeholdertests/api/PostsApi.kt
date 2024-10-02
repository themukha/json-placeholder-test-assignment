package tech.themukha.placeholdertests.api

import io.qameta.allure.Step
import io.restassured.RestAssured.baseURI
import org.apache.http.HttpStatus
import tech.themukha.placeholdertests.api.ApiHelper.callApi
import tech.themukha.placeholdertests.config.TestConfig
import tech.themukha.placeholdertests.dto.PostDto

object PostsApi {

    init {
        baseURI = TestConfig.BASE_URL
    }

    @Step("Get all posts")
    fun `Get all posts`(expectedResponseCode: Int = HttpStatus.SC_OK): List<PostDto>? {
        return callApi(Endpoint.GET_ALL_POSTS, expectedResponseCode = expectedResponseCode)
    }

    @Step("Get post with ID `{postId}`")
    fun `Get post by ID`(postId: Int, expectedResponseCode: Int = HttpStatus.SC_OK): PostDto? {
        return callApi(Endpoint.GET_POST_BY_ID, pathParams = mapOf("postId" to postId), expectedResponseCode = expectedResponseCode)
    }

    @Step("Create a new post")
    fun `Create a new post`(newPost: PostDto, expectedResponseCode: Int = HttpStatus.SC_CREATED): PostDto? {
        return callApi(Endpoint.CREATE_POST, requestBody = newPost, expectedResponseCode = expectedResponseCode)
    }

    @Step("Update an existing post with ID `{postId}`")
    fun `Update an existing post`(postId: Int, updatedPost: PostDto, expectedResponseCode: Int = HttpStatus.SC_OK): PostDto? {
        return callApi(Endpoint.UPDATE_POST, pathParams = mapOf("postId" to postId), requestBody = updatedPost, expectedResponseCode = expectedResponseCode)
    }

    @Step("Partially update an existing post with ID `{postId}`")
    fun `Patch an existing post`(postId: Int, updatedPost: PostDto, expectedResponseCode: Int = HttpStatus.SC_OK): PostDto? {
        return callApi(Endpoint.PATCH_POST, pathParams = mapOf("postId" to postId), requestBody = updatedPost, expectedResponseCode = expectedResponseCode)
    }

    @Step("Delete a post with ID `{postId}`")
    fun `Delete an existing post`(postId: Int, expectedResponseCode: Int = HttpStatus.SC_OK): PostDto? {
        return callApi(Endpoint.DELETE_POST, pathParams = mapOf("postId" to postId), expectedResponseCode = expectedResponseCode)
    }

}