package nearby.lib.router.component

/**
 * @author:
 * @created on: 2022/9/9 16:47
 * @description: 各组件之间回传的数据获取
 */
data class ComponentResultJson(var rawResult: String, var code: Int = 0, var action: Int = 0,var msg: String? = null, var json: String? = null) {
    init {
        rawResult.let {
            val results = rawResult.split(";")
            for (resultParam in results) {
                if (resultParam.startsWith("code")) {
                    code = gatValue(resultParam, "code")?.toInt() ?: 0
                }
                if (resultParam.startsWith("action")) {
                    action = gatValue(resultParam, "action")?.toInt() ?: 0
                }
                if (resultParam.startsWith("msg")) {
                    msg = gatValue(resultParam, "msg")
                }
                if (resultParam.startsWith("json")) {
                    json = gatValue(resultParam, "json")
                }
            }
        }
    }

    private fun gatValue(content: String, key: String): String? {
        //code={6001};memo={支付未完成};json={}
        val prefix = "$key={"
        return content.substring(
            content.indexOf(prefix) + prefix.length,
            content.lastIndexOf('}')
        )
    }
}
