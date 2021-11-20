
plugins {
    id("com.android.application")
    id("idea")
    id("com.apollographql.apollo3").version("3.0.0-beta03")
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

    compileSdk = 30

    defaultConfig {
        applicationId = appId
        minSdk = 16
        targetSdk = 30
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

    implementation("com.apollographql.apollo3:apollo-runtime:3.0.0-beta03")

    implementation("co.early.fore:fore-kt-android:1.5.4")
    implementation("io.coil-kt:coil:1.2.1")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.1")
}
