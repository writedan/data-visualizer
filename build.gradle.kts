import org.gradle.api.tasks.JavaExec

plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.scijava.org/content/repositories/public/")
    }
}

dependencies {
    implementation("com.opencsv:opencsv:5.7.1")
    implementation("org.openjfx:javafx-controls:11")
    implementation("org.openjfx:javafx-graphics:11")
}
