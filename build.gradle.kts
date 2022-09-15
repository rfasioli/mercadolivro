import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.7.10"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.noarg") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    id("java-library")
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("jacoco")
    id("com.gorylenko.gradle-git-properties") version "2.4.1"
}

group = "br.com.rfasioli"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

val flywayVersion = "8.5.12"
val springDocVersion = "1.6.9"
val h2Version = "2.1.214"
val kotlinLoggingVersion = "2.1.23"
val springCloudVersion = "2021.0.3"
val jwtVersion = "0.9.1"
val mockkVersion = "1.12.8"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.cloud:spring-cloud-starter")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")

    implementation("org.springframework.cloud:spring-cloud-starter")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")

    implementation("org.springdoc:springdoc-openapi-ui:$springDocVersion")
    implementation("org.springdoc:springdoc-openapi-kotlin:$springDocVersion")
    implementation("org.flywaydb:flyway-core:$flywayVersion")
    implementation("org.flywaydb:flyway-mysql:$flywayVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingVersion")
    implementation("io.jsonwebtoken:jjwt:$jwtVersion")

    runtimeOnly("mysql:mysql-connector-java")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.h2database:h2:$h2Version")
    testImplementation("io.mockk:mockk:$mockkVersion")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}
noArg {
    annotation("com.lindroid.projectname.annotation.NoArg")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}
noArg {
    annotation("com.lindroid.projectname.annotation.NoArg")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    val javaToolchains = project.extensions.getByType<JavaToolchainService>()
    javaLauncher.set(
        javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    )
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

detekt {
    config = files("$projectDir/config/detekt/style-config.yml")
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
    }
}
