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
    implementation(libs.ktor.server.resources)


    val exposedVersion : String = "0.44.0"

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-crypt:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")

    implementation("com.h2database:h2:2.2.224")

}


application {
    mainClass = "ApplicationKt"
}