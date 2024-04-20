plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    `maven-publish`
    id("net.linguica.maven-settings") version "0.5"
}

android {
    namespace = "com.aria.danesh.sidemenu"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isDefault = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.util)


}
publishing {
    publications {
        register<MavenPublication>("ReleaseAar") {
            groupId = "com.aria.danesh"
            artifactId = "sidemenu"
            version = "1.0.0"
            pom {
                name = "SideMenuJetPackCompose"
                description = "A Simple SideMenu Bar Lib For Android JetPack"
                url = "https://github.com/arya458/SideMenuJetPackCompose"
                groupId = "com.aria.danesh"
                artifactId = "sidemenu"
                version = "1.0.0"
//                artifact("build/outputs/aar/app-release.aar")
                developers {
                    developer {
                        id = "arya458"
                        name = "Aria Danesh"
                    }
                }
            }
            afterEvaluate {
                artifact(tasks.getByName("bundleReleaseAar"))
            }
        }




//        create<MavenPublication>("release") {
//            from(components["aar"])
//            pom {
//                name = "SideMenuJetPackCompose"
//                description = "A Simple SideMenu Bar Lib For Android JetPack"
//                url = "https://github.com/arya458/SideMenuJetPackCompose"
//                groupId = "com.aria.danesh"
//                artifactId = "sidemenu"
//                version = "1.0.0"
//                artifact("build/outputs/aar/app-release.aar")
//                developers {
//                    developer {
//                        id = "arya458"
//                        name = "Aria Danesh"
//                    }
//                }
//            }
//        }
    }
}


