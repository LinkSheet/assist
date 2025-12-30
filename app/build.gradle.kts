import com.gitlab.grrfe.gradlebuild.android.AndroidSdk
import com.gitlab.grrfe.gradlebuild.android.ArchiveBaseName
import com.gitlab.grrfe.gradlebuild.common.version.CurrentTagMode
import com.gitlab.grrfe.gradlebuild.common.version.TagReleaseParser
import com.gitlab.grrfe.gradlebuild.common.version.asProvider
import com.gitlab.grrfe.gradlebuild.common.version.closure
import fe.build.dependencies.Grrfe
import fe.build.dependencies.LinkSheet
import fe.build.dependencies._1fexd
import fe.buildlogic.Version
import fe.buildlogic.common.CompilerOption
import fe.buildlogic.common.extension.addCompilerOptions
import fe.buildlogic.extension.getOrSystemEnv
import fe.buildlogic.extension.readPropertiesOrNull
import fe.buildlogic.version.AndroidVersionStrategy

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("net.nemerosa.versioning")
    id("com.gitlab.grrfe.new-build-logic-plugin")
}

// Must be defined before the android block, or else it won't work
versioning {
    releaseMode = CurrentTagMode.closure
    releaseParser = TagReleaseParser.closure
}

var appName = "LinkSheet Assistant"

android {
    namespace = "app.linksheet.assist"
    compileSdk = AndroidSdk.COMPILE_SDK

    defaultConfig {
        applicationId = "app.linksheet.assist"
        minSdk = AndroidSdk.MIN_SDK
        targetSdk = AndroidSdk.COMPILE_SDK

        val now = System.currentTimeMillis()
        val provider = AndroidVersionStrategy(now)

        val versionProvider = versioning.asProvider(project, provider)
        val (name, code, commit, branch) = versionProvider.get()

        versionCode = code
        versionName = name

        with(ArchiveBaseName) { project.setArchivesBaseName(appName, name, now) }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testOptions.unitTests.isIncludeAndroidResources = true

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        register("env") {
            val properties = rootProject.file(".ignored/keystore.properties").readPropertiesOrNull()

            storeFile = properties.getOrSystemEnv("KEYSTORE_FILE_PATH")?.let { rootProject.file(it) }
            storePassword = properties.getOrSystemEnv("KEYSTORE_PASSWORD")
            keyAlias = properties.getOrSystemEnv("KEY_ALIAS")
            keyPassword = properties.getOrSystemEnv("KEY_PASSWORD")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"

            resValue("string", "app_name", "$appName Debug")
        }

        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }

        register("nightly") {
            initWith(buildTypes.getByName("release"))
            matchingFallbacks.add("release")
            signingConfig = signingConfigs.getByName("env")

            applicationIdSuffix = ".nightly"
            versionNameSuffix = "-nightly"

            resValue("string", "app_name", "$appName Nightly")
        }
    }

    kotlin {
        jvmToolchain(Version.JVM)
        addCompilerOptions(CompilerOption.SkipPreReleaseCheck, CompilerOption.NestedTypeAliases)
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += setOf("/META-INF/{AL2.0,LGPL2.1}", "META-INF/atomicfu.kotlin_module")
        }
    }
}

dependencies {
    implementation(platform(Grrfe.std.bom))
    implementation(platform(_1fexd.composeKit.bom))
    implementation(platform(LinkSheet.flavors.bom))
    implementation(AndroidX.core.ktx)
    implementation(Grrfe.std.result.core)
    implementation(_1fexd.composeKit.core)
    implementation(LinkSheet.flavors.core)
}
