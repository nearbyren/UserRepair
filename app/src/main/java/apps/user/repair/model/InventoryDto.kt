package apps.user.repair.model

data class InventoryDto(
    val msg: String? = null,
    //需要维修的地址
    val memberName: String? = null,
    //需要维修的地址
    val repairAddress: String? = null,
    //描述详情
    val describes: String? = null,
    val state: Int,
    //维修位置图片
    val addressImage: String? = null,
    //报价图片
    val quotationImage: String? = null,
    //维修完成后
    val completeImage: MutableList<String>,
    //发票
    val invoiceImage: String? = null,
    //紧急时间
    val urgentTime: String? = null,
    //是否紧急：0:是，1:否
    val urgent: Int = 0,
)
