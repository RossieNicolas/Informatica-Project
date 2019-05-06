package com.architec.crediti;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${spring.ldap.urls}")
    private String ldapUrls;
    @Value("${spring.ldap.base}")
    private String ldapBaseDn;
    @Value("${spring.ldap.username}")
    private String ldapSecurityPrincipal;
    @Value("${spring.ldap.password}")
    private String ldapPrincipalPassword;
    @Value("${ldap.user.dn.pattern}")
    private String ldapUserDnPattern;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] staticResources  =  {
                "/css/**",
                "/images/**",
        };


        http
                .authorizeRequests()
                    //pagina's die niet-ingelogde gebruikers zien
                    .antMatchers(staticResources).permitAll()
                    .antMatchers("/login", "/").permitAll()
                    //Alle andere pagina's blokkeren
                    .anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?error")
                    .defaultSuccessUrl("/dashboard", true)
                    .permitAll()
                .and()
                .logout()
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .ldapAuthentication()
                    .contextSource()
                        .url(ldapUrls + ldapBaseDn)
                        .managerDn(ldapSecurityPrincipal)
                        .managerPassword(ldapPrincipalPassword)
                    .and()
                        .userDnPatterns(ldapUserDnPattern)
                        .userSearchFilter("userPrincipalName={0}")
                        .userSearchBase("OU=Users,OU=DWAP")
                        .groupSearchBase("OU=Distribution groups,OU=Groups,OU=DWAP")
                        .groupRoleAttribute("CN")
                        .groupSearchFilter("memberOf={0}")
                        .userDetailsContextMapper(userDetailsContextMapper());
    }

    @Bean
    public UserDetailsContextMapper userDetailsContextMapper() {
        return new CustomUserDetailsContextMapper();
    }
}
