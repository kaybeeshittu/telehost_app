package ng.myflex.telehost.model

data class FcmModel(
    val sim: String,
    val text: String,
    val ussdString: String?,
    val refCode: String?,
    val phoneNumber: String?,
    val isUssd: Boolean
)