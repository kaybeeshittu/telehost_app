package ng.myflex.telehost.service

import ng.myflex.telehost.adapter.SmsAdapter
import ng.myflex.telehost.domain.Activity
import ng.myflex.telehost.domain.Device
import ng.myflex.telehost.domain.enumeration.ActivityStatus
import ng.myflex.telehost.domain.enumeration.ActivityType
import ng.myflex.telehost.exception.SmsDeliveryException
import ng.myflex.telehost.payload.*
import ng.myflex.telehost.repository.ActivityRepository
import ng.myflex.telehost.resource.SmsResource
import ng.myflex.telehost.util.*
import java.util.*
import javax.inject.Inject

class SmsService @Inject constructor(
    private val smsResource: SmsResource,
    private val activityRepository: ActivityRepository,
    private val userDetailService: UserDetailService,
    private val smsAdapter: SmsAdapter
) {
    suspend fun scheduleMessage(
        recipients: List<String>,
        device: Device,
        message: String,
        date: String
    ): Boolean {
        val payload = PayloadUtil.getSmsPayload(recipients, device, message, date)
        val response = smsResource.schedule(userDetailService.getKey(), payload)

        return response.status.toLowerCase(Locale.US) == "success"
    }

    suspend fun getSmsCount(status: ActivityStatus = ActivityStatus.COMPLETED): Int {
        return activityRepository.sizeByTypeAndStatus(ActivityType.SMS, status)
    }

    suspend fun createActivities(
        payload: SmsPayload,
        simPort: Int,
        status: ActivityStatus = ActivityStatus.PENDING
    ): List<Activity> {
        val recipients = payload.phone_number.split(",".toRegex())
        val activities = recipients.map {
            payload.toActivity(
                simPort, it.trim(),
                null,
                if (payload.access_code.isEmpty()) {
                    ActivityStatus.COMPLETED
                } else status
            )
        }
        val iDs = activityRepository.saveAll(activities)

        return activityRepository.get(ActivityType.SMS, status, iDs.toLongArray())
    }

    suspend fun sendMessage(activity: Activity): Activity {
        return try {
            val sent = smsAdapter.sendMessage(activity)
            if (!sent) {
                throw SmsDeliveryException()
            }
            activity.copyWith(
                ActivityType.SMS,
                "SENT"
            ).update(activityRepository)
        } catch (exception: Exception) {
            activity.copyWith(
                ActivityType.SMS,
                exception.message
            ).update(activityRepository)
        }
    }

    /**
     * update the server with sent sms (activities of type sms and status ready)
     */
    suspend fun synchronizeSms(aId: LongArray): Boolean {
        val activities = activityRepository.get(ActivityType.SMS, ActivityStatus.READY, aId)
        if (activities.isEmpty()) {
            return true
        }
        val response = smsResource.updateAll(
            userDetailService.getKey(),
            activities.map {
                it.toStatusPayload("2")
            }.toList()
        )

        if (response.status.toLowerCase(Locale.US) == "success") {
            activityRepository.update(ActivityStatus.COMPLETED, activities.map {
                it.id!!
            }.toList())
            return true
        }
        return false
    }

    /**
     * update the server with sent sms (activities of type sms and status ready)
     */
    suspend fun backupSms(aId: LongArray): Boolean {
        val activities = activityRepository.get(ActivityType.SMS, ActivityStatus.RECEIVED, aId)

        val response = smsResource.backup(
            userDetailService.getKey(),
            activities.map {
                it.toSmsBackupPayload()
            }.toList()
        )

        if (response.status.toLowerCase(Locale.US) == "success") {
            activityRepository.update(ActivityStatus.COMPLETED, activities.map {
                it.id!!
            }.toList())
            return true
        }
        return false
    }

    suspend fun sendPendingMessages(aId: LongArray): Boolean {
        val processed = mutableListOf<Activity>()
        val activities = activityRepository.get(ActivityType.SMS, ActivityStatus.PENDING, aId)

        if (activities.isNotEmpty()) {
            for (activity in activities) {
                val sent = sendMessage(activity)
                if (sent.status == ActivityStatus.READY) {
                    processed.add(sent)
                }
            }
        }
        return processed.size == activities.size
    }
}