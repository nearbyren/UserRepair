package apps.user.repair.uitl

object ApiHttpUrl {
    //用戶登錄
    const val login = "user/userLogin"
    //用戶註冊
    const val enroll = "user/userEnroll"
    //用戶退出
    const val logout = "user/logout"
    //維修請求
    const val submit = "user/order/submit"
    //根据订单号查询详情
    const val numbers = "user/numbers"
    //根据用户id查询订单列表
    const val inventory_id = "user/inventory/"
    //确认请求
    const val confirm = "user/confirm"
    //客户中心
    const val note = "user/note"
    //根据用户id查询邮箱
    const val mail_id = "user/mail/"
    //根据用户id修改邮箱
    const val updateEmail = "user/updateEmail"
    //修改密码
    const val revisePassword = "user/revisePassword"
    //文件上传
    const val upload = "/files/upload"
}
