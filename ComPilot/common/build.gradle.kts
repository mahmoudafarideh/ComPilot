import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("com.vanniktech.maven.publish") version "0.28.0"
    id("signing")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

group = "io.github.mahmoudafarideh.compilot"
version = libs.versions.compilotVersion.get()

mavenPublishing {
    coordinates(
        groupId = group as String,
        artifactId = "common",
        version = version as String
    )

    pom {
        name = "ComPilot"
        description = "Type-safe navigation for jetpack compose!"
        url = "https://github.com/mahmoudafarideh/compilot"

        licenses {
            license {
                name = "MIT License"
                url = "https://github.com/mahmoudafarideh/compilot/blob/main/LICENSE"
            }
        }

        developers {
            developer {
                id = "mahmoudafarideh"
                name = "Mahmoud A."
                email = "mahmoudafarideh@gmail.com"
                url = "https://github.io/mahmoudafarideh"
            }
        }

        scm {
            url = "https://github.com/mahmoudafarideh/compilot"
            connection = "scm:git:https://github.com/mahmoudafarideh/compilot.git"
            developerConnection = "scm:git:git@github.com:mahmoudafarideh/compilot.git"
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()
}

signing {
    useInMemoryPgpKeys(
        getLocalProperty("publication.key"),
        getLocalProperty("publication.secret"),
        getLocalProperty("publication.password"),
    )
    sign(publishing.publications)
}