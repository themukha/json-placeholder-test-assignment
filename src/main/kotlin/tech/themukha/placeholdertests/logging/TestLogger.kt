package tech.themukha.placeholdertests.logging

import io.qameta.allure.listener.ContainerLifecycleListener
import io.qameta.allure.listener.StepLifecycleListener
import io.qameta.allure.model.StepResult
import io.qameta.allure.model.TestResultContainer
import org.slf4j.LoggerFactory

class TestLogger : StepLifecycleListener, ContainerLifecycleListener {

    override fun beforeContainerStart(container: TestResultContainer?) {
        logger.info(container?.name)
    }

    override fun beforeStepStart(result: StepResult) {
        logger.info(result.name)
    }

    fun info(message: String) {
        logger.info(message)
    }

    fun debug(message: String) {
        logger.debug(message)
    }

    fun error(message: String) {
        logger.error(message)
    }

    fun warn(message: String) {
        logger.warn(message)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}