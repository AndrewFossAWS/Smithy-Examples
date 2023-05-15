extra["displayName"] = "Smithy :: Codegen :: Integrations :: ReType"
extra["moduleName"] = "io.smithy.examples.codegen.integrations.retype"

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

dependencies {
    // Add the codegen plugin as a dependency so we can depend on the CodegenIntegration Interface
    implementation(project(":codegen"))
    implementation("software.amazon.smithy:smithy-codegen-core:1.31.0")
}