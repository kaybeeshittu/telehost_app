package ng.myflex.telehost.payload

data class SmsBackupPayload(
    val sender: String,
    val access_code: String,
    val message: String
)