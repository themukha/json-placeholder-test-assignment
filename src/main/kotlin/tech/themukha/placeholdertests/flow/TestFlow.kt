package tech.themukha.placeholdertests.flow

import io.qameta.allure.Step
import tech.themukha.placeholdertests.api.PostsApi
import tech.themukha.placeholdertests.logging.TestLogger

// позднее можем наследовать CommentsApi(), UsersApi() и т.п.
class TestFlow : PostsApi() {

    @Step("{stepName}")
    fun step(stepName: String, action: TestFlow.() -> Unit): TestFlow {
        logger.debug("Starting step `$stepName`")
        try {
            action()
            logger.debug("Step `$stepName` completed successfully")
        } catch (e: Throwable) {
            logger.error("Step `$stepName` failed: ${e.message}")
            throw e
        }
        return this
    }

    companion object {
        val logger = TestLogger()
    }

}