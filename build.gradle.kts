plugins {
    application
    id("com.diffplug.spotless") version "8.10.4"
}

group = "com.example"
version = "0.1.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "com.example.app.Greeting"
}

tasks.test {
    useJUnitPlatform()
}

spotless {
    // Set with -PratchetFrom=<git-ref> to format only files that differ from that ref.
    providers.gradleProperty("ratchetFrom").orNull?.takeIf { it.isNotBlank() }?.let { ratchetFrom(it) }

    java {
        target("src/**/*.java")
        googleJavaFormat()
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks.register("installGitHooks") {
    group = "setup"
    description = "Installs a pre-commit hook that rejects commits when Spotless finds formatting violations."
    doLast {
        val source = file(".githooks/pre-commit")
        val target = file(".git/hooks/pre-commit")
        if (!source.isFile) {
            throw GradleException("Missing hook script: $source")
        }
        target.parentFile.mkdirs()
        source.copyTo(target, overwrite = true)
        if (!target.setExecutable(true)) {
            throw GradleException("Could not mark $target as executable.")
        }
        logger.lifecycle("Installed Spotless pre-commit hook at $target")
    }
}
