package ng.myflex.telehost.payload

data class MessagePayload(
    val id: Long,
    val userkey: String,
    val sim: String,
    val text: String?,
    val refCode: String,
    val phoneNumber: String,
    val type: String,
    val params: List<ParamPayload>?,
    val updatedAt: String
)