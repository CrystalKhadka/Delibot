package com.system.online_ordering_system.service.impl;

import com.system.online_ordering_system.config.PasswordEncoderUtil;
import com.system.online_ordering_system.dto.OtpDto;
import com.system.online_ordering_system.dto.UserDto;
import com.system.online_ordering_system.entity.Otp;

import com.system.online_ordering_system.entity.User;
import com.system.online_ordering_system.repo.EmailCredRepo;
import com.system.online_ordering_system.repo.OtpRepo;

import com.system.online_ordering_system.repo.UserRepo;
import com.system.online_ordering_system.service.UserService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final JavaMailSender getJavaMailSender;
    private final EmailCredRepo emailCredRepo;
    private final ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    @Qualifier("emailConfigBean")
    private Configuration emailConfig;
    private final String uploadDirectory = System.getProperty("user.dir") + "/user_images";


    private final OtpRepo otpRepo;
    @Override
    public void register(UserDto userDto) throws IOException {
        User user = userRepo.findByEmail(userDto.getEmail()).orElse(new User());
        user.setEmail(userDto.getEmail());
        user.setPassword(PasswordEncoderUtil.getInstance().encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setActive(false);

        user.setRole(userDto.getRole());
        userRepo.save(user);
    }


    @Override
    public void sendEmail(String email) {
        try {
            User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, String> model = new HashMap<>();
            model.put("name", user.getFirstName() + " " + user.getLastName());
            String otpCode= generateRandomNumber();
            model.put("otp", otpCode);
            Otp otp = otpRepo.findByEmail(email).orElse(new Otp());
            otp.setEmail(email);
            otp.setOtp(otpCode);
            otp.setDate(getDate());
            otpRepo.save(otp);

            MimeMessage message = getJavaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Template template = emailConfig.getTemplate("Email/emailTemp.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText(html, true);
            mimeMessageHelper.setSubject("Registration");
            mimeMessageHelper.setFrom("khadkacrystal23@gmail.com");;


            taskExecutor.execute(new Thread() {
                public void run() {
                    getJavaMailSender.send(message);
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void verifyOtp(OtpDto otpDto) {
        String email = otpDto.getEmail();
        String otp = otpDto.getOtp();
        Otp otp1 = otpRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found"));
        if (otp1.getOtp().equals(otp) && otp1.getDate().isAfter(LocalDateTime.now())) {
            User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
            user.setActive(true);
            userRepo.save(user);
        } else {
            throw new RuntimeException("Invalid OTP");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAllUsers();
    }

    @Override
    public Optional<User> getActiveUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepo.findByEmail(authentication.getName());
    }

    @Override
    public void deleteUser(int id) {
        userRepo.deleteById(id);
    }

    @Override
    public void sendResetEmail(String email) {
        try {
            User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, String> model = new HashMap<>();
            model.put("name", user.getFirstName() + " " + user.getLastName());
            String otpCode= generateRandomNumber();
            model.put("otp", otpCode);
            Otp otp = otpRepo.findByEmail(email).orElse(new Otp());
            otp.setEmail(email);
            otp.setOtp(otpCode);
            otp.setDate(getDate());
            otpRepo.save(otp);

            MimeMessage message = getJavaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Template template = emailConfig.getTemplate("Email/resetPassTemp.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText(html, true);
            mimeMessageHelper.setSubject("Registration");
            mimeMessageHelper.setFrom("khadkacrystal23@gmail.com");;


            taskExecutor.execute(new Thread() {
                public void run() {
                    getJavaMailSender.send(message);
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void resetPass(String email, String password, String Otp) throws IOException {
        Otp otp1 = otpRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found"));
        if (otp1.getOtp().equals(Otp) && otp1.getDate().isAfter(LocalDateTime.now())) {
            User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
            user.setPassword(PasswordEncoderUtil.getInstance().encode(password));
            userRepo.save(user);
        } else {
            throw new RuntimeException("Invalid OTP");
        }
    }

    @Override
    public void update(UserDto userDto) {
        User user = userRepo.findByEmail(userDto.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhone());
        userRepo.save(user);
    }


    @Override
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public static String generateRandomNumber() {
        Random random = new Random();

        int otp=100000 + random.nextInt(900000);
        return String.valueOf(otp);
         // Generates a random number between 100000 and 999999 (inclusive)
    }

    public LocalDateTime getDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.plusHours(1);
    }


}
