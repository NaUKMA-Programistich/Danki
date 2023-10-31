plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation(project(":models"))
    implementation(libs.slf4j)

    implementation(libs.ktor.json)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.negotiation)
    implementation(libs.ktor.server.yaml)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.resources)

    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.validation)
    implementation(libs.ktor.server.status.pages)


    implementation(libs.exposed.core)
    implementation(libs.exposed.crypt)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)

    implementation(libs.h2)
    implementation(libs.jbcrypt)
    implementation(libs.postgresql)
    testImplementation(libs.ktor.client.resources)
    testImplementation(libs.ktor.client.serialization)
    testImplementation(libs.ktor.client.contentnegotiation)

    testImplementation(kotlin("test"))
    testImplementation(libs.mockk)
    testImplementation(libs.ktor.server.test.host)
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "ApplicationKt"
}