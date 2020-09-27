package ng.myflex.telehost.model

data class DashboardModel(
    val status: String,
    val user: String,
    val wallet: String,
    val transactions: Int,
    val devices: Int,
    val ussd: Int,
    val sms: Int
)