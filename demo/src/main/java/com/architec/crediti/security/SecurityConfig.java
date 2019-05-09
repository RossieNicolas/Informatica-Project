package com.architec.crediti.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] staticResources = {
                "/css/**",
                "/images/**",
        };


        http
                .authorizeRequests()
                //pagina's die niet-ingelogde gebruikers zien
                .antMatchers(staticResources).permitAll()
                .antMatchers("/login", "/", "/createexternaluser","/registersucces", "/createexternal").permitAll()
                //Alle andere pagina's blokkeren
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/loginError")
                .defaultSuccessUrl("/main", true)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthProvider);
    }
}
