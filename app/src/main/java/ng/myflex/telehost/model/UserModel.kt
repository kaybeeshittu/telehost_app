package ng.myflex.telehost.model

data class UserModel(
    val status: String,
    val sentSms: Double,
    val receivedSms: Double,
    val ussdCount: Double,
    val devices: Double,
    val user: ProfileModel
)
