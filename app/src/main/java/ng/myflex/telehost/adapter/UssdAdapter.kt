package ng.myflex.telehost.adapter

import ng.myflex.telehost.domain.Activity

interface UssdAdapter {
    suspend fun dialUssd(activity: Activity): String?
}