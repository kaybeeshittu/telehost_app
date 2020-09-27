package ng.myflex.telehost.payload

data class UssdPayload(
    val ussd_code: String,
    val access_code: String,
    val ref_code: String,
    val param: List<String>?,
    val stamp: String
)