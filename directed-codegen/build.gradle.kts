/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn more about Gradle by exploring our samples at https://docs.gradle.org/8.0.1/samples
 */

plugins {
    `java-library`
    id("com.github.spotbugs") version "4.7.1"
}

// The root project doesn't produce a JAR.
tasks["jar"].enabled = false

// SUBPROJECT SETTINGS
subprojects {
    val subproject = this

    /*
     * Java configuration
     * ====================================================
     * Set up all subprojects as java-libraries and configure the
     * default Java version as Java 17.
     */
    apply(plugin = "java-library")

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    // Use Junit5's test runner.
    tasks.withType<Test> {
        useJUnitPlatform()
    }

    /*
     * CheckStyle
     * ====================================================
     * https://docs.gradle.org/current/userguide/checkstyle_plugin.html
     */
    apply(plugin = "checkstyle")
    tasks["checkstyleTest"].enabled = false

    /*
     * Spotbugs
     * ====================================================
     */
    apply(plugin = "com.github.spotbugs")

    // We don't need to lint tests.
    tasks["spotbugsTest"].enabled = false

    // Configure the bug filter for spotbugs.
    spotbugs {
        setEffort("max")
        val excludeFile = File("${project.rootDir}/config/spotbugs/filter.xml")
        if (excludeFile.exists()) {
            excludeFilter.set(excludeFile)
        }
    }

    /*
     * Repositories
     * ====================================================
     * Add maven local and maven central as repositories for all
     * subprojects
     */
    repositories {
        mavenLocal()
        mavenCentral()
    }
}