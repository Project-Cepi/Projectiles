import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.5.30"
    // Kotlinx serialization for any data format
    kotlin("plugin.serialization") version "1.5.30"
    // Shade the plugin
    id("com.github.johnrengelman.shadow") version "7.0.0"
    // Allow publishing
    `maven-publish`

    // Apply the application plugin to add support for building a jar
    java
    // Dokka documentation w/ kotlin
    id("org.jetbrains.dokka") version "1.5.30"
}

repositories {
    // Use mavenCentral
    mavenCentral()

    maven(url = "https://jitpack.io")
    maven(url = "https://repo.spongepowered.org/maven")
    maven(url = "https://repo.minestom.com/repository/maven-public/")
    maven(url = "https://repo.velocitypowered.com/snapshots/")
}

dependencies {
    // Align versions of all Kotlin components
    compileOnly(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    compileOnly(kotlin("stdlib"))

    // Use the Kotlin reflect library.
    compileOnly(kotlin("reflect"))

    // Compile Minestom into project
    compileOnly("com.github.Minestom:Minestom:21bac6fba4")

    // import kotlinx serialization
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")

    // add kstom
    compileOnly("com.github.Project-Cepi:KStom:dff80d98c4")

    // add mobs
    compileOnly("com.github.Project-Cepi:MobExtension:b1d7b0cc8e")

    // add energy
    compileOnly("com.github.Project-Cepi:EnergyExtension:12d8826a68")

    // Add Kepi
    compileOnly("com.github.Project-Cepi:Kepi:65de73c2d8")
}
tasks.withType<Test> {
    useJUnitPlatform()
}

configurations {
    testImplementation {
        extendsFrom(configurations.compileOnly.get())
    }
}

// Take gradle.properties and apply it to resources.
tasks {
    processResources {
        // Apply properties to extension.json
        filesMatching("extension.json") {
            expand(project.properties)
        }
    }

    // Set name, minimize, and merge service files
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set(project.name)
        mergeServiceFiles()
        minimize()
    }

    test { useJUnitPlatform() }

    // Make build depend on shadowJar as shading dependencies will most likely be required.
    build { dependsOn(shadowJar) }

}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}


val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()

compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-Xinline-classes")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.properties["group"] as? String?
            artifactId = project.name
            version = project.properties["version"] as? String?

            from(components["java"])
        }
    }
}