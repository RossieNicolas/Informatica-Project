package com.architec.crediti;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.ArrayList;
import java.util.Hashtable;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Value("${spring.ldap.urls}")
    private String ldapUrl;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        String username = auth.getName();
        String password = auth.getCredentials().toString();


        if (isLdapRegisteredUser(username, password)) {
            return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
        } else {
            return null;
        }

    }

    private boolean isLdapRegisteredUser(String username, String password) {

        boolean result = false;
        Hashtable<String, String> env = new Hashtable<>();
        LdapContext ctx = null;

        try {

            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, username);
            env.put(Context.SECURITY_CREDENTIALS, password);
            env.put(Context.PROVIDER_URL,  ldapUrl);

            ctx = new InitialLdapContext(env, null);

            result = true;
            String selectedLdapUrl = ctx.getEnvironment().get(Context.PROVIDER_URL).toString();
            // do further operations with "ctx" if needed
            System.out.println("selected LDAP url is: " + selectedLdapUrl);
            System.out.println("Connection Successful!");

        } catch(NamingException nex) {
            nex.printStackTrace();
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return result;

    }


    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}
