package ng.myflex.telehost.adapter

import ng.myflex.telehost.domain.Activity

interface SmsAdapter {
    fun simCount(): Int
    suspend fun sendMessage(activity: Activity): Boolean
}