import kr.entree.spigradle.kotlin.*

plugins {
    id("kr.entree.spigradle") version "2.2.4"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

version = "0.1.0-SNAPSHOT"

repositories {
    papermc()
}

dependencies {
    implementation(paper("1.16.5"))
    implementation(project(":fumico-core"))
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        dependencies {
            include(dependency("org.jetbrains.kotlin::"))
            include(dependency("io.typecraft::"))
        }
        relocate("kotlin", "io.typecraft.fumico.kotlin")
    }
}

artifacts {
    archives(tasks.shadowJar)
}

spigot {
    authors = listOf("Ranol_")
    apiVersion = "1.16"
    debug {
        buildVersion = "1.16.5"
    }
}