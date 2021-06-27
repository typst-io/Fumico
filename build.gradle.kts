import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.5.20" apply false
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
}

detekt {
    config = files("$projectDir/config/detekt.yml")
}

group = "io.typecraft"
version = "0.1.0-SNAPSHOT"

subprojects {
    apply {
        plugin("kotlin")
    }


    group = rootProject.group

    repositories {
        mavenCentral()
    }

    val implementation by configurations
    val testImplementation by configurations

    dependencies {
        implementation(kotlin("stdlib"))
        testImplementation(kotlin("test"))
        testImplementation(kotlin("test-junit5"))
    }

    tasks {
        withType<Test>().configureEach {
            useJUnitPlatform()
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}
