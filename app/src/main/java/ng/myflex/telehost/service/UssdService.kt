package ng.myflex.telehost.service

import ng.myflex.telehost.adapter.UssdAdapter
import ng.myflex.telehost.domain.Activity
import ng.myflex.telehost.domain.Device
import ng.myflex.telehost.domain.enumeration.ActivityStatus
import ng.myflex.telehost.domain.enumeration.ActivityType
import ng.myflex.telehost.payload.*
import ng.myflex.telehost.repository.ActivityRepository
import ng.myflex.telehost.resource.UssdResource
import ng.myflex.telehost.util.*
import java.util.*
import javax.inject.Inject

class UssdService @Inject constructor(
    private val ussdResource: UssdResource,
    private val activityRepository: ActivityRepository,
    private val userDetailService: UserDetailService,
    private val ussdAdapter: UssdAdapter
) {
    suspend fun scheduleUssd(
        code: String,
        device: Device,
        date: String
    ): Boolean {
        val payload = PayloadUtil.getUssdPayload(code, device, date)
        val response = ussdResource.schedule(userDetailService.getKey(), payload)

        return response.status.toLowerCase(Locale.US) == "success"
    }

    suspend fun getUssdCount(status: ActivityStatus = ActivityStatus.COMPLETED): Int {
        return activityRepository.sizeByTypeAndStatus(ActivityType.USSD, status)
    }

    suspend fun createActivity(payload: UssdPayload, simPort: Int): Activity {
        val activity = payload.toActivity(
            simPort,
            null,
            if (payload.access_code.isEmpty()) {
                ActivityStatus.COMPLETED
            } else ActivityStatus.PENDING
        )
        val iDs = activityRepository.save(activity)

        return iDs.withActivity(activity)
    }

    suspend fun dialUssd(activity: Activity): Activity {
        return try {
            val message = ussdAdapter.dialUssd(activity)
            activity.copyWith(
                ActivityType.USSD,
                message
            ).update(activityRepository)
        } catch (exception: Exception) {
            activity.copyWith(
                ActivityType.USSD,
                exception.message
            ).update(activityRepository)
        }
    }

    suspend fun dialUssd(id: Long): Activity? {
        val activity = activityRepository.get(
            ActivityType.USSD,
            ActivityStatus.PENDING,
            id
        ) ?: return null

        return dialUssd(activity)
    }

    /**
     * update the server with successful ussd (activities of type ussd and status ready)
     */
    suspend fun synchronizeUssd(id: Long): Boolean {
        val activities = activityRepository.get(
            ActivityType.USSD,
            ActivityStatus.READY,
            longArrayOf(id)
        )
        val response = ussdResource.updateAll(
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
}