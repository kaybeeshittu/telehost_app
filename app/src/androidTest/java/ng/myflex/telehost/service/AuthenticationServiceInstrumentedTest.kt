package ng.myflex.telehost.service

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.Component
import dagger.android.AndroidInjector
import ng.myflex.telehost.common.scope.TestScope
import ng.myflex.telehost.config.Constant
import ng.myflex.telehost.config.rule.ComponentRule
import ng.myflex.telehost.config.rule.CoroutineRule
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class AuthenticationServiceInstrumentedTest {

    companion object {
        const val email = "Arowosegbe67@gmail.com"
        const val password = "_Arowosegbe1"
    }

    @get:Rule
    val componentRule: ComponentRule = ComponentRule()

    @get:Rule
    val coroutineRule: CoroutineRule = CoroutineRule()

    @Inject
    lateinit var sessionStorageService: SessionStorageService

    @Inject
    lateinit var principalService: PrincipalService

    @Inject
    lateinit var authenticationService: AuthenticationService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        DaggerAuthenticationServiceInstrumentedTest_AuthenticationServiceInstrumentedTestComponent
            .builder()
            .componentRuleTestComponent(componentRule.component)
            .build()
            .inject(this)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testAuthenticate() = coroutineRule.runBlockingTest {
        authenticationService.login(email, password)

        val token = sessionStorageService.getString(Constant.authSession)

        Assert.assertNotEquals(token, null)
        Assert.assertEquals(principalService.isAuthenticated(), true)
    }

    @TestScope
    @Component(dependencies = [ComponentRule.ComponentRuleTestComponent::class])
    interface AuthenticationServiceInstrumentedTestComponent :
        AndroidInjector<AuthenticationServiceInstrumentedTest>
}