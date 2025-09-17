package uz.darvoza.baxrin_darvoza.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import uz.darvoza.baxrin_darvoza.entity.EmailHistroyEntity;
import uz.darvoza.baxrin_darvoza.enums.AppLanguage;
import uz.darvoza.baxrin_darvoza.enums.EmailType;
import uz.darvoza.baxrin_darvoza.exps.AppBadException;
import uz.darvoza.baxrin_darvoza.util.JwtUtil;
import uz.darvoza.baxrin_darvoza.util.RendomUtil;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class EmailSendingService {
    private Integer emailLimit=3;
    @Value("${spring.mail.username}")
    private String fromAccount;
    @Value("${server.domain}")
    private String serverDomain;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailHistoryService emailHistoryService;

    public void sendRegistrationEmail(String email, Integer profileId, AppLanguage language) {
        String subject = "Registration Confirmation";
        String body = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "    <style>\n" +
                "        a {\n" +
                "            padding: 10px 30px;\n" +
                "            display: inline-block;\n" +
                "        }\n" +
                "        .button-link{\n" +
                "            text-decoration: none;\n" +
                "            color: white;\n" +
                "            background-color: indianred;\n" +
                "        }\n" +
                "        .button-link:hover{\n" +
                "            background-color: #dd4444;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Registration Confirmation</h1>\n" +
                "<p>Salom ishlar qale</p>\n" +
                "<p>\n" +
                "    Please click to button for completing registration: <a class=\"button-link\"\n" +
                "        href=\" http://localhost:8082/auth/api/v1/verification/%s?language=%s\" target=\"_blank\">Click there</a>\n" +
                "</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        body=String.format(body, JwtUtil.encode(profileId),language);
        System.out.println(JwtUtil.encode(profileId));
        sendMimeEmail(email, subject, body);
        System.out.println(serverDomain);

    }
    public void sendResetPasswordEmail(String email, AppLanguage language) {
        String subject = "Reset Password confirem";
        String code= RendomUtil.getRandomSmsCode();
        String body = "Sizning codingiz:"+ code;

        checkSendMimeEmail(email,subject,body,code);


    }
    public void checkSendMimeEmail(String email, String subject, String body,String code){
        log.debug("checkSendMimeEmail chaqirildi: email: {} subject: {}",email,subject);

        Long count= emailHistoryService.getEmailCount(email);
        log.debug("Ushbu email uchun avval yuborilgan xatlar soni: {}", count);
        if (count>=emailLimit) {
            System.out.println("Email limit reached: Email=" + email);
            throw new AppBadException("email limit reached");
        }
        emailHistoryService.create(email,code, EmailType.RESET_PASWORD);
        sendMimeEmail(email, subject, body);
    }
    private void sendMimeEmail(String email, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            mimeMessage.setFrom(fromAccount);

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);
            CompletableFuture.runAsync(() ->{
                mailSender.send(mimeMessage);
            });
        }catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
