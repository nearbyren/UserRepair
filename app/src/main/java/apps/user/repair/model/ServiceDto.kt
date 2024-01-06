package apps.user.repair.model

/**
 * @author: lr
 * @created on: 2023/12/18 9:45 PM
 * @description:
 */
data class ServiceDto(
    var repairAddress: String,
    //详情地址
    var describes: String,
    //状态：0:未报价，1:已报价 2:已确认 3:未确认 4:未委派，5:已委派，6:未完成，7:已完成
    var state: Int,
    //订单号
    var numbers: String,
    //会员名称
    var memberName: String? = null,
    //序号
    var identifier: String? = null,
    //学校图片
    var addressImage: String? = null,
)
