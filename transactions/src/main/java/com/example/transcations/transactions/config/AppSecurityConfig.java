package com.example.transcations.transactions.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AppSecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((auth) -> {
                    try {
                        auth
                                .antMatchers("/").permitAll()
                                .antMatchers("/user-management/login").permitAll()
                                .antMatchers("/user/**").authenticated()
                                .antMatchers("/role/**").hasAuthority("admin")
                                .and()
                                .formLogin()
                                .loginPage("/user-management/login")
                                .and()
                                .httpBasic()
                                .and()
                                .logout()
                                .logoutUrl("/user-management/logout")
                                .logoutSuccessUrl("/user-management/login")
                                .invalidateHttpSession(true)
                                .permitAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                });
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
