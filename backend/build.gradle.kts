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

    implementation(libs.exposed.core)
    implementation(libs.exposed.crypt)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)

    implementation(libs.h2)
    implementation(libs.jbcrypt)

}


application {
    mainClass = "ApplicationKt"
}