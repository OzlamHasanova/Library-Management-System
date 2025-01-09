package com.intelliacademy.orizonroute.librarymanagmentsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN") // admin dashboard-a yalnız admin roluna malik olanlar girə bilər
                .anyRequest().permitAll() // qalan bütün requestlər icazəli olsun
                .and()
                .formLogin()
                .loginPage("/login") // login səhifəsi
                .defaultSuccessUrl("/admin", true) // uğurlu login sonrası yönləndiriləcək səhifə
                .failureUrl("/login?error=true") // uğursuz login sonrası yönləndiriləcək səhifə
                .permitAll() // bütün istifadəçilərə icazə ver
                .and()
                .logout()
                .logoutUrl("/logout") // logout URL
                .logoutSuccessUrl("/login?logout=true") // uğurlu logout sonrası yönləndiriləcək səhifə
                .permitAll(); // logout-u da hamıya açıq et

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
