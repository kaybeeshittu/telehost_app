package ng.myflex.telehost.config.rule

import dagger.Component
import dagger.android.AndroidInjector
import ng.myflex.telehost.common.scope.TestScope
import ng.myflex.telehost.service.AuthenticationService
import ng.myflex.telehost.service.PrincipalService
import org.junit.runner.Description
import org.junit.runners.model.Statement
import javax.inject.Inject

class AuthenticationRule : ComponentRule() {
    companion object {
        const val email: String = "conxtantyn@gmail.com"
        const val password: String = "CodeSchool@2"
    }

    @Inject
    lateinit var authenticationService: AuthenticationService

    @Inject
    lateinit var principalService: PrincipalService

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                initialize()
                DaggerAuthenticationRule_AuthenticationRuleComponent
                    .builder()
                    .componentRuleTestComponent(component)
                    .build()
                    .inject(this@AuthenticationRule)

                base?.evaluate()
            }
        }
    }

    suspend fun authenticate() {
        authenticationService.login(email, password)
        principalService.identity(true)
    }

    @TestScope
    @Component(dependencies = [ComponentRuleTestComponent::class])
    interface AuthenticationRuleComponent :
        AndroidInjector<AuthenticationRule>
}