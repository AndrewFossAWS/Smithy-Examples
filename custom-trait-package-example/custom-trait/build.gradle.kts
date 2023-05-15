extra["displayName"] = "Smithy :: Custom Validator :: Package"

plugins {
    val smithyGradleVersion: String by project

    id("software.amazon.smithy").version(smithyGradleVersion)
}

buildscript {
    val smithyVersion: String by project

    // Set the version of the CLI for the smithy gradle plugin to use when building this project
    dependencies {
        classpath("software.amazon.smithy:smithy-cli:$smithyVersion")
    }
}

dependencies {
    val smithyVersion: String by project

    implementation("software.amazon.smithy:smithy-model:$smithyVersion")
}