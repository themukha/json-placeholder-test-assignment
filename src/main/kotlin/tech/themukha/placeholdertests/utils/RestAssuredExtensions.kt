package tech.themukha.placeholdertests.utils

import com.google.gson.reflect.TypeToken
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse

object RestAssuredExtensions {

    inline fun <reified T> Response.extractAs(): T {
        val type = object : TypeToken<T>() {}.type
        return JsonUtils.fromJson(this.body.asString(), type)
    }

    inline fun <reified T> ValidatableResponse.extractAs(): T {
        return JsonUtils.fromJson(this.extract().body().asString(), T::class.java)
    }
}