package composing.builds

/**
 * @author:
 * @created on: 2022/3/4 13:36
 * @description:
 */
object AndroidX {
    const val exifinterface = "androidx.exifinterface:exifinterface:1.3.2"
    const val databindingRuntime = "androidx.databinding:databinding-runtime:4.2.2"
    const val percentlayout = "androidx.percentlayout:percentlayout:1.0.0"
    const val annotation = "androidx.annotation:annotation:1.0.0"
    const val appcompat = "androidx.appcompat:appcompat:1.3.1"
    const val core = "androidx.core:core:1.0.0"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"
    const val coreKtx = "androidx.core:core-ktx:1.6.0"
    const val supportV13 = "androidx.legacy:legacy-support-v13:1.0.0"
    const val activityKtx = "androidx.activity:activity-ktx:1.4.0"
    const val cardview = "androidx.cardview:cardview:1.0.0"
    const val multidex = "androidx.multidex:multidex:2.0.1"
    const val legacy = "androidx.legacy:legacy-support-v4:1.0.0"
    const val paging = "androidx.paging:paging-runtime-ktx:3.0.1"
    const val viewpager = "androidx.viewpager:viewpager:1.0.0"
    const val splashscreen = "androidx.core:core-splashscreen:1.0.1"


    val fragment = Fragment

    object Fragment {
        private const val fragment_version = "1.3.6"
        const val fragment = "androidx.fragment:fragment:$fragment_version"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:$fragment_version"
        const val fragmentTesting = "androidx.fragment:fragment-testing:${fragment_version}"

    }

    val lifecyle = Lifecycle

    object Lifecycle {
        private const val lifecycle_version = "2.2.0"
        private const val lifecycle_version2 = "2.6.1"
        const val lcyExt = "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
        const val lifecycleRuntimeKtx =
            "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
        const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
        const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"

        const val viewModelSavedState =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
        const val service = "androidx.lifecycle:lifecycle-service:$lifecycle_version"

    }


    val constraintlayout = Constraintlayout

    object Constraintlayout {
        private const val constraintlayout_version = "2.1.4"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:$constraintlayout_version"
        const val constraintLayoutSolver =
            "androidx.constraintlayout:constraintlayout-solver:$constraintlayout_version"

    }


    val navigation = Navigation

    object Navigation {
        private const val navigation_version = "2.4.2"
        const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$navigation_version"
        const val uiKtx = "androidx.navigation:navigation-ui-ktx:$navigation_version"

        //GlobalScope（不推荐）
        const val kotCorCor = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0"
        const val kotCorAnd = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0"
        const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:1.6.10"
    }


    val room = Room

    object Room {
        private const val room_version = "2.3.0"
        const val roomRuntime = "androidx.room:room-runtime:$room_version"
        const val roomCompiler = "androidx.room:room-compiler:$room_version"
        const val roomKtx = "androidx.room:room-ktx:$room_version"
    }


    val camera = Camera

    object Camera {
        private const val camera_version = "1.0.0"
        const val cameraCore = "androidx.camera:camera-core:$camera_version"
        const val cameraCamera2 = "androidx.camera:camera-camera2:$camera_version"
        const val cameraLifecycle = "androidx.camera:camera-lifecycle:$camera_version"
        const val cameraView = "androidx.camera:camera-view:$camera_version"
        const val cameraExtensions = "androidx.camera:camera-extensions:$camera_version"
    }
}