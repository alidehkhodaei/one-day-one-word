import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
    application
}

group = "org.words"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.h2database:h2:2.2.222")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation ("org.json:json:20230227")
    testImplementation(kotlin("test"))
    testImplementation("org.mockito:mockito-core:5.4.0")
    testImplementation("org.mockito:mockito-junit-jupiter:3.12.4")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("MainKt")
}