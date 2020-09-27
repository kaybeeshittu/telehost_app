package ng.myflex.telehost.util

import ng.myflex.telehost.domain.Activity
import ng.myflex.telehost.domain.Device
import ng.myflex.telehost.domain.enumeration.ActivityStatus
import ng.myflex.telehost.domain.enumeration.ActivityType
import ng.myflex.telehost.payload.*
import java.util.*

class PayloadUtil {
    companion object {
        fun getReferenceCode(): String {
            return UUID.randomUUID().toString().subSequence(0, 8).toString()
        }

        fun getUssdPayload(
            code: String,
            device: Device,
            date: String
        ): UssdPayload {
            return UssdPayload(
                code,
                device.accessCode,
                getReferenceCode(),
                null,
                date.md5()
            )
        }

        fun getSmsPayload(
            recipients: List<String>,
            device: Device,
            message: String,
            date: String
        ): SmsPayload {
            val refCode = getReferenceCode()
            val numbers = recipients.joinToString(",")

            return SmsPayload(message, device.accessCode, refCode, numbers, date.md5())
        }
    }
}

fun SmsPayload.toActivity(
    simPort: Int,
    recipient: String,
    response: String?,
    status: ActivityStatus = ActivityStatus.READY
): Activity = Activity(
    null,
    recipient,
    text,
    response,
    access_code,
    ref_code,
    simPort,
    ActivityType.SMS,
    null,
    status,
    stamp
)

fun UssdPayload.toActivity(
    simPort: Int,
    response: String?,
    status: ActivityStatus = ActivityStatus.READY
): Activity {
    return Activity(
        null,
        ussd_code.trim(),
        "",
        response,
        access_code,
        ref_code,
        simPort,
        ActivityType.USSD,
        param,
        status,
        stamp
    )
}

fun Activity.toStatusPayload(status: String): StatusPayload = StatusPayload(
    status, refCode, response
)

fun Activity.toSmsBackupPayload(): SmsBackupPayload = SmsBackupPayload(
    number, accessCode!!, message
)