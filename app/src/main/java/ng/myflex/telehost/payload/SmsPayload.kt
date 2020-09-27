package ng.myflex.telehost.payload

data class SmsPayload(
    val text: String,
    val access_code: String,
    val ref_code: String,
    val phone_number: String,
    val stamp: String
)