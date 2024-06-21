
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val postgres_version : String by project
val h2_version : String by project
val exposed_version : String by project

val koin_version: String by project
val hikaricp_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
}

group = "instaU.ayush.com"
version = "0.0.1"

application {
    mainClass.set("instau.ayush.com.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("org.postgresql:postgresql:42.5.1")
    implementation("com.h2database:h2:2.1.214")
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("com.h2database:h2:2.1.214")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")



    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")

    implementation("de.mkammerer.snowflake-id:snowflake-id:0.0.2")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("io.ktor:ktor-server-websockets-jvm")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")

    tasks.register<Jar>("fatJar") {
        manifest {
            attributes["Main-Class"] = "instau.ayush.com.ApplicationKt"
        }
        from(*configurations.runtimeClasspath.get().filter { it.exists() }.map { if (it.isDirectory) it else zipTree(it) }.toTypedArray())
        with(tasks.getByName("jar") as CopySpec)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

}
