import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    id("com.vanniktech.maven.publish") version "0.36.0"
    id("signing")
}

android {
    namespace = "m.a.compilot.navigation"
    compileSdk = 36

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
}

dependencies {
    implementation(libs.compose.navigation)
    api(project(":ComPilot:common"))
}

val properties = Properties()
properties.load(FileInputStream(rootProject.file("local.properties")))
group = properties.getProperty("publication.group")
version = libs.versions.compilotVersion.get()

mavenPublishing {
    coordinates(
        groupId = group as String,
        artifactId = "navigation",
        version = version as String
    )

    pom {
        name = "ComPilot KMP"
        description = "Type-safe navigation for jetpack compose!"
        url = "https://github.com/mahmoudafarideh/compilot-kmp"

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
            url = "https://github.com/mahmoudafarideh/compilot-kmp"
            connection = "scm:git:https://github.com/mahmoudafarideh/compilot-kmp.git"
            developerConnection = "scm:git:git@github.com:mahmoudafarideh/compilot-kmp.git"
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral()

    // Enable GPG signing for all publications
    signAllPublications()
}

signing {
    val properties = Properties()
    properties.load(FileInputStream(rootProject.file("local.properties")))
    useInMemoryPgpKeys(
        properties.getProperty("publication.key"),
        properties.getProperty("publication.secretKey"),
        properties.getProperty("publication.password"),
    )
    sign(publishing.publications)
}