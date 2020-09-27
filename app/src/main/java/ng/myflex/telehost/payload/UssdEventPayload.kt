package ng.myflex.telehost.payload

data class UssdEventPayload(
    val ref_code: String,
    val ussd_code: String,
    val message: String
)