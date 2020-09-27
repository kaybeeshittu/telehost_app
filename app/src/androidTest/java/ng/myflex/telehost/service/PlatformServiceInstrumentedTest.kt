package ng.myflex.telehost.service

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.Component
import dagger.android.AndroidInjector
import ng.myflex.telehost.common.Properties
import ng.myflex.telehost.common.scope.TestScope
import ng.myflex.telehost.config.rule.AuthenticationRule
import ng.myflex.telehost.config.rule.ComponentRule
import ng.myflex.telehost.config.rule.CoroutineRule
import ng.myflex.telehost.repository.ActivityRepository
import ng.myflex.telehost.repository.DeviceRepository
import ng.myflex.telehost.resource.DeviceResource
import ng.myflex.telehost.resource.SmsResource
import ng.myflex.telehost.resource.UssdResource
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class PlatformServiceInstrumentedTest {

    @get:Rule
    val authenticationRule: AuthenticationRule = AuthenticationRule()

    @get:Rule
    val coroutineRule: CoroutineRule = CoroutineRule()

    @Inject
    lateinit var properties: Properties

    @Inject
    lateinit var smsResource: SmsResource

    @Inject
    lateinit var ussdResource: UssdResource

    @Inject
    lateinit var deviceResource: DeviceResource

    @Inject
    lateinit var deviceRepository: DeviceRepository

    @Inject
    lateinit var activityRepository: ActivityRepository

    @Inject
    lateinit var userDetailService: UserDetailService

    @Inject
    lateinit var sessionStorageService: SessionStorageService

    lateinit var platformService: PlatformService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        DaggerPlatformServiceInstrumentedTest_PlatformServiceInstrumentedTestComponent
            .builder()
            .componentRuleTestComponent(authenticationRule.component)
            .build()
            .inject(this)

        platformService = PlatformService(
            deviceResource,
            deviceRepository,
            userDetailService,
            sessionStorageService
        )
    }

    @Test
    fun testPlatformService() {
        Assert.assertNotEquals(platformService, null)
    }

    @After
    fun tearDown() {
    }

    @TestScope
    @Component(dependencies = [ComponentRule.ComponentRuleTestComponent::class])
    interface PlatformServiceInstrumentedTestComponent :
        AndroidInjector<PlatformServiceInstrumentedTest>
}