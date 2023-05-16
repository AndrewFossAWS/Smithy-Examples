# Common Shapes Package
This package provides an example of how to create a package to share 
common Smithy shapes. This reposi

## Building Package
You can build this common model package by running `./gradlew clean build` from the 
root of this package. That will build are JAR file that can be used as a dependency in 
other smithy projects. The generated JAR will use the 

## Pre-configured validators
This package 

## Next Steps
You can include this example within another gradle project or publish it to 
a maven repository to start using the generated jar as a common smithy model 
package.

TODO: SHOW HOW TO USE FOR GRADLE.
```kotlin
    implementation(project(":common-shapes"))
```

TODO: SHOW HOW TO USE WITH CLI.
Publish to local maven