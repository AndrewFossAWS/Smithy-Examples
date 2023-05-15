description = "Generates code from Smithy models"
extra["displayName"] = "Smithy :: Examples :: Codegen"
extra["moduleName"] = "io.smithy.examples.codegen"

dependencies {
    implementation("software.amazon.smithy:smithy-codegen-core:1.31.0")
    implementation("software.amazon.smithy:smithy-model:1.31.0")
    implementation("software.amazon.smithy:smithy-rules-engine:1.31.0")
}