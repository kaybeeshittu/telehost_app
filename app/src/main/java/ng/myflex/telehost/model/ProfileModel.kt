package ng.myflex.telehost.model

data class ProfileModel(
    val id: Long,
    val api_key: String,
    val webhook: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val phone_num: String,
    val country: String,
    val payant_customer_id: Double?,
    val verified: Int,
    val status: Int,
    val created_at: String,
    val updated_at: String
)
