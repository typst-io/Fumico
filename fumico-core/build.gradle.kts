version = "0.1.0-SNAPSHOT"

dependencies {
    implementation(project(":parsecom"))

    testImplementation("io.mockk", "mockk", "1.11.0")
}

tasks {
    test {
        testLogging.showStandardStreams = true
    }
}