package ng.myflex.telehost.payload

data class ActivationPayload(
    val device_token: String?,
    val device_name: String,
    val duration: String,
    val sim_port: String
)