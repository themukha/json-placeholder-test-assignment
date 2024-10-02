package tech.themukha.placeholdertests.api

import io.restassured.http.Method

enum class Endpoint(val method: Method, val path: String) {
    GET_ALL_POSTS(Method.GET, "/posts"),
    GET_POST_BY_ID(Method.GET, "/posts/{postId}"),
    CREATE_POST(Method.POST, "/posts"),
    UPDATE_POST(Method.PUT, "/posts/{postId}"),
    PATCH_POST(Method.PATCH, "/posts/{postId}"),
    DELETE_POST(Method.DELETE, "/posts/{postId}"),
    GET_COMMENTS_BY_POST_ID(Method.GET, "/posts/{postId}/comments");

    fun setPathParams(pathParams: Map<String, Any?>? = emptyMap()): String {
        var resultPath = path
        if (!pathParams.isNullOrEmpty()) {
            pathParams.forEach { (key, value) ->
                resultPath = resultPath.replace("{$key}", value.toString())
            }
        }
        return resultPath
    }
}