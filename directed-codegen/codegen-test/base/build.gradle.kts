extra["displayName"] = "Smithy :: Codegen :: Test :: Base"

// Test project does not generate a jar
tasks["jar"].enabled = false


plugins {
    id("software.amazon.smithy").version("0.7.0")
}

buildscript {
    repositories {
        mavenCentral()
    }

    // Set the version of the CLI for the smithy gradle plugin to use when building this project
    dependencies {
        classpath("software.amazon.smithy:smithy-cli:1.31.0")
    }
}

dependencies {
    // Add the codegen plugin as a dependency so it can be applied to the models
    implementation(project(":codegen"))

    // Add the common example model to the dependencies so we can use the model for building
    implementation(project(":codegen-test:example-model"))
}