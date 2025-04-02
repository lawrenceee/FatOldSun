plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp) // 添加 KSP 插件
}

android {
    namespace = "com.lj.fatoldsun.core"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testOptions.targetSdk = libs.versions.targetSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
        viewBinding = true // 启用 ViewBinding
        dataBinding = true // 启用 DataBinding
        buildConfig = true  // 显式启用 BuildConfig 生成（通常默认启用）
    }
    // 配置依赖的解析策略,防止冲突，在api implementation配置中使用23.0.0
    configurations.matching { it.name in listOf("api", "implementation") }.all {
        resolutionStrategy {
            force(libs.jetbrains.annotations)
        }
    }

    dependencies {
        // Jetpack 组件
        api(libs.androidx.appcompat)
        api(libs.androidx.core.ktx)

        // ViewModel 和 LiveData 依赖
        api(libs.androidx.lifecycle.viewmodel.ktx)
        api(libs.androidx.lifecycle.livedata.ktx)
        api(libs.jetbrains.annotations)

        //hilt
        implementation(libs.hilt.android)  // 这里是引用依赖的的gradle->lib.versions.toml
        ksp(libs.hilt.compiler)

        // Fragment 依赖（确保可以使用 viewModels()）
        api(libs.androidx.fragment.ktx)//

        // Retrofit 网络框架
        api(libs.retrofit)
        api(libs.retrofit.converter.gson)
        api(libs.okHttp)
        api(libs.okHttp.logging.interceptor) // 日志拦截器依赖
        api(libs.gson)

        //布局组件
        api(libs.androidx.recyclerview)
        api(libs.androidx.constraintlayout)

        //Material 组件库
        api(libs.material)

        // 协程
        api(libs.coroutines.core)
        api(libs.coroutines.android)

        // Glide 图片加载
        api("com.github.bumptech.glide:glide:4.16.0")

        // 状态栏适配
        api(libs.ultimateBarX)

        // 适配器
        api(libs.baseRecyclerViewAdapterHelper)

        // 屏幕适配
        api(libs.androidAutoSize)

        // 日志工具
        api(libs.timber)

        //banner
        api(libs.youth.banner)

        //paging
        api(libs.androidx.paging)
        api(libs.androidx.room.paging)

        // 单元测试依赖
        testImplementation(libs.junit)
    }


}
dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
}
