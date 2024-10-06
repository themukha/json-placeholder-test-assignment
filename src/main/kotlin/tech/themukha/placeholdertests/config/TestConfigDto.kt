package tech.themukha.placeholdertests.config

data class TestConfigDto(
    val service: ServiceConfig,
)

data class ServiceConfig(
    val baseUrl: String,
)
