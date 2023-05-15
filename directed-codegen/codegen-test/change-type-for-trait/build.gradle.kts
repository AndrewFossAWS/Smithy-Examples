extra["displayName"] = "Smithy :: Codegen :: Test :: Change Type"

// Test project does not generate a jar
tasks["jar"].enabled = false

plugins {
    val smithyGradleVersion: String by project

    id("software.amazon.smithy").version(smithyGradleVersion)
}

buildscript {
    val smithyVersion: String by project

    repositories {
        mavenCentral()
    }

    // Set the version of the CLI for the smithy gradle plugin to use when building this project
    dependencies {
        classpath("software.amazon.smithy:smithy-cli:$smithyVersion")
    }
}

dependencies {
    // Add the codegen plugin as a dependency so it can be applied to the models
    implementation(project(":codegen"))

    // Add the common example model to the dependencies so we can use the model for building
    implementation(project(":codegen-test:example-model"))

    // Add our custom integration
    implementation(project(":integrations:change-type-for-trait"))
}