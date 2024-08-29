plugins {
    kotlin("jvm") version "2.0.0"
}

group = "funn.j2k"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    val ktor_version = "2.3.12"
    implementation("io.ktor:ktor-network:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC.2")
    testImplementation(kotlin("test"))
    implementation(kotlin("reflect"))
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}