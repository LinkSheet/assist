@file:Suppress("UnstableApiUsage")

import fe.build.dependencies.Grrfe
import fe.build.dependencies.LinkSheet
import fe.build.dependencies._1fexd
import fe.buildsettings.config.GradlePluginPortalRepository
import fe.buildsettings.config.MavenRepository
import fe.buildsettings.config.configureRepositories

rootProject.name = "LinkSheetAssist"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }

    plugins {
        id("de.fayard.refreshVersions") version "0.60.6"
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
        id("com.android.library")
        id("org.jetbrains.kotlin.android")
        id("net.nemerosa.versioning")
        id("androidx.navigation.safeargs") version "2.8.2"
    }

    fun Any?.parsePlugins(): Map<String, String> {
        return toString()
            .trim()
            .split(",")
            .map { it.trim().split("=") }
            .filter { it.size == 2 }
            .associate { it[0] to it[1] }
    }

    val plugins: Any? = extra.properties["gradle.build.plugins"]
    when (val gradleBuildDir = extra.properties["gradle.build.dir"]) {
        null -> {
            val gradleBuildVersion = extra.properties["gradle.build.version"]
            val plugins = extra.properties["gradle.build.plugins"].parsePlugins()
            resolutionStrategy {
                eachPlugin {
                    plugins[requested.id.id]?.let { useModule("$it:$gradleBuildVersion") }
                }
            }
        }

        else -> includeBuild(gradleBuildDir.toString())
    }
}

plugins {
    id("de.fayard.refreshVersions")
    id("org.gradle.toolchains.foojay-resolver-convention")
    id("com.gitlab.grrfe.build-settings-plugin")
}

configureRepositories(
    MavenRepository.Google,
    MavenRepository.MavenCentral,
    MavenRepository.Jitpack,
    GradlePluginPortalRepository,
    mode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
)

extra.properties["gradle.build.dir"]
    ?.let { includeBuild(it.toString()) }

include(":app")

buildSettings {
    substitutes {
        trySubstitute(Grrfe.std, properties["kotlin-ext.dir"])
        trySubstitute(Grrfe.httpkt, properties["httpkt.dir"])
        trySubstitute(Grrfe.gsonExt, properties["gson-ext.dir"])
        trySubstitute(_1fexd.composeKit, properties["composekit.dir"])
        trySubstitute(LinkSheet.flavors, properties["linksheet.flavors.dir"])
    }
}
