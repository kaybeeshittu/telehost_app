package ng.myflex.telehost.payload

data class SmsEventPayload(
    val ref_code: String,
    val message: String
)