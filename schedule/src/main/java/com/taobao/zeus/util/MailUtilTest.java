package com.taobao.zeus.util;

import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;

public class MailUtilTest {

    public static void main(String[] args) {
//        send();
//        sendMultiReceivers();
//        sendToGroup();
//        send4Cc();
//        sendWithAttach();
//        String[] tests = null;
//        for(int i = 0; i < tests.length ; i++) {
//            System.out.println(tests[i]);
//        }
//
//        System.out.println(tests.length);

//        List<String> strList = new ArrayList<String>();
//        for(String str : strList) {
//            System.out.println("haha");
//        }
        sendMail();
    }

    /**
     * 发送office365邮件
     * @return
     */
    public static boolean send() {
        return false;
    }

    public static void sendMail() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.exmail.qq.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.ssl.enable", "true");

        // 使用ssl验证
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException ex ) {
            ex.printStackTrace();
        }
        props.put("mail.smtp.ssl.socketFactory", sf);

//        props.put("mail.debug", "true");

        Session session = Session.getInstance(props, new MyAuthenricator("wanghaichao@kuainiugroup.com", "Aa123455"));

        try {
            System.out.println("begin to send mail...........");
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom("wanghaichao@kuainiugroup.com");
            msg.setRecipients(Message.RecipientType.TO,
                    "wanghaichao@kuainiugroup.com");
            msg.setSubject("zeus hc test");
            msg.setSentDate(new Date());
            msg.setText("the third mail!\n");
            Transport.send(msg);
            System.out.println("send mail successfully!!!!");
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
        }

    }

    public static void sendMultiReceivers() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", "587");

//        props.put("mail.debug", "true");

        Session session = Session.getInstance(props, null);

        try {
            System.out.println("begin to send mail...........");
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom("wanghaichao@kuainiugroup.com");
            msg.setRecipients(Message.RecipientType.TO,
                    new InternetAddress[]{
                        new InternetAddress("wanghaichao@kuainiugroup.com"),
                            new InternetAddress("wanghaichao@kuainiugroup.com")});
            msg.setSubject("test multiple users");
            msg.setSentDate(new Date());
            msg.setText("what a fuck!\n");
            Transport.send(msg, "wanghaichao@kuainiugroup.com", "Aa123455");
            System.out.println("send mail successfully!!!!");
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
        }

    }


    public static void sendToGroup() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", "587");

//        props.put("mail.debug", "true");

        Session session = Session.getInstance(props, null);

        try {
            System.out.println("begin to send mail...........");
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom("wanghaichao@kuainiugroup.com");
            msg.setRecipients(Message.RecipientType.TO, "wanghaichao@kuainiugroup.com");
            msg.setSubject("just for test, please ignore");
            msg.setSentDate(new Date());
            msg.setText("happy new year, everyone!\n");
            Transport.send(msg, "wanghaichao@kuainiugroup.com", "Aa123455");
            System.out.println("send mail successfully!!!!");
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
        }

    }


    public static void send4Cc() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", "587");

//        props.put("mail.debug", "true");

        Session session = Session.getInstance(props, null);

        try {
            System.out.println("begin to send mail...........");
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom("wanghaichao@kuainiugroup.com");
            msg.addRecipients(Message.RecipientType.TO, "wanghaichao@kuainiugroup.com");
            msg.addRecipients(Message.RecipientType.CC, "wanghaichao@kuainiugroup.com");
            msg.setSubject("just for test, please ignore");
            msg.setSentDate(new Date());
            msg.setText("i am back!\n");
            Transport.send(msg, "wanghaichao@kuainiugroup.com", "Aa123455");
            System.out.println("send mail successfully!!!!");
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
        }

    }

    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", "587");

//        props.put("mail.debug", "true");

        return Session.getInstance(props, null);
    }

    private static String doHandlerFileName(String filePath){
        String fileName=filePath;
        if(null !=filePath && !"".equals(filePath)){
            if (filePath.lastIndexOf("\\") != -1) {
                fileName=filePath.substring(filePath.lastIndexOf("\\")+1);
            } else if (filePath.lastIndexOf("/") != -1) {
                fileName=filePath.substring(filePath.lastIndexOf("/")+1);
            }

        }
        return fileName;
    }

    public static void sendWithAttach() {
        Session session = getSession();

        try {
            System.out.println("begin to send mail...........");
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom("wanghaichao@kuainiugroup.com");
            msg.addRecipients(Message.RecipientType.TO, "wanghaichao@kuainiugroup.com");
            msg.setSubject("测试123456");
            msg.setSentDate(new Date());


            MimeBodyPart attached = new MimeBodyPart();
            String filePath = "E:\\software\\CDH5.3.6离线安装手册.pdf";
            FileDataSource fds = new FileDataSource(filePath);
            attached.setDataHandler(new DataHandler(fds));
            String fileName = doHandlerFileName(filePath);
//            String validateFileName = MimeUtility.encodeWord(fileName);
            attached.setFileName(fileName);

            MimeMultipart mmp = new MimeMultipart();
            mmp.addBodyPart(attached);

            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setContent("请查看附件\n", "text/plain;charset=gb2312");
            mmp.addBodyPart(mbp);

            msg.setContent(mmp);

            Transport.send(msg, "wanghaichao@kuainiugroup.com", "Aa123455");
            System.out.println("send mail successfully!!!!");
        } catch (Exception e) {
            System.out.println("send failed, exception: " + e);
        }

    }

}

