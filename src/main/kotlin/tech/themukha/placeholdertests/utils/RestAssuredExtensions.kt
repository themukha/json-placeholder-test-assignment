package tech.themukha.placeholdertests.utils

import io.restassured.response.Response
import io.restassured.response.ValidatableResponse

object RestAssuredExtensions {

    inline fun <reified T> Response.extractAs(): T {
        return JsonUtils.fromJson(this.body.asString(), T::class.java)
    }

    inline fun <reified T> ValidatableResponse.extractAs(): T {
        return JsonUtils.fromJson(this.extract().body().asString(), T::class.java)
    }
}