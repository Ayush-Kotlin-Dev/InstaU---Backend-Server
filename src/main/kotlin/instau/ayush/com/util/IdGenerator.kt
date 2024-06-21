package instau.ayush.com.util

import de.mkammerer.snowflakeid.SnowflakeIdGenerator

private val generatorId = System.getenv("idGenerator")

object IdGenerator {
    fun generateId(): Long{
        return SnowflakeIdGenerator.createDefault(generatorId.toInt()).next()
    }
}