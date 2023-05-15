extra["displayName"] = "Smithy :: Codegen :: Example Model"

plugins {
    id("software.amazon.smithy").version("0.7.0")
}

buildscript {
    val smithyVersion: String by project

    repositories {
        mavenCentral()
    }

    // Set the version of the CLI for the smithy gradle plugin to use when building this project
    dependencies {
        classpath("software.amazon.smithy:smithy-cli:1.31.0")
    }
}
