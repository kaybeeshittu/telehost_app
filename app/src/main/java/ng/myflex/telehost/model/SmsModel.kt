package ng.myflex.telehost.model

data class SmsModel(
    val id: Double,
    val user_id: Double,
    val ref_code: String,
    val access_code: String,
    val reciever: String,
    val message: String,
    val type: String,
    val status: Double,
    val created_at: String
)