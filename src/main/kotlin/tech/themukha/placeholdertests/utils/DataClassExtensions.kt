package tech.themukha.placeholdertests.utils

import tech.themukha.placeholdertests.dto.PostDto
import kotlin.reflect.full.memberProperties

object DataClassExtensions {
    inline fun <reified T : Any> T.toParams(): Map<String, Any>? {
        return T::class.memberProperties
            .filter { property -> property.get(this) != null }
            .associate { property -> property.name to property.get(this) as Any }
            .takeIf { it.isNotEmpty() }
    }

    fun List<PostDto>?.getLatestPost(): PostDto? {
        if (this == null) return null
        return this.maxByOrNull { it.id ?: return null }
    }

    fun List<PostDto>?.getRandomPost(): PostDto? {
        return this?.randomOrNull()
    }

    fun List<PostDto>?.getRandomPosts(count: Int): List<PostDto>? {
        if (this == null) return null
        return this.shuffled().take(count)
    }

}