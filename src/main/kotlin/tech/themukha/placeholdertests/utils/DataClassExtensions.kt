package tech.themukha.placeholdertests.utils

import kotlin.reflect.full.memberProperties

object DataClassExtensions {
    inline fun <reified T : Any> T.toParams(): Map<String, Any>? {
        return T::class.memberProperties
            .filter { property -> property.get(this) != null }
            .associate { property -> property.name to property.get(this) as Any }
            .takeIf { it.isNotEmpty() }
    }
}