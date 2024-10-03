package tech.themukha.placeholdertests.utils

import com.google.gson.Gson
import java.lang.reflect.Type

object JsonUtils {
    private val gson = Gson()

    fun <T> fromJson(json: String, classOfT: Type): T {
        return gson.fromJson(json, classOfT)
    }

    fun toJson(obj: Any): String {
        return gson.toJson(obj)
    }
}