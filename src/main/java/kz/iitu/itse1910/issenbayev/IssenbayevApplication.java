package kz.iitu.itse1910.issenbayev;

import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootApplication
public class IssenbayevApplication {
	private static final PageRequest defaultPageRequest = PageRequest.of(0, 10);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(IssenbayevApplication.class, args);
		UserRepository userRepository = context.getBean(UserRepository.class);
//		Page<User> unassignedLeadDevs = userRepository.findUnassignedLeadDevs(defaultPageRequest);
//		unassignedLeadDevs.forEach(System.out::println);
//		Page<User> assignedLeadDevs = userRepository.findAssignedLeadDevs(defaultPageRequest);
//		assignedLeadDevs.forEach(System.out::println);

//		Page<User> unassignedDevelopers = userRepository.findUnassignedDevelopers(defaultPageRequest);
//		unassignedDevelopers.forEach(System.out::println);
//		Page<User> assignedDevelopers = userRepository.findAssignedDevelopers(defaultPageRequest);
//		assignedDevelopers.forEach(System.out::println);
	}
}

