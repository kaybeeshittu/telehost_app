package ng.myflex.telehost.service

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.Component
import dagger.android.AndroidInjector
import ng.myflex.telehost.common.scope.TestScope
import ng.myflex.telehost.config.rule.ComponentRule
import ng.myflex.telehost.config.rule.CoroutineRule
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.resource.AccountResource
import ng.myflex.telehost.util.AccountUtil
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.Exception
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class PrincipalServiceInstrumentedTest {

    @get:Rule
    val componentRule: ComponentRule = ComponentRule()

    @get:Rule
    val coroutineRule: CoroutineRule = CoroutineRule()

    @Inject
    lateinit var userDetailService: UserDetailService

    @Inject
    lateinit var accountService: AccountService

    @Mock
    lateinit var accountResource: AccountResource

    private lateinit var mockAccountService: AccountService

    private lateinit var principalService: PrincipalService

    private lateinit var mockPrincipalService: PrincipalService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        DaggerPrincipalServiceInstrumentedTest_PrincipalServiceInstrumentedTestComponent
            .builder()
            .componentRuleTestComponent(componentRule.component)
            .build()
            .inject(this)

        principalService = PrincipalService(accountService, userDetailService)

        mockAccountService = AccountService(accountResource)
        mockPrincipalService = PrincipalService(
            mockAccountService,
            userDetailService
        )
    }

    @Test
    fun testSetAuthentication() = coroutineRule.runBlockingTest {
        AccountUtil.mockAccount(accountResource)

        mockPrincipalService.identity(true)

        assertEquals(mockPrincipalService.isAuthenticated(), true)
    }

    @Test
    fun testUnsetAuthentication() {
        mockPrincipalService.authenticate(null)

        assertEquals(mockPrincipalService.isAuthenticated(), false)
    }

    @Test
    fun testUnauthorized() = coroutineRule.runBlockingTest {
        try {
            principalService.identity(true)
        } catch (exception: Exception) {
        }
        assertEquals(mockPrincipalService.isAuthenticated(), false)
    }

    @Test
    fun testCachedUser() = coroutineRule.runBlockingTest {
        var account: Account? = null

        userDetailService.setAuthenticatedUser(AccountUtil.generateModel())

        Mockito.`when`(accountResource.getProfile()).thenThrow()
        try {
            account = mockPrincipalService.identity(true)
        } catch (exception: Exception) {
        }
        assertEquals(mockPrincipalService.isAuthenticated(), true)
        assertNotEquals(account, null)
    }

    @After
    fun tearDown() {
    }

    @TestScope
    @Component(dependencies = [ComponentRule.ComponentRuleTestComponent::class])
    interface PrincipalServiceInstrumentedTestComponent :
        AndroidInjector<PrincipalServiceInstrumentedTest>
}