/*
 * Copyright 2022, TeamDev. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

@file:Suppress("RemoveRedundantQualifierName")

import com.google.protobuf.gradle.remove
import io.spine.internal.dependency.ErrorProne
import io.spine.internal.dependency.JUnit
import io.spine.internal.dependency.Kotest
import io.spine.internal.dependency.Spine
import io.spine.internal.dependency.Validation
import io.spine.internal.gradle.VersionWriter
import io.spine.internal.gradle.checkstyle.CheckStyleConfig
import io.spine.internal.gradle.github.pages.updateGitHubPages
import io.spine.internal.gradle.javac.configureErrorProne
import io.spine.internal.gradle.javac.configureJavac
import io.spine.internal.gradle.javadoc.JavadocConfig
import io.spine.internal.gradle.publish.IncrementGuard
import io.spine.internal.gradle.publish.PublishingRepos
import io.spine.internal.gradle.publish.PublishingRepos.gitHub
import io.spine.internal.gradle.publish.spinePublishing
import io.spine.internal.gradle.report.license.LicenseReporter
import io.spine.internal.gradle.report.pom.PomGenerator
import io.spine.internal.gradle.standardToSpineSdk
import io.spine.internal.gradle.testing.configureLogging
import io.spine.internal.gradle.testing.registerTestTasks
import org.gradle.api.file.DuplicatesStrategy.INCLUDE
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    standardSpineSdkRepositories()
    dependencies {
        classpath(io.spine.internal.dependency.Spine.McJava.pluginLib)
    }
}

plugins {
    `java-library`
    kotlin("jvm")
    idea
    protobuf
    errorprone
    pmd
    jacoco
    `project-report`
    `pmd-settings`
    `dokka-for-java`
    `detekt-code-analysis`
    `gradle-doctor`
}

apply(from = "$projectDir/version.gradle.kts")
val versionToPublish: String by extra

group = "io.spine"
version = versionToPublish

repositories {
    standardToSpineSdk()
}

apply {
    plugin(Spine.McJava.pluginId)
    plugin<IncrementGuard>()
    plugin<VersionWriter>()
}

configurations {
    forceVersions()
    excludeProtobufLite()

    all {
        resolutionStrategy {
            force(
                JUnit.runner,
                Spine.base,
                Spine.toolBase,
                Spine.Logging.lib,
                Spine.Logging.backend,
                Validation.runtime,
                Kotest.assertions
            )
        }
    }
}

dependencies {
    errorprone(ErrorProne.core)

    implementation(Spine.base)
    implementation(Validation.runtime)

    testImplementation(JUnit.runner)
    testImplementation(Spine.testlib)
    testImplementation(Kotest.assertions)
}

spinePublishing {
    destinations = setOf(
        gitHub("text"),
        PublishingRepos.cloudRepo,
        PublishingRepos.cloudArtifactRegistry
    )
    dokkaJar {
        java = true
        kotlin = true
    }
}

val javaVersion = JavaVersion.VERSION_11

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion

    tasks {
        withType<JavaCompile>().configureEach {
            configureJavac()
            configureErrorProne()
        }
    }
}

kotlin {
    explicitApi()

    tasks {
        withType<KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = javaVersion.toString()
                freeCompilerArgs = listOf("-Xskip-prerelease-check")
            }
        }
    }
}

protobuf {
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                remove("grpc")
            }
        }
    }
}

val javadocToolsVersion = Spine.ArtifactVersion.javadocTools
updateGitHubPages(javadocToolsVersion) {
    allowInternalJavadoc.set(true)
    rootFolder.set(rootDir)
}


tasks {
    registerTestTasks()
    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required.set(true)
        }
    }
    test {
        useJUnitPlatform()
        configureLogging()
        finalizedBy(jacocoTestReport)
    }
}

configureTaskDependencies()

project.afterEvaluate {
    val sourcesJar: Task by tasks.getting {
        (this as Jar).duplicatesStrategy = INCLUDE
    }
}

CheckStyleConfig.applyTo(project)
JavadocConfig.applyTo(project)
PomGenerator.applyTo(project)
LicenseReporter.generateReportIn(project)
LicenseReporter.mergeAllReports(project)
