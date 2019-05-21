package com.architec.crediti.controllers;

import com.architec.crediti.email.EmailServiceImpl;
import com.architec.crediti.models.ExternalUser;
import com.architec.crediti.repositories.ExternalUserRepository;
import com.architec.crediti.repositories.HashPass;
import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

@Controller
public class PasswordRecoveryController {
    private final
    ExternalUserRepository externalUserRepo;

    private final
    UserRepository userRepo;


    @Autowired
    private EmailServiceImpl emailImpl;

    @Autowired
    public PasswordRecoveryController(ExternalUserRepository externalUserRepo, UserRepository userRepo, EmailServiceImpl emailImpl) {

        this.externalUserRepo = externalUserRepo;
        this.emailImpl = emailImpl;
        this.userRepo = userRepo;
    }

    // Display forgotPassword page
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
    public ModelAndView displayForgotPasswordPage() {
        return new ModelAndView("forgotPassword");
    }

    @PostMapping(value = "/forgotPassword")
    public ModelAndView processForgotPasswordForm( ModelAndView modelAndView, @RequestParam("email") String userEmail, HttpServletRequest request) {

        // Lookup user in database by e-mail
        ExternalUser user = externalUserRepo.findByUserId(userRepo.findByEmail(userEmail));

        if (user == null) {
            modelAndView.addObject("errorMessage", "Er werd geen account gevonden voor het opgegeven emailadres.");
        } else {

            // Generate random 36-character string token for reset password
            user = externalUserRepo.findByUserId(userRepo.findByEmail(userEmail));
            user.setResetToken(UUID.randomUUID().toString());
            // Save token to database
            externalUserRepo.save(user);

            String appUrl = request.getScheme() + "://" + request.getServerName();

            // Email message
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setTo(user.getEmail());
            passwordResetEmail.setSubject("Wachtwoord Reset Aanvraag");
            passwordResetEmail.setText("Om je wachtwoord te resetten, click op onderstaande link:\n" + appUrl
                    + "/reset?token=" + user.getResetToken());

            emailImpl.sendSimpleMessage(user.getEmail() , "recovery" , "Om je wachtwoord te resetten, click op onderstaande link:\n" + appUrl
                    + ":8080/reset?token=" + user.getResetToken());

            // Add success message to view
            modelAndView.addObject("successMessage", "U hebt met succes een wachtwoordherstel aangevraagd. " +
                    "Er is een email verstuurd naar: " + userEmail);
        }

        modelAndView.setViewName("forgotPassword");
        return modelAndView;

    }

    // Display form to reset password
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public ModelAndView displayResetPasswordPage(ModelAndView modelAndView, @RequestParam("token") String token) {

        ExternalUser extUser = externalUserRepo.findByResettoken(token);

        if (extUser != null) { // Token found in DB
            modelAndView.addObject("resetToken", token);
        } else { // Token not found in DB
            modelAndView.addObject("errorMessage", "Oops!  Dit is een ongeldige link voor het opnieuw instellen van het wachtwoord.");
        }

        modelAndView.setViewName("updatePassword");
        return modelAndView;
    }

    // Process reset password form
    @RequestMapping(value = "/reset ", method = RequestMethod.POST)
    public ModelAndView setNewPassword(ModelAndView modelAndView, @RequestParam Map<String, String> requestParams, RedirectAttributes redir) {

        // Find the user associated with the reset token
        ExternalUser extUser = externalUserRepo.findByResettoken(requestParams.get("token"));

        // This should always be non-null but we check just in case
        if (extUser != null) {
            Object[] hashBytes = HashPass.convertToPbkdf2(requestParams.get("password").toCharArray());

            extUser.setSalt((byte[]) hashBytes[1]);

            // Set new password
            extUser.setPassword(hashBytes[0].toString().toCharArray());

            // Set the reset token to null so it cannot be used again
            extUser.setResetToken(null);

            // Save user
            externalUserRepo.save(extUser);

            // In order to set a model attribute on a redirect, we must use
            // RedirectAttributes
            redir.addFlashAttribute("successMessage", "\n" +
                    "62/5000\n" +
                    "U hebt met succes uw wachtwoord opnieuw ingesteld. U kunt nu inloggen.");

            modelAndView.setViewName("redirect:login");
            return modelAndView;

        } else {
            modelAndView.addObject("errorMessage",
                    "Oops!  Dit is een ongeldige link voor het opnieuw instellen van het wachtwoord.");
            modelAndView.setViewName("updatePassword");
        }

        return modelAndView;
    }

    // Going to reset page without a token redirects to login page
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex) {
        return new ModelAndView("redirect:login");
    }
}
