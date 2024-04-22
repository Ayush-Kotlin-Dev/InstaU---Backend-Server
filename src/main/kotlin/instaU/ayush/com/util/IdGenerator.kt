package instaU.ayush.com.util

import de.mkammerer.snowflakeid.SnowflakeIdGenerator

//private val generatorId = System.getenv("id.generator")
private val generatorId = "1"

object IdGenerator {
    fun generateId(): Long{
        return SnowflakeIdGenerator.createDefault(generatorId.toInt()).next()
    }
}