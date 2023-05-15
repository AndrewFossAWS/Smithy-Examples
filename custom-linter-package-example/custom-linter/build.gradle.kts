extra["displayName"] = "Smithy :: Custom Linter :: Package"

dependencies {
    val smithyVersion: String by project

    implementation("software.amazon.smithy:smithy-model:$smithyVersion")
}