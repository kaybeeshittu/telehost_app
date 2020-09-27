package ng.myflex.telehost.model

data class LoginModel(
    val status: String,
    val token: String,
    val user: LoginResponseModel
)