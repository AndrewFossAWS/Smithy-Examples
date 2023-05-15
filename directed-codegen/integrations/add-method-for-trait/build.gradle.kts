extra["displayName"] = "Smithy :: Codegen :: Integrations :: ReType"
extra["moduleName"] = "io.smithy.examples.codegen.integrations.retype"

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
    val smithyVersion: String by project

    // Add the codegen plugin as a dependency so we can depend on the CodegenIntegration Interface
    implementation(project(":codegen"))
    implementation("software.amazon.smithy:smithy-codegen-core:$smithyVersion")
}