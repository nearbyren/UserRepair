package apps.user.repair.uitl

object ApiHttpUrl {
    //用戶登錄
    const val login = "user/user/login"
    //用戶註冊
    const val enroll = "user/user/enroll"
    //用戶退出
    const val logout = "user/user/logout"
    //維修請求
    const val submit = "user/order/submit"
    //服务清单列表
    const val inventory = "user/inventory"
    //根据订单id查询详情
    const val inventory_id = "user/inventory/"
    //确认请求
    const val confirm = "user/inventory/confirm"
    //客户中心
    const val note = "user/note/note"
    //根据用户id查询邮箱
    const val mail_id = "user/mail/"
    //根据用户id修改邮箱
    const val updateEmail = "user/updateEmail"
    //修改密码
    const val revisePassword_id = "user/revisePassword/"
    //文件上传
    const val files_upload = "/files/upload"
    //图片下载
    const val files_report = "/files/report/{Image_id}"
}
