package ng.myflex.telehost.service

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.Component
import dagger.android.AndroidInjector
import ng.myflex.telehost.common.scope.TestScope
import ng.myflex.telehost.config.rule.ComponentRule
import ng.myflex.telehost.config.rule.CoroutineRule
import ng.myflex.telehost.payload.UserPayload
import ng.myflex.telehost.resource.AccountResource
import ng.myflex.telehost.util.AccountUtil
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*
import javax.inject.Inject
import kotlin.math.absoluteValue

@RunWith(AndroidJUnit4::class)
class AccountServiceInstrumentedTest {

    @get:Rule
    val componentRule: ComponentRule =
        ComponentRule()

    @get:Rule
    val coroutineRule: CoroutineRule =
        CoroutineRule()

    @Inject
    lateinit var accountService: AccountService

    @Mock
    lateinit var accountResource: AccountResource

    private lateinit var mockAccountService: AccountService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        DaggerAccountServiceInstrumentedTest_AccountServiceInstrumentedTestComponent
            .builder()
            .componentRuleTestComponent(componentRule.component)
            .build()
            .inject(this)

        mockAccountService = AccountService(accountResource)
    }

    @Test
    fun testGetAccount() = coroutineRule.runBlockingTest {
        AccountUtil.mockAccount(accountResource)

        val account = mockAccountService.get()

        verify(accountResource).getProfile()

        Assert.assertEquals(account.user.email, AccountUtil.email)
        Assert.assertEquals(account.user.firstname, AccountUtil.firstName)
        Assert.assertEquals(account.user.lastname, AccountUtil.lastName)
        Assert.assertEquals(account.user.country, AccountUtil.country)
    }

    @Test
    fun testRegistration() = coroutineRule.runBlockingTest {
        val payload = UserPayload(
            AccountUtil.firstName,
            AccountUtil.lastName,
            "mail${Random().nextInt().absoluteValue}@gmail.com",
            "_Arowosegbe1",
            "07016762847",
            "Nigeria"
        )
        val account = accountService.createAccount(payload)

        Assert.assertEquals(account.email, payload.email)
        Assert.assertEquals(account.firstName, payload.firstname)
        Assert.assertEquals(account.lastName, payload.lastname)
        Assert.assertEquals(account.country, payload.country)
    }

    @After
    fun tearDown() {
    }

    @TestScope
    @Component(dependencies = [ComponentRule.ComponentRuleTestComponent::class])
    interface AccountServiceInstrumentedTestComponent :
        AndroidInjector<AccountServiceInstrumentedTest>
}