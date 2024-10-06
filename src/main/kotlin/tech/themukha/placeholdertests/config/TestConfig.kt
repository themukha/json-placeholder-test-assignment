package tech.themukha.placeholdertests.config

object TestConfig {
    private var config: TestConfigDto = YamlConfigLoader.loadConfig("src/test/resources/configs/test.yaml")

    val BASE_URL = config.service.baseUrl
}