package nearby.lib.router.component

import com.google.gson.Gson
/**
 * @author:
 * @created on: 2022/9/9 16:47
 * @description: 各组件之间回传的数据格式封装
 */
object ComponentResultJsonUtil {

    fun toResultJson(code: Int = 0, action: Int = 0, msg: String? = null, json: Any? = null): String {
        var sb = StringBuffer()
        sb.append("code={$code};")
        sb.append("action={$action};")
        msg?.let {
            sb.append("msg={$it};")
        }
        json?.let {
            sb.append("json={${Gson().toJson(it)}};")
        }
        return sb.toString()
    }

}
