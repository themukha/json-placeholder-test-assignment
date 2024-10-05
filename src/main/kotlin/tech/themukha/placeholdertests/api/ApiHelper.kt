package tech.themukha.placeholdertests.api

import io.restassured.RestAssured.baseURI
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.http.Method
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.apache.http.HttpStatus
import tech.themukha.placeholdertests.config.TestConfig
import tech.themukha.placeholdertests.exceptions.ApiException
import tech.themukha.placeholdertests.utils.JsonUtils.toJson
import tech.themukha.placeholdertests.utils.RestAssuredExtensions.extractAs

object ApiHelper {
    init {
        baseURI = TestConfig.BASE_URL
    }

    inline fun <reified T> callApi(
        endpoint: Endpoint,
        requestBody: Any? = null,
        pathParams: Map<String, Any>? = null,
        queryParams: Map<String, Any>? = null,
        expectedResponseCode: Int = HttpStatus.SC_OK,
    ): T? {
        val response = executeRequest(endpoint, requestBody, pathParams = pathParams, queryParams = queryParams, expectedResponseCode)

        if (response.statusCode != expectedResponseCode) throw ApiException(expectedResponseCode, response.statusCode, response.body.asString())

        if (response.statusCode in 200..299) {
            return response.extractAs<T>()
        }
        return null
    }

    @PublishedApi
    internal fun executeRequest(
        endpoint: Endpoint,
        requestBody: Any? = null,
        pathParams: Map<String, Any?>? = null,
        queryParams: Map<String, Any>? = null,
        expectedResponseCode: Int = HttpStatus.SC_OK,
    ): Response {

        val formattedPath = endpoint.setPathParams(pathParams)
        val request: RequestSpecification = given()

        requestBody?.let {
            request.contentType(ContentType.JSON).body(toJson(requestBody))
        }

        queryParams?.let {
            request.queryParams(queryParams)
        }

        return when (endpoint.method) {
            Method.GET -> request.get(formattedPath)
            Method.POST -> request.post(formattedPath)
            Method.PUT -> request.put(formattedPath)
            Method.PATCH -> request.patch(formattedPath)
            Method.DELETE -> request.delete(formattedPath)
            else -> throw IllegalArgumentException("Unsupported method")
        }.then().statusCode(expectedResponseCode).extract().response()
    }
}