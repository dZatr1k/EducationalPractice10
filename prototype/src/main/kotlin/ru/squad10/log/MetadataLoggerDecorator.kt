package ru.squad10.log

import java.time.Instant

class MetadataLoggerDecorator(private val logger: Logger) : Logger() {
    override fun log(level: Level, message: String) {
        logger.log(level, "[${Instant.now()}] $message")
    }
}