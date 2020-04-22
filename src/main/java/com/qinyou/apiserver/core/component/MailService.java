package com.qinyou.apiserver.core.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 * 邮件工具
 *
 * @author chuang
 */
@Component
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from; //邮件发件人

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // 发送纯文本文件
    public boolean sendTextMail(String to, String subject, String content) {
        boolean flag = false;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            flag = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return flag;
    }

    // 发送 html 邮件
    public boolean sendHtmlMail(String to, String subject, String content) {
        boolean flag = false;
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper send = new MimeMessageHelper(message, true);
            send.setFrom(from);
            send.setTo(to);
            send.setSubject(subject);
            send.setText(content, true);
            mailSender.send(message);
            flag = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return flag;
    }

}
