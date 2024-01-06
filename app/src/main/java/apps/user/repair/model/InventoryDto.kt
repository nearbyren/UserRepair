package apps.user.repair.model

data class InventoryDto(
    val msg: String? = null,
    val repairAddress: String,
    val describes: String,
    val state: Int,
    val addressImage: String,
    val urgentTime: String,
    val urgent: Int,
)
