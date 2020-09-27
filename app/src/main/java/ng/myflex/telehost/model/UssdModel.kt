package ng.myflex.telehost.model

data class UssdModel(
    val id: Double,
    val user_id: Double,
    val ref_code: String,
    val code_dialed: String,
    val access_code: String,
    val created_at: String
)