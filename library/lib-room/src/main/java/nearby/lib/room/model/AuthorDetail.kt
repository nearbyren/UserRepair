package nearby.lib.room.model

import androidx.room.Ignore

data class AuthorDetail(
    val authorId: Long = 0L,
    val authorName: String = "",
    val joinTime: String = "",
    val updateTime: String = "") {
    @Ignore
    constructor() : this(0, "", "", "")
}

