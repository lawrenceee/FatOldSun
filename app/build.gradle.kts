plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp) // 添加 KSP 插件


}

android {
    namespace = "com.lj.fatoldsun.platform"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lj.fatoldsun.platform"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true // 显式设置 debug 模式为 debuggable
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        viewBinding = true
        buildConfig = true  // 显式启用 BuildConfig 生成（通常默认启用）
    }


}

dependencies {
    implementation(project(":core")) // 依赖 core 模块
    implementation(libs.hilt.android)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)  // 添加 Hilt 依赖
    ksp(libs.hilt.compiler)          // 添加 Hilt 注解处理器
// 添加 Room 相关依赖
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler) // 添加 Room 的 KSP 处理器
    // 单元测试依赖
    testImplementation(libs.junit)
    testImplementation(libs.mockito)

//    implementation(libs.androidx.appcompat)

//    implementation(libs.androidx.appcompat)
//
//    // ViewModel 和 LiveData 依赖
//    implementation(libs.androidx.lifecycle.viewmodel.ktx)
//    implementation(libs.androidx.lifecycle.livedata.ktx)
//
//    // Fragment 依赖（确保可以使用 viewModels()）
//    implementation(libs.androidx.fragment.ktx)
//
//    // 状态栏适配
//    implementation(libs.ultimateBarX)
//
//    // 屏幕适配
//    implementation(libs.androidAutoSize)
//
//    // 日志工具
//    implementation(libs.timber)
//
//    //网络请求
//    implementation(libs.retrofit)
//    implementation(libs.retrofit.converter.gson)
//    implementation(libs.okHttp)
//    implementation(libs.gson)
//
//
//    //Room
//    implementation(libs.room.runtime)
//    implementation(libs.room.ktx)
//    kapt(libs.room.compiler)


//    implementation("androidx.core:core-ktx:1.12.0")
//    implementation("com.google.android.material:material:1.11.0")
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.lifecycle.runtime.ktx)
//    implementation(libs.androidx.activity.compose)
//    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.androidx.ui)
//    implementation(libs.androidx.ui.graphics)
//    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)
}