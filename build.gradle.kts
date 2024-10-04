val junitVersion: String = "5.11.1"
val restAssuredVersion: String = "5.5.0"
val allureVersion: String = "2.29.0"
val gsonVersion: String = "2.11.0"
val slf4jVersion: String = "2.0.16"

plugins {
    kotlin("jvm") version "1.9.23"
}

group = "tech.themukha"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    implementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    implementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    implementation("io.rest-assured:rest-assured:$restAssuredVersion")
    implementation("io.qameta.allure:allure-junit5:$allureVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    implementation("com.google.code.gson:gson:$gsonVersion")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}