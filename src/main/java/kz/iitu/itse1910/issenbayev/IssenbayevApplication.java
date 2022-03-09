package kz.iitu.itse1910.issenbayev;

import kz.iitu.itse1910.issenbayev.entity.Ticket;
import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.repository.TicketRepository;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import kz.iitu.itse1910.issenbayev.repository.UserRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@SpringBootApplication
public class IssenbayevApplication {

	static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		context = SpringApplication.run(IssenbayevApplication.class, args);

		TicketRepository ticketRepository = context.getBean(TicketRepository.class);
		List<Ticket> tickets = ticketRepository.findAll(PageRequest.of(0, 6)).getContent();
		printList(tickets);

//		printUsers();
//		System.out.println();
//		printUsers();
	}

	private static void printUsers() {
		UserRepository userRepository = context.getBean(UserRepositoryImpl.class);
		List<User> users = userRepository.findAllPaginated(1, 5);
		for (User u : users) {
			System.out.println(u);
		}
	}

	private static void printList(List list) {
		for (Object o : list) {
			System.out.println(o);
		}
	}
}

