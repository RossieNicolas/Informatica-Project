package com.architec.crediti.security;

import com.architec.crediti.models.ExternalUser;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.ExternalUserRepository;
import com.architec.crediti.repositories.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private Log log = LogFactory.getLog(this.getClass());

    //Ldap attributes
    private static final String GIVEN_NAME = "givenName"; //firstname
    private static final String SN = "sn"; //lastname
    private static final String LDAP_SEARCH = "OU=Users,OU=DWAP,DC=alpaca,DC=int"; //Search string for LDAP

    @Value("${spring.ldap.urls}")
    private String ldapUrl;

    private final
    UserRepository userRepo;

    private final
    ExternalUserRepository exRepo;

    @Autowired
    public CustomAuthenticationProvider(UserRepository userRepo, ExternalUserRepository exRepo) {
        this.userRepo = userRepo;
        this.exRepo = exRepo;
    }


    @Override
    public Authentication authenticate(Authentication auth) {

        String username = auth.getName();
        String password = auth.getCredentials().toString();
        List<GrantedAuthority> grantedAuths = new ArrayList<>();

        if (isLdapRegisteredUser(username, password)) {
            log.info("LDAP successful");
            Role role = userRepo.findByEmail(username).getRole();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + role)); //student or docent
            return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
        } else if (isExternalUser(username, password)) {
            Role role = userRepo.findByEmail(username).getRole();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + role)); //should be extern
            return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
        } else {
            throw new AuthenticationCredentialsNotFoundException("Invalid Credentials!");
        }
    }

    private boolean isExternalUser(String username, String password) {
        boolean result = false;

        if (userRepo.findByEmail(username) != null) {
            User user = userRepo.findByEmail(username);

            if (exRepo.findByUserId(user) != null) {
                ExternalUser exUser = exRepo.findByUserId(user);

                // Make sure extern is not null
                String hash = HashPass.convertToPbkdf2Salt(password.toCharArray(), exUser.getSalt());

                if (Arrays.equals(hash.toCharArray(), exUser.getPassword())) {
                    result = true;
                }
            }
        }
        return result;
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
            env.put(Context.PROVIDER_URL, ldapUrl);

            ctx = new InitialLdapContext(env, null);

            InitialDirContext context = new InitialDirContext(env);
            SearchControls ctrls = new SearchControls();
            ctrls.setReturningAttributes(new String[]{GIVEN_NAME, SN});
            ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> answers = context.search(LDAP_SEARCH,
                    "(userPrincipalName=" + username + ")", ctrls);
            javax.naming.directory.SearchResult res = answers.nextElement();

            String rawFirstname = res.getAttributes().get(GIVEN_NAME).toString();
            String rawLastname = res.getAttributes().get(SN).toString();

            String firstname = (rawFirstname.substring(rawFirstname.lastIndexOf(' ') + 1));
            String lastname = (rawLastname.substring(rawLastname.lastIndexOf(' ') + 1));

            boolean emailExsist = userRepo.existsByEmail(username);

            if (!emailExsist) {
                boolean firstLogin = false;

                //check if student
                if (findRole(username.substring(0, 1)) == Role.STUDENT) {
                    firstLogin = true;
                }

                User user = new User(firstname, lastname, username, findRole(username.substring(0, 1)), firstLogin);
                userRepo.save(user);
            }

            result = true;

            log.info("LDAP Connection Successful!");

        } catch (NamingException nex) {
            log.info("LDAP Connection: FAILED");
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception ex) {
                    log.error(ex.toString());
                }
            }
        }
        return result;
    }

    private Role findRole(String emailSubstring) {
        Role role;
        switch (emailSubstring) {
            case "s":
            case "S":
                role = Role.STUDENT;
                break;
            case "p":
            case "P":
                role = Role.DOCENT;
                break;
            default:
                role = Role.NONE;
        }
        return role;
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}
