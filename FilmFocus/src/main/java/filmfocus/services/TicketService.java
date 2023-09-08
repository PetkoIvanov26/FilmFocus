package filmfocus.services;

import filmfocus.exceptions.DateNotValidException;
import filmfocus.exceptions.NoAvailableTicketsException;
import filmfocus.exceptions.TicketNotFoundException;
import filmfocus.mappers.TicketMapper;
import filmfocus.models.dtos.TicketDto;
import filmfocus.models.entities.Projection;
import filmfocus.models.entities.Ticket;
import filmfocus.models.requests.TicketRequest;
import filmfocus.repositories.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static filmfocus.utils.constants.ExceptionMessages.DATE_NOT_VALID_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.NO_AVAILABLE_TICKETS_EXCEPTION;
import static filmfocus.utils.constants.ExceptionMessages.TICKET_NOT_FOUND_MESSAGE;

@Service
public class TicketService {

  private static final Logger log = LoggerFactory.getLogger(TicketService.class);

  private final ProjectionService projectionService;
  private final TicketMapper ticketMapper;
  private final TicketRepository ticketRepository;

  @Autowired
  public TicketService(
    ProjectionService projectionService, TicketMapper ticketMapper, TicketRepository ticketRepository) {
    this.projectionService = projectionService;
    this.ticketMapper = ticketMapper;
    this.ticketRepository = ticketRepository;
  }

  public Ticket addTicket(TicketRequest request) {
    Projection projection = projectionService.getProjectionById(request.getProjectionId());

    LocalDate programDate = projection.getProgram().getProgramDate();
    LocalDate dateOfPurchase = LocalDate.now();
    LocalTime projectionStartTime = projection.getStartTime();
    LocalDateTime currentDateTime = LocalDateTime.now();


    if (programDate.isBefore(dateOfPurchase) || currentDateTime.toLocalTime().isAfter(projectionStartTime) ) {
      log.error(String.format("Exception caught: %s", DATE_NOT_VALID_MESSAGE));
      throw new DateNotValidException(DATE_NOT_VALID_MESSAGE);
    }


    calculateAvailableTickets(projection);

    Ticket ticket = new Ticket(dateOfPurchase, projection);

    log.info("An attempt to add a new ticket in the database");

    return ticketRepository.save(ticket);
  }

  public List<TicketDto> getTicketsByProjectionId(int id) {
    Projection projection = this.projectionService.getProjectionById(id);

    List<Ticket> tickets = ticketRepository.findTicketByProjectionId(projection.getId());

    log.info(String.format("All tickets with projection id %d were requested from the database", id));

    return tickets.stream().map(ticketMapper::mapTicketToTicketDto).collect(Collectors.toList());
  }

  public Ticket getTicketById(int id) {
    log.info(String.format("Retrieving ticket with id %d from database", id));

    return ticketRepository.findById(id).orElseThrow(() -> {
      log.error(String.format("Exception caught: %s", TICKET_NOT_FOUND_MESSAGE));

      throw new TicketNotFoundException(TICKET_NOT_FOUND_MESSAGE);
    });
  }

  public List<Ticket> getTicketsByDateBetween(LocalDate startDate, LocalDate endDate) {
    log.info(
      String.format("All tickets with date between %s and %s were requested from the database", startDate, endDate));

    return ticketRepository.findTicketsByDateOfPurchaseBetween(startDate, endDate);
  }

  public int calculateAvailableTickets(Projection projection) {
    int capacity = projection.getHall().getCapacity();
    int totalTickets = ticketRepository.countByProjectionId(projection.getId());
    int availableTickets = capacity - totalTickets;

    if (availableTickets <= 0) {
      log.error(String.format("Exception caught: %s", NO_AVAILABLE_TICKETS_EXCEPTION));

      throw new NoAvailableTicketsException(NO_AVAILABLE_TICKETS_EXCEPTION);
    }

    return capacity - totalTickets;
  }
}
