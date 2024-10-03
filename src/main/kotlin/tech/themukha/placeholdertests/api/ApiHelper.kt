package tech.themukha.placeholdertests.api

import io.restassured.RestAssured.baseURI
import io.restassured.RestAssured.delete
import io.restassured.RestAssured.get
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
        expectedResponseCode: Int = HttpStatus.SC_OK,
    ): T? {
        val response = executeRequest(endpoint, requestBody, pathParams, expectedResponseCode)

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
        expectedResponseCode: Int = HttpStatus.SC_OK,
    ): Response {

        val formattedPath = endpoint.setPathParams(pathParams)
        var request: RequestSpecification = given()
        if (requestBody != null) {
            request = request.contentType(ContentType.JSON).body(toJson(requestBody))
        }


        return when (endpoint.method) {
            Method.GET -> get(formattedPath)
            Method.POST -> request.post(formattedPath)
            Method.PUT -> request.put(formattedPath)
            Method.PATCH -> request.patch(formattedPath)
            Method.DELETE -> delete(formattedPath)
            else -> throw IllegalArgumentException("Unsupported method")
        }.then().statusCode(expectedResponseCode).extract().response()
    }
}