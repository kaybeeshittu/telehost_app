package ng.myflex.telehost.service

import ng.myflex.telehost.config.Constant
import ng.myflex.telehost.payload.*
import ng.myflex.telehost.repository.DeviceRepository
import ng.myflex.telehost.resource.DeviceResource
import ng.myflex.telehost.util.md5
import ng.myflex.telehost.util.toDevices
import java.util.*
import javax.inject.Inject

class PlatformService @Inject constructor(
    private val deviceResource: DeviceResource,
    private val deviceRepository: DeviceRepository,
    private val userDetailService: UserDetailService,
    private val sessionStorageService: SessionStorageService
) {
    suspend fun activateSim(phone: String, duration: String, simPort: String): Boolean {
        val payload = ActivationPayload(
            sessionStorageService.getString(Constant.fcmSession)?.md5(),
            phone,
            duration,
            simPort
        )
        val model = deviceResource.activateDevice(userDetailService.getKey(), payload)

        return model.status.toLowerCase(Locale.US) == "success"
    }

    suspend fun getAccessCode(): String {
        val token = sessionStorageService.getString(Constant.fcmSession) ?: return ""
        var device = deviceRepository.get(token.md5())

        if (device == null) {
            val response = deviceResource.getDevices()

            deviceRepository.saveAll(response.toDevices())

            device = deviceRepository.get(token)
        }
        if (device != null) {
            return device.accessCode
        }
        return ""
    }

    suspend fun getDevices() = deviceRepository.getAll()
}