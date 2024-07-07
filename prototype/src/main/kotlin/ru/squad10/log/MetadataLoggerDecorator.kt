package ru.squad10.log

import java.time.Instant

class MetadataLoggerDecorator(private val logger: Logger) : Logger() {
    override fun log(message: String) {
        logger.log("[INFO] [${Instant.now()}] $message")
    }
}