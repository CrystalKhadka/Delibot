package com.system.online_ordering_system.service.impl;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.system.online_ordering_system.entity.Bill;
import com.system.online_ordering_system.entity.Cart;
import com.system.online_ordering_system.entity.Qr;
import com.system.online_ordering_system.entity.User;
import com.system.online_ordering_system.repo.BillRepo;
import com.system.online_ordering_system.repo.CartRepo;
import com.system.online_ordering_system.repo.EmailCredRepo;
import com.system.online_ordering_system.repo.QrRepo;
import com.system.online_ordering_system.service.BillService;
import com.system.online_ordering_system.service.CartService;
import com.system.online_ordering_system.service.UserService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final JavaMailSender getJavaMailSender;
    private final EmailCredRepo emailCredRepo;
    private final ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    @Qualifier("emailConfigBean")
    private Configuration emailConfig;
    private final BillRepo billRepo;
    private final CartRepo cartRepo;
    private final QrRepo qrRepo;
    private final UserService userService;

    @Override
    public void generateBill() {
        Bill bill = new Bill();
        User user = userService.getActiveUser().get();
        bill.setUser(user);
        List<Cart> cartList = cartRepo.findAllByUserAndStatusUnpaid(user.getId());
        double total = 0;
        for (Cart cart : cartList) {
            total += cart.getTotalPrice();
        }
        bill.setTotal(total);

        LocalDateTime localDateTime = LocalDateTime.now();
        bill.setDate(localDateTime);

        billRepo.save(bill);

        for (Cart cart : cartList) {
            cart.setStatus("Paid");
            cart.setBill(bill);
            cartRepo.save(cart);
        }

    }

    @Override
    public List<Bill> getAllBillsByUser(int id) {
        return billRepo.findAllByUser(id);
    }

    @Override
    public Optional<Bill> getLatestBillByUser(int userId) {
        // Retrieve the latest bill for the specified user ID
        return billRepo.findTopByUserIdOrderByDateDesc(userId);
    }

    @Override
    public List<Bill> getBillForTenDays() {
        User user = userService.getActiveUser().get();
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate startDate = localDateTime.toLocalDate().minusDays(10);
        LocalDate endDate = localDateTime.toLocalDate().plusDays(1);
        return billRepo.findBillForTenDays(user.getId(), startDate, endDate);
    }

    @SuppressWarnings("null")
    @Override
    public void sentQrCodeToUser(int id) {
        try {
            User user = userService.getActiveUser().get();
            String email = user.getEmail();
            Map<String, Object> model = new HashMap<>();
            model.put("name", user.getFirstName() + " " + user.getLastName());
            String qrSecret = generateRandomNumber();

            String qrData = String.format("Bill ID: %d, QR Secret: %s, Destination: %s", id, qrSecret, email);
            model.put("bill_id", id);
            Qr qr = new Qr();
            qr.setBill_id(id);
            qr.setQr_secret(qrSecret);
            qr.setDestination(email);
            qrRepo.save(qr);
            MimeMessage message = getJavaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Template template = emailConfig.getTemplate("Email/qrTemp.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText(html, true);
            mimeMessageHelper.setSubject("Registration");
            mimeMessageHelper.setFrom("khadkacrystal23@gmail.com");

            // Generate QR code image
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, com.google.zxing.BarcodeFormat.QR_CODE, 250, 250);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            // Add QR code image as an attachment
            ByteArrayDataSource dataSource = new ByteArrayDataSource(outputStream.toByteArray(), "image/png");
            mimeMessageHelper.addAttachment("qrcode.png", dataSource);
            taskExecutor.execute(() -> {
                getJavaMailSender.send(message);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateRandomNumber() {
        Random random = new Random();

        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
        // Generates a random number between 100000 and 999999 (inclusive)
    }
}
