package ng.myflex.telehost.payload

data class StatusPayload(
    val status: String,
    val ref_code: String,
    val message: String? = null
)