plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
}

group = "com.alilopez"
version = "0.0.1"


application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.swagger)
    //implementation(libs.ktor.server.routing.openapi)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)

    // Koin para Ktor
    implementation("io.insert-koin:koin-ktor:3.5.0")
    //implementation("io.insert-koin:koin-logger-slf4j:3.5.0")

    // Base de Datos
    implementation(libs.bundles.exposed)
    implementation(libs.postgresql)
    implementation(libs.hikaricp)

    ///claudinary
    implementation("com.cloudinary:cloudinary-http5:2.0.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    // Serialization
    implementation(libs.ktor.serialization.kotlinx.json)
    //implementation("io.ktor:ktor-server-content-negotiation-jvm")
    //implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
}
