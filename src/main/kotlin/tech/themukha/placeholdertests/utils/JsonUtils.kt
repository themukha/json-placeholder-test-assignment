package tech.themukha.placeholdertests.utils

import com.google.gson.Gson

object JsonUtils {
    private val gson = Gson()

    fun <T> fromJson(json: String, classOfT: Class<T>): T {
        return gson.fromJson(json, classOfT)
    }

    fun toJson(obj: Any): String {
        return gson.toJson(obj)
    }
}