package ng.myflex.telehost.model

data class AccountModel(
    val user: ProfileModel,
    val wallet: WalletModel,
    val devices: List<DeviceModel>,
    val sentSms: Double,
    val receivedSms: Double,
    val ussdCount: Double
)
