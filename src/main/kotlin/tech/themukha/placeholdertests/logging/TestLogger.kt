package tech.themukha.placeholdertests.logging

import org.slf4j.LoggerFactory

object TestLogger {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun info(message: String) {
        logger.info(message)
    }

    fun debug(message: String) {
        logger.debug(message)
    }

    fun error(message: String) {
        logger.debug(message)
    }

    fun warn(message: String) {
        logger.debug(message)
    }
}