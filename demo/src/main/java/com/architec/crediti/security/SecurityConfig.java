package com.architec.crediti.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationProvider customAuthProvider;

    @Autowired
    public SecurityConfig(CustomAuthenticationProvider customAuthProvider) {
        this.customAuthProvider = customAuthProvider;
    }

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

                //Everyone can see these pages
                .antMatchers("/login", "/", "/createexternaluser","/registersucces", "/createexternal", "/passwordRecovery", "/notapproved").permitAll()

                //COORDINATOR && DOCENT
                .antMatchers("/allFullAssignments", "/listStudents", "/unapprovedEnrollments", "/approveEnroll/{id}", "/deleteEnroll/{id}").hasAnyRole("DOCENT", "COORDINATOR")

                //Only for EXTERNAL
                .antMatchers("/externalUserProfile").hasRole("EXTERNE")

                //Only for STUDENTS
                .antMatchers("/portfolio", "/studentenroll/{assignmentId}").hasRole("STUDENT")

                //Only for COORDINATOR
                .antMatchers("/tag", "/listAllTags", "/listUnvalidatedExternal", "/archive").hasRole("COORDINATOR")

                //For all but EXTERN
                .antMatchers("/listAllAssignments", "/allassignments", "/documentation").hasAnyRole("STUDENT", "DOCENT", "COORDINATOR")

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
