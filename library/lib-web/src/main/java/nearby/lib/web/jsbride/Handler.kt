package nearby.lib.web.jsbride

interface Handler {
    fun handler(map: HashMap<String, Any>?, json: String, callback: Callback)
}