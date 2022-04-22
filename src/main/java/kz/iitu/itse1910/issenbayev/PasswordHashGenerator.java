//package kz.iitu.itse1910.issenbayev;
//
//import lombok.AllArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@Component
//@AllArgsConstructor
//public class PasswordHashGenerator implements CommandLineRunner {
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(String... args) throws Exception {
//        List<String> passwords = Stream.generate(() -> "password").limit(10).collect(Collectors.toList());
//        for (int i = 0; i < passwords.size(); i++) {
//            System.out.println(i + ": " + passwordEncoder.encode(passwords.get(i)));
//        }
//    }
//}
