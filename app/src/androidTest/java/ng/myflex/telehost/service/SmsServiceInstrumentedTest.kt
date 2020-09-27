package ng.myflex.telehost.service

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.Component
import dagger.android.AndroidInjector
import ng.myflex.telehost.adapter.SmsAdapter
import ng.myflex.telehost.common.scope.TestScope
import ng.myflex.telehost.config.rule.AuthenticationRule
import ng.myflex.telehost.config.rule.ComponentRule
import ng.myflex.telehost.config.rule.CoroutineRule
import ng.myflex.telehost.domain.Activity
import ng.myflex.telehost.domain.Device
import ng.myflex.telehost.domain.enumeration.ActivityStatus
import ng.myflex.telehost.repository.ActivityRepository
import ng.myflex.telehost.repository.DeviceRepository
import ng.myflex.telehost.resource.SmsResource
import ng.myflex.telehost.util.PayloadUtil
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class SmsServiceInstrumentedTest {
    companion object {
        const val recipient = "08172690317"
        const val messageBody = "AAAAAAAAAA"
    }

    @get:Rule
    val authenticationRule: AuthenticationRule = AuthenticationRule()

    @get:Rule
    val coroutineRule: CoroutineRule = CoroutineRule()

    @Inject
    lateinit var smsResource: SmsResource

    @Inject
    lateinit var deviceRepository: DeviceRepository

    @Inject
    lateinit var activityRepository: ActivityRepository

    @Inject
    lateinit var userDetailService: UserDetailService

    @Inject
    lateinit var sessionStorageService: SessionStorageService

    @Mock
    lateinit var smsAdapter: SmsAdapter

    private lateinit var smsService: SmsService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        DaggerSmsServiceInstrumentedTest_SmsServiceInstrumentedTestComponent
            .builder()
            .componentRuleTestComponent(authenticationRule.component)
            .build()
            .inject(this)

        smsService = SmsService(
            smsResource,
            activityRepository,
            userDetailService,
            smsAdapter
        )
    }

    private suspend fun createActivities(
        recipients: List<String>,
        message: String,
        status: ActivityStatus = ActivityStatus.PENDING
    ): List<Activity> {
        val port = 1
        val payload = PayloadUtil.getSmsPayload(recipients, getDevice(), message, Date().toString())

        return smsService.createActivities(payload, port, status)
    }

    private suspend fun getDevice(): Device {
        val devices = deviceRepository.getAll()

        return devices[devices.size - 1]
    }

    @Test
    fun testScheduleSms() = coroutineRule.runBlockingTest {
        authenticationRule.authenticate()

        val isScheduled = smsService.scheduleMessage(
            arrayListOf(recipient),
            getDevice(),
            messageBody,
            Date().toString()
        )
        Assert.assertEquals(isScheduled, true)
    }

    @Test
    fun testBackupSms() = coroutineRule.runBlockingTest {
        authenticationRule.authenticate()

        val activities =
            createActivities(arrayListOf(recipient), messageBody, ActivityStatus.RECEIVED)

        val isScheduled = smsService.backupSms(activities.map { it.id!! }.toLongArray())

        Assert.assertEquals(isScheduled, true)
    }

    @Test
    fun testCreateActivities() = coroutineRule.runBlockingTest {
        authenticationRule.authenticate()

        val recipients = arrayListOf(recipient)
        val activities = createActivities(arrayListOf(recipient), messageBody)

        Assert.assertEquals(recipients.size, activities.size)
    }

    @Test
    fun testSendSms() = coroutineRule.runBlockingTest {
        authenticationRule.authenticate()

        val databaseSizeBefore = activityRepository.size()
        val activities = createActivities(arrayListOf(recipient), messageBody)

        Mockito.`when`(smsAdapter.sendMessage(activities[0])).then { true }

        val activity = smsService.sendMessage(activities[0])

        Assert.assertEquals(activity.status, ActivityStatus.READY)
        Assert.assertEquals(activityRepository.size(), databaseSizeBefore + 1)
    }

    @Test
    fun testSendSmsError() = coroutineRule.runBlockingTest {
        authenticationRule.authenticate()

        val databaseSizeBefore = activityRepository.size()
        val activities = createActivities(arrayListOf(recipient), messageBody)

        Mockito.`when`(smsAdapter.sendMessage(activities[0])).thenThrow()

        val activity = smsService.sendMessage(activities[0])

        Assert.assertEquals(activity.status, ActivityStatus.READY)
        Assert.assertEquals(activityRepository.size(), databaseSizeBefore + 1)
    }

    @After
    fun tearDown() {
    }

    @TestScope
    @Component(dependencies = [ComponentRule.ComponentRuleTestComponent::class])
    interface SmsServiceInstrumentedTestComponent :
        AndroidInjector<SmsServiceInstrumentedTest>
}