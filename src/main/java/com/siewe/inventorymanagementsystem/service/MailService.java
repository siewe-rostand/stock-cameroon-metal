package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.UserDto;
import com.siewe.inventorymanagementsystem.model.User;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";
    private static final String TRANSACTION = "transaction";
    private static final String BASE_URL = "baseUrl";

    @Autowired(required = false)
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Async
    public void sendEmailFromNoreplyAddress(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        javaMailSender.setUsername("noreply@pharma.com");
        javaMailSender.setPassword("pharmanr1");

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            //message.setFrom("noreply@pharma.com");
            message.setFrom(new InternetAddress("noreply@pharma.com", "pharma"));
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendEmailFromContactAddress(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        javaMailSender.setUsername("contact@sprinted.net");
        javaMailSender.setPassword("Oiseau+2018");

        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(new InternetAddress("contact@sprinted.net", "Famille Ndeng-Nsiah"));
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendCredentialsMail(User user, String mail, String baseUrl) {
        log.debug("Sending credentials to '{}'", mail);
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("creationEmail", context);
        String subject = messageSource.getMessage("email.creation.title", null, locale);
        sendEmailFromContactAddress(mail, subject, content, false, true);
    }

    @Async
    public void sendNewRegistrationEmail(UserDto userDto) {
        log.debug("Sending credentials to '{}'", "t.teufak@sprinted.net");
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable(USER, userDto);
        String content = templateEngine.process("newRegistrationEmail", context);
        String subject = messageSource.getMessage("email.newRegistration.title", null, locale);
        sendEmailFromContactAddress("t.teufak@sprinted.net", subject, content, false, true);
    }
}
