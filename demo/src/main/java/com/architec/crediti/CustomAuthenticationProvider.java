package com.architec.crediti;

import com.architec.crediti.models.User;
import com.architec.crediti.repositories.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
import java.util.Hashtable;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private Log log = LogFactory.getLog(this.getClass());

    @Value("${spring.ldap.urls}")
    private String ldapUrl;

    @Autowired
    UserRepository userRepo;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        String username = auth.getName();
        String password = auth.getCredentials().toString();

        if (isLdapRegisteredUser(username, password)) {
            return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
        } else {
            throw new AuthenticationCredentialsNotFoundException("Invalid Credentials!");
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
            env.put(Context.PROVIDER_URL, ldapUrl);

            ctx = new InitialLdapContext(env, null);

            InitialDirContext context = new InitialDirContext(env);
            SearchControls ctrls = new SearchControls();
            ctrls.setReturningAttributes(new String[]{"givenName", "sn", "department", "extensionAttribute1", "sAMAccountName"});
            ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> answers = context.search("OU=Users,OU=DWAP,DC=alpaca,DC=int", "(userPrincipalName=" + username + ")", ctrls);
            javax.naming.directory.SearchResult res = answers.nextElement();

            String rawFirstname = res.getAttributes().get("givenname").toString();
            String rawLastname = res.getAttributes().get("sn").toString();
            String rawDepartment = res.getAttributes().get("department").toString();
            String rawRole = res.getAttributes().get("extensionAttribute1").toString();
            String rawStudentNr = res.getAttributes().get("sAMAccountName").toString();
            String email = username;

            String firstname = (rawFirstname.substring(rawFirstname.lastIndexOf(" ") + 1));
            String lastname = (rawLastname.substring(rawLastname.lastIndexOf(" ") + 1));
            String department = (rawDepartment.substring(rawDepartment.lastIndexOf(" ") + 1));
            String role = (rawRole.substring(rawRole.lastIndexOf(" ") + 1));
            String studentNr = (rawStudentNr.substring(rawStudentNr.lastIndexOf(" ") + 2));

            boolean emailExsist = userRepo.existsByEmail(email);

            if (!emailExsist) {
                User user = new User(firstname, lastname, email, role, studentNr, true);
                userRepo.save(user);
            }

            result = true;

            log.info("LDAP Connection Successful!");

        } catch (NamingException nex) {
            System.out.println("LDAP Connection: FAILED");
            nex.printStackTrace(); //TODO: show error to user
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
