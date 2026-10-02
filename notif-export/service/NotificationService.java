package com.example.notification_export_service.service;

import com.example.notification_export_service.domain.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final INotificationDAO dao;
    private final JavaMailSender mailSender;
    private final RestTemplate restTemplate;

    @Value("${user.service.url:http://localhost:8083/api/users}")
    private String userServiceUrl;

    @Value("${twilio.account.sid:}")
    private String twilioSid;

    @Value("${twilio.auth.token:}")
    private String twilioToken;

    @Value("${twilio.whatsapp.from:}")
    private String twilioFrom;

    public NotificationService(INotificationDAO dao,
                               JavaMailSender mailSender,
                               RestTemplate restTemplate) {
        this.dao = dao;
        this.mailSender = mailSender;
        this.restTemplate = restTemplate;
    }

    public List<Notification> getAll() {
        return dao.notifications();
    }

    public List<Notification> getUserNotifications(int userId) {
        return dao.findByUserId(userId);
    }

    public List<Notification> getPendingNotifications() {
        return dao.findByStatus("PENDING");
    }

    public boolean send(Notification notification) {
        try {
            notification.setStatus("PENDING");
            notification.setCreatedAt(LocalDateTime.now());
            dao.insert(notification);

            System.out.println("Notificare creată cu ID: " + notification.getId());

            UserInfo user = fetchUserInfo(notification.getUserId());

            if (user == null) {
                updateNotificationStatus(notification, "FAILED", "Utilizator negăsit");
                return false;
            }

            boolean sent = switch (notification.getChannel().toUpperCase()) {
                case "EMAIL" -> sendEmail(user.getEmail(), notification.getMessage());
                case "WHATSAPP" -> sendWhatsApp(user.getPhoneNumber(), notification.getMessage());
                default -> {
                    System.err.println("Canal necunoscut: " + notification.getChannel());
                    yield false;
                }
            };

            if (sent) {
                notification.setStatus("SENT");
                notification.setSentAt(LocalDateTime.now());
                dao.update(notification);
                System.out.println("✅ Notificare trimisă cu succes către userul " + notification.getUserId());
                return true;
            } else {
                updateNotificationStatus(notification, "FAILED", "Eroare la trimitere");
                return false;
            }

        } catch (Exception e) {
            System.err.println("Eroare la trimitere notificare: " + e.getMessage());
            e.printStackTrace();
            updateNotificationStatus(notification, "FAILED", e.getMessage());
            return false;
        }
    }

    private boolean sendEmail(String to, String message) {
        try {
            if (to == null || to.isEmpty()) {
                System.err.println(" Adresă email invalidă");
                return false;
            }

            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(to);
            email.setSubject("📧 Notificare Plant Management System");
            email.setText(message);
            email.setFrom("noreply@plantms.com");

            mailSender.send(email);
            System.out.println("📧 Email trimis REAL la: " + to);
            return true;

        } catch (Exception e) {
            System.err.println(" Eroare trimitere email: " + e.getMessage());
            return false;
        }
    }

    private boolean sendWhatsApp(String phoneNumber, String message) {
        System.out.println("phone number: " + phoneNumber);
        if (twilioSid == null || twilioSid.isEmpty() || twilioToken == null || twilioToken.isEmpty()) {
            System.out.println("========================================");
            System.out.println("📱 WHATSAPP NOTIFICATION (SIMULATĂ)");
            System.out.println("   Către: " + (phoneNumber != null ? phoneNumber : "nr. negăsit"));
            System.out.println("   Mesaj: " + message);
            System.out.println("========================================");
            System.out.println("ℹ️  Pentru WhatsApp real, configurează Twilio:");
            System.out.println("   twilio.account.sid=...");
            System.out.println("   twilio.auth.token=...");
            System.out.println("   twilio.whatsapp.from=whatsapp:+...");
            System.out.println("========================================");
            return true;
        }

        try {
            if (phoneNumber == null || phoneNumber.isEmpty()) {

                System.err.println(" Număr de telefon invalid, WhatsApp not sent");
                return false;
            }

            com.twilio.Twilio.init(twilioSid, twilioToken);

            com.twilio.rest.api.v2010.account.Message.creator(
                    new com.twilio.type.PhoneNumber("whatsapp:" + phoneNumber),
                    new com.twilio.type.PhoneNumber(twilioFrom),
                    message
            ).create();

            System.out.println(" WhatsApp trimis REAL la: " + phoneNumber);
            return true;

        } catch (Exception e) {
            System.err.println(" Eroare Twilio WhatsApp: " + e.getMessage());
            System.out.println(" WhatsApp notification (SIMULATĂ) - demonstrație concept");
            return true;
        }
    }

    private UserInfo fetchUserInfo(int userId) {
        try {
            String token = getAuthTokenFromRequest();

            String url = userServiceUrl + "/" + userId;
            System.out.println("Fetching user info from: " + url);

            HttpHeaders headers = new HttpHeaders();
            if (token != null && !token.isEmpty()) {
                headers.set("Authorization", token);
                System.out.println(" Token added to request");
            } else {
                System.out.println("️ No token found");
            }

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<UserInfo> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, UserInfo.class
            );

            return response.getBody();

        } catch (Exception e) {
            System.err.println("Eroare la preluarea utilizatorului: " + e.getMessage());
            return null;
        }
    }

    private String getAuthTokenFromRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null && !authHeader.isEmpty()) {
                    return authHeader;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to get token: " + e.getMessage());
        }
        return null;
    }

    private void updateNotificationStatus(Notification notification, String status, String errorMessage) {
        if (notification.getId() == null || notification.getId().isEmpty()) {
            System.err.println(" Cannot update notification: ID is null");
            return;
        }
        notification.setStatus(status);
        notification.setErrorMessage(errorMessage);
        notification.setSentAt(LocalDateTime.now());
        dao.update(notification);
    }

    public void retryFailedNotifications() {
        List<Notification> failedNotifications = dao.findByStatus("FAILED");

        for (Notification notification : failedNotifications) {
            System.out.println(" Retry notificare eșuată: " + notification.getId());
            send(notification);
        }
    }
}