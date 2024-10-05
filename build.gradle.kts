import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

val junitVersion: String = "5.11.1"
val restAssuredVersion: String = "5.5.0"
val allureVersion: String = "2.29.0"
val gsonVersion: String = "2.11.0"
val slf4jVersion: String = "2.0.16"
val logbackVersion: String = "1.5.8"

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.qameta.allure") version "2.12.0"
}

group = "tech.themukha"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
    implementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    implementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    implementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    implementation("io.rest-assured:rest-assured:$restAssuredVersion")
    implementation("io.qameta.allure:allure-junit5:$allureVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion") // Logback
    implementation("ch.qos.logback:logback-core:$logbackVersion") // Logback
    implementation("com.google.code.gson:gson:$gsonVersion")
}

tasks.test {
    testLogging {
        events(
//            TestLogEvent.PASSED,
//            TestLogEvent.STARTED,
//            TestLogEvent.SKIPPED,
//            TestLogEvent.FAILED,
//            TestLogEvent.STANDARD_ERROR,
//            TestLogEvent.STANDARD_OUT,
        )

        exceptionFormat = TestExceptionFormat.SHORT
        showStandardStreams = true
        showCauses = true
        showExceptions = true
        showStackTraces = true
    }
    useJUnitPlatform()
    ignoreFailures = true
    finalizedBy(tasks.allureReport)
}

tasks.allureReport {
    dependsOn(tasks.test)
    allure {
        version = allureVersion
        report {
            enabled = true
            singleFile = true
        }
        adapter {
            enabled = true
        }
        clean = true
    }
}

kotlin {
    jvmToolchain(21)
}