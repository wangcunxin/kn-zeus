package com.taobao.zeus.util;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.mail.util.MailSSLSocketFactory;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * hc 2017-07-31 update
 * 修改支持qsq邮箱发送
 */
public class MailUtil {


    public static String[] ccMail = {""};   // 底层 cc mail

    private static Logger log=LoggerFactory.getLogger(MailUtil.class);
    private static String mailUser = "";    // 发件人
    private static String mailPassword = ""; // 发件人密码


    private static String infKey = "0512";

    private static MailUtil signle = null;
    public static MailUtil getInstance() {
        if (signle == null ) {
            signle = new MailUtil();
        }
        return signle;
    }

     public static void main(String[] args) {

         // "jobId=12&channel=zeus_rd_dispatch&password=123"
         String mailContent = "任务id：" + "12" + ", 任务名：" + "Detail" + ", 描述：" + "hello" + ", 执行结果：失败。请检查恢复";
         // isRecovery(mailContent);

         if( send("wanghaichao@kuainiugroup.com", "Aa123455",
                 new String[]{"wanghaichao@kuainiugroup.com"},
                 new String[]{"wanghaichao@kuainiugroup.com", "haichaozz@163.com" },
                 "恢复测试",
                 mailContent,
                 null) ) {
             System.out.println("邮件发送成功！！！！");
         } else {
             System.out.println("邮件发送失败！！！！");
         }
     }

     public static boolean isRecovery(String mailContent) {
        boolean is = false;
        String regex = "任务id：(.*), 任务名：";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mailContent);//匹配类
        String id = null;
        while (matcher.find()) {
            id = matcher.group(1);//打印中间字符
        }
        if (id == null) return is;

         try {
             String pwd = md5("zeus_dispatch_channel_stream" + "#" + infKey + "#" + id);
             String res = GetResponse("jobId=" + id + "&channel=zeus_dispatch_channel_stream&password=" + pwd);
             JSONObject json = JSONObject.fromObject(res);
             String code = json.getString("code");
             if (code.equals("1000")) is = true;
         } catch (IOException e) {
             e.printStackTrace();
         }
         return is;
     }

     public static String  GetResponse(String Info) throws IOException {
        String path = "http://10.105.216.57:9094/recovery/restart/job/";

        //1, 得到URL对象
        URL url = new URL(path);

        //2, 打开连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        //3, 设置提交类型
        conn.setRequestMethod("POST");

        //4, 设置允许写出数据,默认是不允许 false
        conn.setDoOutput(true);
        conn.setDoInput(true);//当前的连接可以从服务器读取内容, 默认是true

        //5, 获取向服务器写出数据的流
        OutputStream os = conn.getOutputStream();
        //参数是键值队  , 不以"?"开始
        os.write(Info.getBytes());
        //os.write("googleTokenKey=&username=admin&password=5df5c29ae86331e1b5b526ad90d767e4".getBytes());
        os.flush();
        //6, 获取响应的数据
        //得到服务器写回的响应数据
        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
        String str = br.readLine();
        return  str;
    }


    /**
     * 发送邮件方法
     * @param fromMailAddr
     * @param fromMailPassword
     * @param toMailAddary
     * @param ccMailAddrArr
     * @param subject
     * @param content
     * @param attachUrls
     * @return
     */
    public static boolean send( String fromMailAddr, String fromMailPassword, String[] toMailAddary,
                               String[] ccMailAddrArr, String subject, String content, String[] attachUrls) {

        boolean sendResult = false;

        if ( isRecovery(content) ) content = "*** 任务已提交恢复接口，请等待恢复! *** \n" + content; else content = "******* 任务恢复失败，需手动恢复 *******" + content;

        if (null == fromMailAddr || fromMailAddr.equals("")) {
            fromMailAddr = mailUser;
        }

        if (null == fromMailPassword || fromMailPassword.equals("")) {
            fromMailPassword = mailPassword;
        }
        // String[] toMailAddary = null;
        // if ( toMailAdd.equals("1") ) {
        //     toMailAddary = lstMailOne;
        // } else if (toMailAdd.equals("2")) {
        //     toMailAddary = lstMailTwo;
        // } else if (toMailAdd.equals("3")) {
        //     toMailAddary = lstMailTre;
        // } else if ( !toMailAdd.equals("") && toMailAdd != null) {
        //     toMailAddary = new String[] { toMailAdd };
        // }

        List<InternetAddress> toMailValidateAddrList = new ArrayList<InternetAddress>();
        for(String toMailAddr: toMailAddary) {
            try {
                toMailValidateAddrList.add(new InternetAddress(toMailAddr));
            } catch (Exception e) {
                log.error("illegal recipient mail address " + toMailAddr + " ignore", e);
            }
        }

        if (0 == toMailValidateAddrList.size()) {
            log.error("the recipient mail addresses are all illegal!");
            return sendResult;
        }

        List<InternetAddress> ccMailStandardAddrList = new ArrayList<InternetAddress>();
        if (null != ccMailAddrArr && 0 != ccMailAddrArr.length) {
            for(String ccMailAddr : ccMailAddrArr) {
                try {
                    ccMailStandardAddrList.add(new InternetAddress(ccMailAddr));
                } catch (Exception e) {
                    log.error("illegal cc mail address " + ccMailAddr + " ignore", e);
                }
            }
        }

        List<MimeBodyPart> attachList = new ArrayList<MimeBodyPart>();
        if (null != attachUrls && 0 != attachUrls.length) {
            for(String attachUrl : attachUrls) {
                try {
                    MimeBodyPart attached = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(attachUrl);
                    attached.setDataHandler(new DataHandler(fds));
                    attached.setFileName(doHandlerFileName(attachUrl));
                    attachList.add(attached);
                } catch (Exception e) {
                    log.error("illegal attach url " + attachUrl + " ignore", e);
                }
            }
        }

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

        Session session = Session.getInstance(props, new MyAuthenricator(fromMailAddr, fromMailPassword));

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(fromMailAddr);
            msg.addRecipients(Message.RecipientType.TO, toMailValidateAddrList.toArray(new InternetAddress[toMailValidateAddrList.size()]));
            if (ccMailStandardAddrList.size() > 0) {
                msg.addRecipients(Message.RecipientType.CC, ccMailStandardAddrList.toArray(new InternetAddress[ccMailStandardAddrList.size()]));
            }
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            MimeMultipart mmp = new MimeMultipart();
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setContent(content,"text/plain;charset=gb2312");
            mmp.addBodyPart(mbp);

            for(MimeBodyPart oneAttach : attachList) {
                mmp.addBodyPart(oneAttach);
            }
            msg.setContent(mmp);

            Transport.send(msg);
            sendResult = true;
        } catch (Exception e) {
            log.error("send failed, exception: " + e);
        }


        return sendResult;
    }

    private static String doHandlerFileName(String filePath){
        String fileName = filePath;
        if(null != filePath && !"".equals(filePath)){
            if (filePath.lastIndexOf("\\") != -1) {
                fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
            } else if (filePath.lastIndexOf("/") != -1) {
                fileName = filePath.substring(filePath.lastIndexOf("/")+1);
            }

        }
        return fileName;
    }

    /**
     * 获取String的MD5值
     *
     * @param info 字符串
     * @return 该字符串的MD5值
     */
    private static String md5(String info) {
        try {
            //获取 MessageDigest 对象，参数为 MD5 字符串，表示这是一个 MD5 算法（其他还有 SHA1 算法等）：
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //update(byte[])方法，输入原数据
            //类似StringBuilder对象的append()方法，追加模式，属于一个累计更改的过程
            md5.update(info.getBytes("UTF-8"));
            //digest()被调用后,MessageDigest对象就被重置，即不能连续再次调用该方法计算原数据的MD5值。可以手动调用reset()方法重置输入源。
            //digest()返回值16位长度的哈希值，由byte[]承接
            byte[] md5Array = md5.digest();
            //byte[]通常我们会转化为十六进制的32位长度的字符串来使用,本文会介绍三种常用的转换方法
            return bytesToHex1(md5Array);
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private static String bytesToHex1(byte[] md5Array) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < md5Array.length; i++) {
            int temp = 0xff & md5Array[i];//TODO:此处为什么添加 0xff & ？
            String hexString = Integer.toHexString(temp);
            if (hexString.length() == 1) {//如果是十六进制的0f，默认只显示f，此时要补上0
                strBuilder.append("0").append(hexString);
            } else {
                strBuilder.append(hexString);
            }
        }
        return strBuilder.toString();
    }

}
