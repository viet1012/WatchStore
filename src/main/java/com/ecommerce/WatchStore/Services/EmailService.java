package com.ecommerce.WatchStore.Services;


import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.BillDetail;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Repositories.UserRepository;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class EmailService {

    private final String username = "vietta678@gmail.com";
    private final String password = "qgqa djtt fvul vqps";

    public void scheduleEmailSending(int hoursToAdd, int minutesToAdd, String emailTo) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Thêm số giờ và số phút vào thời gian hiện tại
        LocalDateTime targetDateTime = currentDateTime.plusHours(hoursToAdd).plusMinutes(minutesToAdd);
        System.out.println("targetDateTime: " + targetDateTime);


        long initialDelay;
        if (currentDateTime.isBefore(targetDateTime)) {
            initialDelay = Duration.between(currentDateTime, targetDateTime).toMinutes(); // Tính số phút delay nếu thời gian hiện tại trước thời gian gửi email
        } else {
            targetDateTime = targetDateTime.plusDays(1); // Cộng 1 ngày nếu đã qua thời gian gửi email trong ngày
            initialDelay = Duration.between(currentDateTime, targetDateTime).toMinutes(); // Tính số phút delay tới 8:00 sáng ngày hôm sau
        }

        System.out.println("initialDelay: " + initialDelay);
        scheduler.scheduleAtFixedRate(() -> {
            // Gọi hàm gửi email tại đây
            sendEmailWithAdsForNewUser(emailTo);
        }, initialDelay, 24 * 60, TimeUnit.MINUTES); // Lặp lại mỗi 24 giờ (24 * 60 phút)
    }

    public boolean sendEmailWithAds(String emailTo) {
        boolean isSended = false;
        String subject = "Ưu đãi hấp dẫn từ V-WATCH Cửa hàng Đồng Hồ Chất Lượng Cao!";
        String body = "<p>Chào bạn,</p>\n"
                + "<p>Chúng tôi rất vui thông báo về những ưu đãi hấp dẫn đang chờ đón bạn tại V-WATCH của chúng tôi!</p>\n"
                + "<p>Đừng bỏ lỡ cơ hội sở hữu những mẫu đồng hồ độc đáo và sang trọng với giá ưu đãi cực kỳ hấp dẫn. Dù bạn đang tìm kiếm một chiếc đồng hồ phong cách cho bản thân hoặc làm quà tặng cho người thân yêu, chúng tôi cam kết mang đến sự lựa chọn đa dạng và chất lượng nhất.</p>\n"
                + "<p>Những ưu đãi đặc biệt đang chờ đợi bạn:</p>\n"
                + "<ul style=\"color: blue;\"> <!-- Thêm màu cho danh sách ưu đãi -->\n"
                + "<li>Giảm giá đặc biệt cho các mẫu đồng hồ cao cấp</li>\n"
                + "<li>Quà tặng hấp dẫn kèm theo cho các đơn hàng</li>\n"
                + "<li>Ưu đãi các phụ kiện cho mùa lễ giáng sinh sắp tới</li>\n"
                + "</ul>\n"
                + "<p>Hãy ghé thăm cửa hàng của chúng tôi ngay hôm nay để trải nghiệm không gian mua sắm đầy thú vị và lựa chọn cho mình những sản phẩm ưng ý nhất.</p>\n"
                + "<p><a href=\"https://www.example.com\">Click ngay để khám phá</a></p>\n"
                + "<p>Cảm ơn bạn đã luôn ủng hộ và tin tưởng <strong>  V-Watch Cửa hàng Đồng Hồ Chất Lượng Cao </strong> của chúng tôi. </p>\n"
                + "<p>Trân trọng,</p>\n"
                + "<p> <strong> V-WATCH </strong></p>";

        try {
            Properties prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            isSended = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSended;
    }

    public boolean sendEmailWithAdsForNewUser(String emailTo) {
        boolean isSended = false;
        String subject = "V-WATCH nhớ bạn thật nhiều";
        StringBuilder body = new StringBuilder();
        body.append("<p>Chào bạn,</p>\n")
                .append("<p>Chúng tôi rất vui thông báo về những ưu đãi hấp dẫn đang chờ đón bạn tại V-WATCH của chúng tôi!</p>\n")
                .append("<p>Đừng bỏ lỡ cơ hội sở hữu những mẫu đồng hồ độc đáo và sang trọng với giá ưu đãi cực kỳ hấp dẫn. Dù bạn đang tìm kiếm một chiếc đồng hồ phong cách cho bản thân hoặc làm quà tặng cho người thân yêu, chúng tôi cam kết mang đến sự lựa chọn đa dạng và chất lượng nhất.</p>\n")
                .append("<p>Những ưu đãi đặc biệt đang chờ đợi bạn:</p>\n")
                .append("<ul style=\"color: blue;\">\n")
                .append("<li>Giảm giá đặc biệt cho các mẫu đồng hồ cao cấp</li>\n")
                .append("<li>Quà tặng hấp dẫn kèm theo cho các đơn hàng</li>\n")
                .append("<li>Ưu đãi các phụ kiện cho mùa lễ giáng sinh sắp tới</li>\n")
                .append("</ul>\n")
                .append("<p>Hãy ghé thăm cửa hàng của chúng tôi ngay hôm nay để trải nghiệm không gian mua sắm đầy thú vị và lựa chọn cho mình những sản phẩm ưng ý nhất.</p>\n")
                .append("<p><a href=\"https://www.example.com\">Click ngay để khám phá</a></p>\n")
                .append("<p>Cảm ơn bạn đã luôn ủng hộ và tin tưởng <strong>V-Watch Cửa hàng Đồng Hồ Chất Lượng Cao</strong> của chúng tôi.</p>\n")
                .append("<p>Trân trọng,</p>\n")
                .append("<p><strong>V-WATCH</strong></p>");

        try {
            Properties prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body.toString(), "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            isSended = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSended;
    }

    public boolean sendEmailWithOTP(String emailTo, String display_name, String otp) {
        boolean isSended = false;
        String subject = "Xác thực tài khoản - Mã OTP của bạn";
        String body = "<p>Chào " + display_name + ",</p>\n"
                + "    <p>Chúng tôi đã nhận được yêu cầu xác thực tài khoản của bạn. Dưới đây là mã OTP của bạn:</p>\n"
                + "    <p><strong>Mã OTP:</strong> <u><span style='color: blue;'>" + otp + "</span></u></p>\n"
                + "    <p>Mã OTP này sẽ có hiệu lực trong vòng 10 phút kể từ thời điểm bạn nhận được email này. Vui lòng không chia sẻ mã OTP này với bất kỳ ai khác.</p>\n"
                + "    <p>Xin cảm ơn.</p>";
        try {

            Properties prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            isSended = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSended;
    }


    public boolean sendOrderConfirmationEmail(String emailTo, Bill bill, List<BillDetail> billDetails) {
        boolean isSended = false;
        String subject = "Xác nhận đơn hàng";
        String body = "<p>Xin chào " + bill.getUser().getDisplayName() + ",</p>\n"
                + "<p>Cảm ơn bạn đã đặt hàng. Đơn hàng của bạn đã được ghi nhận.</p>\n"
                + "<p>Thông tin đơn hàng:</p>\n"

                + "<p>Địa chỉ giao hàng: <u><span style='color: blue;'>" + bill.getDeliverAddress() + "</span></u>  </p>\n"
                + "<p>Tổng giá: <u><span style='color: blue;'>" + bill.getTotalPrice() + "</span></u></p>\n"
                + "<p>Chi tiết đơn hàng:</p>\n"
                + "<ul>";

        for (BillDetail detail : billDetails) {
            body += "<li><strong style=\"color:blue;\">" + detail.getProduct().getProductName() + "</strong> - Số lượng: <strong style=\"color:blue;\">" + detail.getQuantity() + "</strong></li>";
        }

        body += "</ul><p>Xin cảm ơn.</p>";

        try {

            Properties prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            isSended = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSended;
    }

    public boolean sendVerificationEmail(String emailTo, String verificationCode) {
        boolean isSended = false;
        String subject = "Xác minh tài khoản - Quên mật khẩu";
        String body = "Chào bạn,<br><br>"
                + "Đây là mã OTP (One Time Password) để xác minh quên mật khẩu của bạn:<br><br>"
                + "<span style='color: #ff0000; font-weight: bold;'>" + verificationCode + "</span><br><br>"
                + "Vui lòng nhập mã này để thiết lập lại mật khẩu của bạn.<br><br>"
                + "Trân trọng,<br>"
                + "<p> <strong> V-WATCH </strong></p>";


        try {
            Properties prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            isSended = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSended;
    }

}
