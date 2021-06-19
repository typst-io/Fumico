version = "0.1.0-SNAPSHOT"

dependencies {
    implementation("io.arrow-kt", "arrow-core", "0.13.2")
    testImplementation("io.mockk", "mockk", "1.11.0")
}

tasks {
    test {
        testLogging.showStandardStreams = true
    }
}