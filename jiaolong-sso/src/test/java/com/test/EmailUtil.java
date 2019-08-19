package com.test;
/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/17 10:26
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Properties;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EmailUtil {

        public  void main(String[] args) throws AddressException, MessagingException {
            Properties properties = new Properties();
            properties.put("mail.transport.protocol", "smtp");// 连接协议
            properties.put("mail.smtp.host", "smtp.qq.com");// 主机名
            properties.put("mail.smtp.port", 465);// 端口号
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接
            properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
// 得到回话对象
            Session session = Session.getInstance(properties);
// 获取邮件对象
            Message message = new MimeMessage(session);
// 设置发件人邮箱地址
            message.setFrom(new InternetAddress("1017887438@qq.com"));
// 设置收件人地址
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[] { new InternetAddress("1017887438@qq.com") });
// 设置邮件标题
            message.setSubject("邮件标题");
// 设置邮件内容
            message.setText("java邮件内容");
// 得到邮差对象
            Transport transport = session.getTransport();
// 连接自己的邮箱账户
            transport.connect("1017887438@qq.com", "pwdckowasryhbeff");// 密码为刚才得到的授权码
// 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
        }




}
