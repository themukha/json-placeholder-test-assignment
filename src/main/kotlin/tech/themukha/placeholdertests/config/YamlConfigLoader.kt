package tech.themukha.placeholdertests.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File

internal object YamlConfigLoader {
    private val mapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule.Builder().build())

    fun loadConfig(path: String): TestConfigDto {
        return mapper.readValue(File(path), TestConfigDto::class.java)
    }
}