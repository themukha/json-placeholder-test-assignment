package tech.themukha.placeholdertests.flow

import io.qameta.allure.Step
import tech.themukha.placeholdertests.logging.TestLogger

class TestFlow {

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