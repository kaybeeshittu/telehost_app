package ng.myflex.telehost.service

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.Component
import dagger.android.AndroidInjector
import ng.myflex.telehost.adapter.UssdAdapter
import ng.myflex.telehost.common.scope.TestScope
import ng.myflex.telehost.config.rule.AuthenticationRule
import ng.myflex.telehost.config.rule.ComponentRule
import ng.myflex.telehost.config.rule.CoroutineRule
import ng.myflex.telehost.domain.Activity
import ng.myflex.telehost.domain.Device
import ng.myflex.telehost.domain.enumeration.ActivityStatus
import ng.myflex.telehost.repository.ActivityRepository
import ng.myflex.telehost.repository.DeviceRepository
import ng.myflex.telehost.resource.UssdResource
import ng.myflex.telehost.util.PayloadUtil
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class UssdServiceInstrumentedTest {
    companion object {
        const val ussdCode = "*232#"
    }

    @get:Rule
    val authenticationRule: AuthenticationRule = AuthenticationRule()

    @get:Rule
    val coroutineRule: CoroutineRule = CoroutineRule()

    @Inject
    lateinit var ussdResource: UssdResource

    @Inject
    lateinit var deviceRepository: DeviceRepository

    @Inject
    lateinit var activityRepository: ActivityRepository

    @Inject
    lateinit var userDetailService: UserDetailService

    @Inject
    lateinit var sessionStorageService: SessionStorageService

    @Mock
    lateinit var ussdAdapter: UssdAdapter

    lateinit var ussdService: UssdService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        DaggerUssdServiceInstrumentedTest_UssdServiceInstrumentedTestComponent
            .builder()
            .componentRuleTestComponent(authenticationRule.component)
            .build()
            .inject(this)

        ussdService = UssdService(
            ussdResource,
            activityRepository,
            userDetailService,
            ussdAdapter
        )
    }

    private suspend fun createActivity(code: String): Activity {
        val port = 1
        val payload = PayloadUtil.getUssdPayload(code, getDevice(), Date().toString())

        return ussdService.createActivity(payload, port)
    }

    private suspend fun getDevice(): Device {
        val devices = deviceRepository.getAll()

        return devices[devices.size - 1]
    }

    @Test
    fun testScheduleUssd() = coroutineRule.runBlockingTest {
        authenticationRule.authenticate()

        val isScheduled = ussdService.scheduleUssd(ussdCode, getDevice(), Date().toString())

        Assert.assertEquals(isScheduled, true)
    }

    @Test
    fun testCreateActivity() = coroutineRule.runBlockingTest {
        authenticationRule.authenticate()

        val newActivity = createActivity(ussdCode)

        Assert.assertNotEquals(newActivity.id, null)
    }

    @Test
    fun testDialUssd() = coroutineRule.runBlockingTest {
        authenticationRule.authenticate()

        val databaseSizeBefore = activityRepository.size()
        val newActivity = createActivity(ussdCode)
        val activity = ussdService.dialUssd(newActivity)

        Assert.assertEquals(activity.status, ActivityStatus.READY)
        Assert.assertEquals(activityRepository.size(), databaseSizeBefore + 1)
    }

    @Test
    fun testDialUssdError() = coroutineRule.runBlockingTest {
        authenticationRule.authenticate()

        val databaseSizeBefore = activityRepository.size()
        val newActivity = createActivity(ussdCode)

        Mockito.`when`(ussdAdapter.dialUssd(newActivity)).thenThrow()

        val activity = ussdService.dialUssd(newActivity)

        Assert.assertEquals(activity.status, ActivityStatus.READY)
        Assert.assertEquals(activityRepository.size(), databaseSizeBefore + 1)
    }

    @After
    fun tearDown() {
    }

    @TestScope
    @Component(dependencies = [ComponentRule.ComponentRuleTestComponent::class])
    interface UssdServiceInstrumentedTestComponent :
        AndroidInjector<UssdServiceInstrumentedTest>
}