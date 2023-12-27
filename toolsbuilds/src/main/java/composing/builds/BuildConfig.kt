package composing.builds

/**
 * @author:
 * @created on: 2022/3/4 13:39
 * @description:
 */
object BuildConfig {

    //时间：2017.2.13；每次修改版本号都要添加修改时间
    //V1_1_2_161209_beta
    //V主版本号_子版本号_阶段版本号_日期版本号_希腊字母版本号

    /**
     * 程序编译app时候用的sdk版本 建议最新
     */
    const val compileSdk = 33

    /**
     * 程序运行的最低的要求的Sdk   [使用cameraX时记得切换成这个 21]
     */
    const val minSdk = 21

    /**
     * 向前兼容的作用
     */
    const val targetSdk = 32

    const val versionCode = 1

    const val versionName = "1.0"
}