package kz.iitu.itse1910.issenbayev.feature.emailsending;

import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class PasswdChangeReminderEmailingScheduler {
    private final EmailSender emailSender;
    private final UserRepository userRepository;

    @Scheduled(initialDelayString = "${scheduling.email-sending.password-change-reminder.initial-delay}",
            fixedDelayString = "${scheduling.email-sending.password-change-reminder.fixed-delay}")
    public void sendToAllUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            String toEmail = user.getEmail();
            String subject = "Change password - Issue Tracker";
            String text = "Hello, " + user.getUsername() + "\n" +
                    "You haven't updated password for quite a while.\n" +
                    "Keep your account secure by changing password regularly.";
            emailSender.sendSimpleMessage(toEmail, subject, text);
        });
    }
}
