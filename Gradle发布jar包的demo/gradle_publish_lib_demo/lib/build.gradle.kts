/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java library project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.5/userguide/building_java_projects.html in the Gradle documentation.
 */
plugins {
    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    java
    id("com.gradle.plugin-publish") version "1.2.1"
}

// The project version will be used as your plugin version when publishing.
version = "1.0.2"
group = "io.github.nolan"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit test framework.
    testImplementation(libs.junit)

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api(libs.commons.math3)

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation(libs.guava)
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

gradlePlugin {
    website.set("https://github.com/F1Robert/stack-overflow-of-nolan")
    vcsUrl.set("https://github.com/F1Robert/stack-overflow-of-nolan")
    plugins {
        create("NolanGreetingsPlugin") {
            id = "io.github.nolan.greeting"
            implementationClass = "io.github.nolan.gradle.GreetingPlugin"
            displayName = "Gradle Greeting plugin"
            description = "Gradle plugin to say hello!"
            tags.set(listOf("search", "tags", "for", "your", "hello"))
        }
        create("NolanGoodbyePlugin") {
            id = "io.github.nolan.goodbye"
            implementationClass = "io.github.nolan.gradle.GoodbyePlugin"
            displayName = "Gradle Goodbye plugin"
            description = "Gradle plugin to say goodbye!"
            tags.set(listOf("search", "tags", "for", "your", "goodbye"))
        }
    }
}



