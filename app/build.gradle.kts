
plugins {
    id("com.android.application")
    id("idea")
    id("com.apollographql.apollo3").version("3.0.0")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

val appId = "foo.bar.foreapollo3"

android {

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    compileSdk = 31

    defaultConfig {
        applicationId = appId
        minSdk = 16
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
    }
}

apollo {
    packageName.set(appId)
    srcDir("src/main/graphql")
}

// fetch the graphql schema:
//./gradlew downloadApolloSchema \
//  --endpoint="https://apollo-fullstack-tutorial.herokuapp.com/" \
//  --schema="app/src/main/graphql/schema.json"

// creating graphql queries:
// https://www.youtube.com/watch?v=omSpI1Nu_pg

dependencies {

    implementation("com.apollographql.apollo3:apollo-runtime:3.0.0")

    implementation("co.early.fore:fore-kt-android:1.5.12")
    implementation("io.coil-kt:coil:1.2.1")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
}
