
package com.promoit.service;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.promoit.util.JDBCPropertyLoader;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsmpp.bean.*;
import org.jsmpp.session.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class NotificationService {
    private Session emailSession;
    private String fromEmail;
    private Properties emailProps;

    private String smppHost;
    private int smppPort;
    private String smppSystemId;
    private String smppPassword;
    private String smppSystemType;
    private String smppSourceAddr;

    private String telegramToken;
    private String telegramChatId;
    private String telegramApiUrl;

    public NotificationService() throws Exception {
        loadEmailConfig();
        loadSmsConfig();
        loadTelegramConfig();
    }

    private void loadEmailConfig() throws Exception {
        emailProps = new Properties();
        emailProps.load(getClass().getClassLoader().getResourceAsStream("email.properties"));
        fromEmail = emailProps.getProperty("email.from");
        emailSession = Session.getInstance(emailProps, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailProps.getProperty("email.username"),
                        emailProps.getProperty("email.password"));
            }
        });
    }

    private void loadSmsConfig() throws Exception {
        Properties smsProps = new Properties();
        smsProps.load(getClass().getClassLoader().getResourceAsStream("sms.properties"));
        smppHost = smsProps.getProperty("smpp.host");
        smppPort = Integer.parseInt(smsProps.getProperty("smpp.port"));
        smppSystemId = smsProps.getProperty("smpp.system_id");
        smppPassword = smsProps.getProperty("smpp.password");
        smppSystemType = smsProps.getProperty("smpp.system_type");
        smppSourceAddr = smsProps.getProperty("smpp.source_addr");
    }

    private void loadTelegramConfig() throws Exception {
        Properties tgProps = new Properties();
        tgProps.load(getClass().getClassLoader().getResourceAsStream("telegram.properties"));
        telegramToken = tgProps.getProperty("telegram.token");
        telegramChatId = tgProps.getProperty("telegram.chat_id");
        telegramApiUrl = "https://api.telegram.org/bot" + telegramToken + "/sendMessage";
    }

    public void sendEmail(String toEmail, String code) throws Exception {
        Message message = new MimeMessage(emailSession);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Ваш OTP-код");
        message.setText("Ваш код: " + code);
        Transport.send(message);
    }

    public void sendSms(String destination, String code) {
        try {
            SMPPClientSession session = new SMPPClientSession(smppHost, smppPort);
            BindParameter bindParam = new BindParameter(BindType.BIND_TX, smppSystemId, smppPassword, smppSystemType,
                    TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, null);
            session.connectAndBind(smppHost, smppPort, bindParam);

            SubmitSm submit = new SubmitSm();
            submit.setSourceAddr(smppSourceAddr);
            submit.setDestAddr(destination);
            submit.setShortMessage(("Ваш код: " + code).getBytes());

            session.submit(submit);
            session.unbindAndClose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTelegram(String message) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String url = telegramApiUrl + "?chat_id=" + telegramChatId + "&text=" + URLEncoder.encode(message, StandardCharsets.UTF_8);
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(request)) {
                // ignore
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(String code) {
        try (PrintWriter out = new PrintWriter(new FileWriter("otp_codes.txt", true))) {
            out.println(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
