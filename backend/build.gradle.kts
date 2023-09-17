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
    implementation(libs.ktor.server.netty)
}


application {
    mainClass = "ApplicationKt"
}