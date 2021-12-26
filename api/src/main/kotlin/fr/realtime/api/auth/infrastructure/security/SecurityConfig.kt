package fr.realtime.api.auth.infrastructure.security

import fr.realtime.api.auth.infrastructure.security.login.JwtAuthenticationFilter
import fr.realtime.api.auth.infrastructure.security.login.JwtAuthorizationFilter
import fr.realtime.api.shared.infrastructure.utils.PasswordUtilsImpl
import fr.realtime.api.user.core.UserDao
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val userDao: UserDao,
    private val tokenProvider: TokenProvider,
    private val unauthorizedHandler: AuthenticationEntryPoint
) : WebSecurityConfigurerAdapter() {
    private val logger = LoggerFactory.getLogger(this.javaClass)


    override fun configure(http: HttpSecurity?) {
        http
            ?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)?.and()

            ?.cors()?.and()
            ?.csrf()?.disable()
            ?.exceptionHandling()?.authenticationEntryPoint(unauthorizedHandler)?.and()
            ?.authorizeRequests()
            ?.antMatchers("/api/auth/register", "/api/login")?.permitAll()
            ?.antMatchers("/api/**")?.hasRole("USER")
            ?.antMatchers("/admin/**")?.hasRole("ADMIN")
            ?.anyRequest()?.authenticated()
            ?.and()
            ?.addFilter(
                JwtAuthenticationFilter(
                    authenticationManager(),
                    PasswordUtilsImpl(passwordEncoder()),
                    tokenProvider
                )
            )
            ?.addFilterBefore(JwtAuthorizationFilter(tokenProvider), UsernamePasswordAuthenticationFilter::class.java)
            ?: logger.error("Problem when configure http")
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService())?.passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun userDetailsService(): UserDetailsService {
        return UserDetailsServiceImpl(userDao)
    }
}