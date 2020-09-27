package ng.myflex.telehost.util

import ng.myflex.telehost.domain.Activity
import ng.myflex.telehost.domain.enumeration.ActivityStatus
import ng.myflex.telehost.domain.enumeration.ActivityType
import ng.myflex.telehost.repository.ActivityRepository

fun Long.withActivity(activity: Activity): Activity = Activity(
    this,
    activity.number,
    activity.message,
    activity.response,
    activity.accessCode,
    activity.refCode,
    activity.simPort,
    activity.type,
    activity.param,
    activity.status,
    activity.stamp
)

fun Activity.copyWith(
    activityType: ActivityType,
    activityResponse: String?,
    activityStatus: ActivityStatus = ActivityStatus.READY
): Activity {
    return Activity(
        id,
        number,
        message,
        activityResponse,
        accessCode,
        refCode,
        simPort,
        activityType,
        param,
        activityStatus,
        stamp
    )
}

suspend fun Activity.update(activityRepository: ActivityRepository): Activity {
    return activityRepository.update(this).withActivity(this)
}