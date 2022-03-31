package kz.iitu.itse1910.issenbayev;

import kz.iitu.itse1910.issenbayev.entity.Ticket;
import kz.iitu.itse1910.issenbayev.repository.TicketRepository;
<<<<<<< HEAD
<<<<<<< HEAD
=======
import kz.iitu.itse1910.issenbayev.service.UserService;
>>>>>>> a747d78 (Wrote unassigned and overdue tickets JPQL queries)
=======
>>>>>>> 2799244 (Done: send unassigned and overdue ticket reports to lead devs)
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@SpringBootApplication
public class IssenbayevApplication {

	static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		context = SpringApplication.run(IssenbayevApplication.class, args);


<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 2799244 (Done: send unassigned and overdue ticket reports to lead devs)
//		TicketRepository ticketRepository = context.getBean(TicketRepository.class);
//		List<Object[]> unassignedTickets = ticketRepository.findUnassignedTickets();
//		for (Object[] t : unassignedTickets) {
//			for (Object field : t) {
//				System.out.println(field);
//			}
//			System.out.println();
//		}
//
//		List<Object[]> overdueTickets = ticketRepository.findOverdueTicketReports();
//		for (Object[] t : overdueTickets) {
//			for (Object field : t) {
//				System.out.println(field);
//			}
//			System.out.println();
//		}
<<<<<<< HEAD
=======
		TicketRepository ticketRepository = context.getBean(TicketRepository.class);
		List<Object[]> unassignedTickets = ticketRepository.findUnassignedTickets();
		for (Object[] t : unassignedTickets) {
			for (Object field : t) {
				System.out.println(field);
			}
			System.out.println();
		}

		List<Object[]> overdueTickets = ticketRepository.findOverdueTickets();
		for (Object[] t : overdueTickets) {
			for (Object field : t) {
				System.out.println(field);
			}
			System.out.println();
		}
>>>>>>> a747d78 (Wrote unassigned and overdue tickets JPQL queries)
=======
>>>>>>> 2799244 (Done: send unassigned and overdue ticket reports to lead devs)

//		UserService userService = context.getBean(UserService.class);
//		userService.register(new UserSignupReq("email@test.com", "aldiyar04", "pass"));

//		printTickets();
//		System.out.println();
//		printTickets();
	}

	private static void printTickets() {
		TicketRepository ticketRepository = context.getBean(TicketRepository.class);
		List<Ticket> tickets = ticketRepository.findAll(PageRequest.of(0, 6)).getContent();
		printList(tickets);
	}

	private static void printList(List list) {
		for (Object o : list) {
			System.out.println(o);
		}
	}
}

