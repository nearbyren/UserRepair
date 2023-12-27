package nearby.lib.base.network

/**
 * @Author: let
 * @date: 2022/11/15 17:30
 * @description: 网络连接状态的枚举
 */
enum class NetworkStatus(val status: Int, val desc: String) {
    /**
     * ；
     */
    NONE(-1, "无网络连接"),

    /**
     * 解析数据内容失败
     */
    MOBILE(0, "移动网络连接"),

    /**
     * 网络问题
     */
    WIFI(1, "WIFI连接");

    override fun toString(): String {
        return "NetwordStatus{" +
                "status=" + status +
                ", desc='" + desc + '\'' +
                "} " + super.toString()
    }
}