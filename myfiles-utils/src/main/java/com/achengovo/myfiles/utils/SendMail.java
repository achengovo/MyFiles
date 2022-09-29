package com.achengovo.myfiles.utils;

import org.slf4j.Logger;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMail {
    static Logger log = org.slf4j.LoggerFactory.getLogger(SendMail.class);
    //发送网络邮箱服务器邮件
    public static void sendNetMail(String email, String emailMsg, String Subject)
            throws AddressException, MessagingException {
        //创建配置对象
        Properties props = new Properties();
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");
        // 创建验证器
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                //设置发送人的帐号和密码
                return new PasswordAuthentication("achengovo@qq.com", "mailpassword");
            }
        };
        // 创建邮件对象
        Session session = Session.getInstance(props, auth);
        // 2.创建一个Message，它相当于是邮件内容
        Message message = new MimeMessage(session);
        //设置发送者
        message.setFrom(new InternetAddress("achengovo@qq.com"));
        //设置发送方式与接收者
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        //设置邮件主题
        message.setSubject(Subject);
        //设置邮件内容
        message.setContent(emailMsg, "text/html;charset=utf-8");
        // 3.创建 Transport用于将邮件发送
        Transport.send(message);
        log.info("发送邮件："+emailMsg+"-->"+email);
    }
}
