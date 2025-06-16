package org.example.googletask

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/", permitAll)
                authorize(anyRequest, authenticated)
            }

            oauth2Login {
                loginPage = "/login"
                failureUrl = "/"
                permitAll = true
                defaultSuccessUrl("/welcome", true)
            }

            logout {
                logoutUrl = "/logout"
                logoutSuccessUrl = "/"
            }
        }
        return http.build()
    }
}