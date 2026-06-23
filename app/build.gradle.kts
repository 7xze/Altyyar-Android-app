import app.cash.licensee.SpdxId
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import javax.inject.Inject

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.licensee)
}

abstract class GitShaValueSource : ValueSource<String, ValueSourceParameters.None> {
    @get:Inject
    abstract val execOperations: ExecOperations

    override fun obtain(): String {
        return try {
            val out = ByteArrayOutputStream()
            execOperations.exec {
                commandLine("git", "rev-parse", "--short=8", "HEAD")
                standardOutput = out
            }
            out.toString().trim()
        } catch (_: Exception) {
            "unknown"
        }
    }
}

val gitSha = providers.of(GitShaValueSource::class) {}.get()
val APP_NAME = "تيار"
val APP_ID = "com.altayyar.app"
val CUSTOM_LOGO_URL = ""
val CUSTOM_INSTANCE = ""
val SUPPORT_ACCOUNT_URL = ""

android {
    namespace = "com.altayyar.app"
    compileSdk = 35

    defaultConfig {
        applicationId = APP_ID
        minSdk = 24
        targetSdk = 35
        versionCode = 133
        versionName = "29.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        resValue("string", "app_name", APP_NAME)
        buildConfigField("String", "CUSTOM_LOGO_URL", "\"$CUSTOM_LOGO_URL\"")
        buildConfigField("String", "CUSTOM_INSTANCE", "\"$CUSTOM_INSTANCE\"")
        buildConfigField("String", "SUPPORT_ACCOUNT_URL", "\"$SUPPORT_ACCOUNT_URL\"")
        buildConfigField("String", "GIT_SHA", "\"$gitSha\"")
    }

    buildTypes {
        debug {
            isDefault = true
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles("proguard-rules.pro")
            kotlinOptions {
                freeCompilerArgs = listOf(
                    "-Xno-param-assertions",
                    "-Xno-call-assertions",
                    "-Xno-receiver-assertions"
                )
            }
        }
    }

    flavorDimensions += "color"
    productFlavors {
        register("blue")
        register("green") {
            resValue("string", "app_name", APP_NAME + " Test")
            applicationIdSuffix = ".test"
            versionNameSuffix = "-dev"
            isDefault = true
        }
    }

    lint {
        lintConfig = file("lint.xml")
        baseline = file("lint-baseline.xml")
    }

    buildFeatures {
        buildConfig = true
        resValues = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        getByName("debug") {
            assets {
                srcDirs(layout.projectDirectory.dir("schemas").asFile)
            }
        }
    }

    packaging {
        resources.excludes += listOf(
            "LICENSE_OFL",
            "LICENSE_UNICODE",
            "META-INF/androidx/**",
            "META-INF/NOTICE.md",
            "DebugProbesKt.bin"
        )
    }

    bundle {
        language {
            enableSplit = false
        }
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

val outputDir = file("build/outputs/apk")
val targetDir = file("/storage/internal/AlTayyar-apk")

tasks.register("copyApk") {
    dependsOn("assembleDebug")
    doFirst {
        targetDir.mkdirs()
    }
    doLast {
        outputDir.mkdirs() // ensure parent
        fileTree(outputDir).matching { include("**/*.apk") }.forEach { apk ->
            copy {
                from(apk)
                into(targetDir)
                rename { "AlTayyar-${apk.name}" }
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    systemProperty("robolectric.logging.enabled", "true")
    systemProperty("robolectric.lazyload", "ON")
}

ksp {
    arg("room.schemaLocation", layout.projectDirectory.dir("schemas").asFile.path)
    arg("room.generateKotlin", "true")
    arg("room.incremental", "true")
}

licensee {
    allow(SpdxId.Apache_20)
    allow(SpdxId.MIT)
    allowUrl("https://github.com/AOMediaCodec/libavif/blob/master/LICENSE")
    allowUrl("https://www.bouncycastle.org/licence.html")
}

configurations.configureEach {
    if (name.startsWith("test")) {
        exclude(group = "org.conscrypt", module = "conscrypt-android")
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.bundles.androidx)
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    implementation(libs.android.material)
    implementation(libs.bundles.moshi)
    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.bundles.retrofit)
    implementation(libs.networkresult.calladapter)
    implementation(libs.bundles.okhttp)
    implementation(libs.okio)
    implementation(libs.conscrypt.android)
    implementation(libs.bundles.glide)
    ksp(libs.glide.compiler)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.sparkbutton)
    implementation(libs.touchimageview)
    implementation(libs.bundles.material.drawer)
    implementation(libs.image.cropper)
    implementation(libs.bundles.filemojicompat)
    implementation(libs.bouncycastle)
    implementation(libs.unified.push)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.auth)
    implementation(libs.bundles.xmldiff)
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.work.testing)
    testImplementation(libs.turbine)
}
