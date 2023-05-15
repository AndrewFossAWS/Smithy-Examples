description = "Generates code from Smithy models"
extra["displayName"] = "Smithy :: Examples :: Codegen"
extra["moduleName"] = "io.smithy.examples.codegen"

dependencies {
    val smithyVersion: String by project

    implementation("software.amazon.smithy:smithy-codegen-core:$smithyVersion")
    implementation("software.amazon.smithy:smithy-model:$smithyVersion")
    implementation("software.amazon.smithy:smithy-rules-engine:$smithyVersion")
}