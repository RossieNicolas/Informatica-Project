package com.architec.crediti;

import com.architec.crediti.models.User;
import com.architec.crediti.repositories.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import java.util.Collection;
import java.util.List;

@Configuration
public class CustomUserDetailsContextMapper extends LdapUserDetailsMapper implements UserDetailsContextMapper {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    UserRepository userRepo;

    @Override
    public LdapUserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {

        LdapUserDetailsImpl details = (LdapUserDetailsImpl) super.mapUserFromContext(ctx, username, authorities);
        log.info("--- START ATTRIBUTES ---");
        log.info("Firstname: " + ctx.getStringAttribute("givenName"));
        log.info("Lastname: " + ctx.getStringAttribute("description").substring(ctx.getStringAttribute("givenName").length() + 1));
        log.info("Email: " + ctx.getStringAttribute("userPrincipalName"));
        log.info("Departement: " + ctx.getStringAttribute("department"));
        log.info("Role: " + ctx.getStringAttribute("extensionAttribute1"));
        log.info("--- END ATTRIBUTES ---");

        String firstname = ctx.getStringAttribute("givenName");
        String lastname = ctx.getStringAttribute("description").substring(ctx.getStringAttribute("givenName").length() + 1);
        String email = ctx.getStringAttribute("userPrincipalName");
        String department = ctx.getStringAttribute("department");
        String role = ctx.getStringAttribute("extensionAttribute1");

//        String userEmail =  userRepo.findByEmail(email);
//
//            if (userEmail == null) {
//                User user = new User(firstname, lastname, email, role, true);
//                userRepo.save(user);
//            } else {
//                for (User u : userRepo.findAll()) {
//                    if (u.getEmail().equals(email)) {
//                        User user = userRepo.getOne(u.getUserId());
//                        user.setLoginCount(u.getLoginCount() + 1);
//                    }
//                }
//            }


        return new CustomUserDetails(details);
    }


    @Override
    public void mapUserToContext(UserDetails userDetails, DirContextAdapter dirContextAdapter) {

    }
}
