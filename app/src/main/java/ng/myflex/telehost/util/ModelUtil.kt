package ng.myflex.telehost.util

import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.domain.Device
import ng.myflex.telehost.model.AccountModel
import ng.myflex.telehost.model.FcmModel
import ng.myflex.telehost.model.LoginResponseModel
import ng.myflex.telehost.model.UserModel

fun Map<String, String>.toModel(): FcmModel = FcmModel(
    this["sim"] ?: "1",
    this["text"] ?: "",
    "*232#",// this["ussdString"],
    this["ref_code"],
    this["phone"],
    true // !this["ussdString"].isNullOrEmpty()
)

fun UserModel.toAccount(): Account = Account(
    null,
    user.id,
    user.api_key,
    user.firstname,
    user.lastname,
    user.email,
    user.phone_num,
    user.verified,
    user.payant_customer_id,
    user.country,
    0.0,
    ussdCount.toInt(),
    sentSms.toInt(),
    devices.toInt()
)

fun AccountModel.toAccount(): Account = Account(
    null,
    user.id,
    user.api_key,
    user.firstname,
    user.lastname,
    user.email,
    user.phone_num,
    user.verified,
    user.payant_customer_id,
    user.country,
    wallet.balance.toDouble(),
    ussdCount.toInt(),
    sentSms.toInt(),
    devices.size
)

fun AccountModel.toDevices(): List<Device> = devices.map {
    Device(
        null,
        it.id,
        it.device_token,
        it.device_name,
        it.duration,
        it.sim_port,
        it.access_code,
        it.status,
        it.subscribed_at,
        it.valid_sub
    )
}.toList()

fun LoginResponseModel.toAccount(): Account = Account(
    null,
    `0`.id,
    `0`.api_key,
    `0`.firstname,
    `0`.lastname,
    `0`.email,
    `0`.phone_num,
    `0`.verified,
    `0`.payant_customer_id,
    `0`.country,
    wallet_balance.toDouble(),
    0,
    0,
    0
)