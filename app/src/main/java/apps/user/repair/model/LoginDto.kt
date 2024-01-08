package apps.user.repair.model

data class LoginDto(
    var id: Int = 1,
    var email: String? = null,
    var name: String? = null,
    var shoolName: String? = null,
    var token: String? = null,
    var message: String? = null
)