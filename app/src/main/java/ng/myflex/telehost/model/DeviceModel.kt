package ng.myflex.telehost.model

data class DeviceModel(
    val id: Long,
    val device_token: String,
    val device_name: String,
    val duration: String,
    val sim_port: String,
    val access_code: String,
    val status: Int,
    val subscribed_at: String,
    val valid_sub: Int
)