package kz.iitu.itse1910.issenbayev;

import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import kz.iitu.itse1910.issenbayev.repository.UserRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class IssenbayevApplication {

	static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		context = SpringApplication.run(IssenbayevApplication.class, args);

		printUsers();
		printUsers();
	}

	private static void printUsers() {
		UserRepository userRepository = context.getBean(UserRepositoryImpl.class);
		List<User> users = userRepository.findAllPaginated(0, 5);
		for (User u : users) {
			System.out.println(u);
		}
	}
}

