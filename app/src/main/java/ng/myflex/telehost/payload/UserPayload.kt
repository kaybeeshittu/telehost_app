package ng.myflex.telehost.payload

data class UserPayload(
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val phone_num: String,
    val country: String
)