package com.trs.tickets.service;

import com.trs.tickets.mappers.UserMapper;
import com.trs.tickets.model.dto.UserDto;
import com.trs.tickets.model.entity.PasswordResetToken;
import com.trs.tickets.repository.TokenRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final TokenRepository tokenRepository;
    private final UserMapper userMapper;
    @Value("${spring.mail.username}")
    private String senderEmailID;
    private static final String ENDPOINT_URL = "http://localhost:5550/resetPassword";

    public void sendEmail(UserDto user) {
        try {
            String resetLink = generateResetToken(user);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(senderEmailID);
            helper.setTo(user.getUsername());

            String htmlMsg = """
                    <!doctype html>
                    <html lang="en-US">
                    <head>
                        <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
                        <style type="text/css">
                            a:hover {text-decoration: underline !important;}
                        </style>
                    </head>
                                        
                    <body marginheight="0" topmargin="0" marginwidth="0" style="margin: 0px; background-color: #f2f3f8;" leftmargin="0">
                        <!--100% body table-->
                        <table cellspacing="0" border="0" cellpadding="0" width="100%" bgcolor="#f2f3f8"
                            style="@import url(https://fonts.googleapis.com/css?family=Rubik:300,400,500,700|Open+Sans:300,400,600,700); font-family: 'Open Sans', sans-serif;">
                            <tr>
                                <td>
                                    <table style="background-color: #f2f3f8; max-width:670px;  margin:0 auto;" width="100%" border="0"
                                        align="center" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td style="height:80px;">&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td style="text-align:center;">
                                              <h3>Cinemagic</h3>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="height:20px;">&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0"
                                                    style="max-width:670px;background:#fff; border-radius:3px; text-align:center;-webkit-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);-moz-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);box-shadow:0 6px 18px 0 rgba(0,0,0,.06);">
                                                    <tr>
                                                        <td style="height:40px;">&nbsp;</td>
                                                    </tr>
                                                    <tr>
                                                        <td style="padding:0 35px;">
                                                            <h1 style="color:#1e1e2d; font-weight:500; margin:0;font-size:32px;font-family:'Rubik',sans-serif;">You have
                                                                requested to reset your password!</h1>
                                                            <span
                                                                style="display:inline-block; vertical-align:middle; margin:29px 0 26px; border-bottom:1px solid #cecece; width:100px;"></span>
                                                            <p style="color:#455056; font-size:15px;line-height:24px; margin:0;">
                                                                We cannot simply send you your old password. A unique link to reset your
                                                                password has been generated for you. To reset your password, click the
                                                                following button and follow the instructions.
                                                            </p>
                                                            <a href="{resetPasswordAnchor}"
                                                                style="background:#0dcaf0;text-decoration:none !important; font-weight:500; margin-top:35px; color:#fff;text-transform:uppercase; font-size:14px;padding:10px 24px;display:inline-block;border-radius:50px;">Reset
                                                                Password</a>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td style="height:40px;">&nbsp;</td>
                                                    </tr>
                                                </table>
                                            </td>
                                        <tr>
                                            <td style="height:20px;">&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td style="text-align:center;">
                                                <p style="font-size:14px; color:rgba(69, 80, 86, 0.7411764705882353); line-height:18px; margin:0 0 0;">&copy; <strong>Cinemagic</strong></p>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="height:80px;">&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                        <!--/100% body table-->
                    </body>
                                        
                    </html>
                    """.replace("{resetPasswordAnchor}", resetLink);

            helper.setText(htmlMsg, true); // Use this or above line.
            helper.setSubject("Cinemagic | Reset your password");
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public String generateResetToken(UserDto userDto) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expiryDateTime = currentDateTime.plusMinutes(30);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(userMapper.convert(userDto));
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDateTime(expiryDateTime);
        log.info("PasswordResetToken: {}", resetToken);
        tokenRepository.save(resetToken);
        return ENDPOINT_URL + "/" + resetToken.getToken();
    }

    public boolean hasExpired(LocalDateTime expiryDateTime) {
        return expiryDateTime.isBefore(LocalDateTime.now());
    }
}
