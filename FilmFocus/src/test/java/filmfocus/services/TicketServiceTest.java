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
import filmfocus.testUtils.factories.ProjectionFactory;
import filmfocus.testUtils.factories.TicketFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static filmfocus.testUtils.constants.ReportConstants.END_DATE;
import static filmfocus.testUtils.constants.ReportConstants.START_DATE;
import static filmfocus.testUtils.constants.TicketConstants.ID;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceTest {

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ProjectionService projectionService;

    @InjectMocks
    private TicketService ticketService;

    @Test
    public void testAddTicket_noExceptions_success() {
        LocalDate programDate = LocalDate.now().plusDays(1);
        LocalTime projectionStartTime = LocalTime.now().plusHours(1);

        Projection projection = ProjectionFactory.getDefaultProjection();
        projection.getProgram().setProgramDate(programDate);
        projection.setStartTime(projectionStartTime);

        TicketRequest request = TicketFactory.getDefaultTicketRequest();
        request.setProjectionId(projection.getId());

        int availableTickets = 10;
        Ticket expected = TicketFactory.getDefaultTicket();

        when(projectionService.getProjectionById(anyInt())).thenReturn(projection);
        when(ticketRepository.save(any())).thenReturn(expected);
        when(ticketService.calculateAvailableTickets(projection)).thenReturn(availableTickets);

        Ticket ticket = ticketService.addTicket(request);

        assertEquals(expected, ticket);
    }


    @Test
    public void testGetTicketsByProjectionId_noExceptions_success() {
        List<TicketDto> expected = TicketFactory.getDefaultTicketDtoList();

        when(ticketRepository.findTicketByProjectionId(anyInt())).thenReturn(TicketFactory.getDefaultTicketList());
        when(projectionService.getProjectionById(anyInt())).thenReturn(ProjectionFactory.getDefaultProjection());
        when(ticketMapper.mapTicketToTicketDto(any())).thenReturn(TicketFactory.getDefaultTicketDto());

        List<TicketDto> result = ticketService.getTicketsByProjectionId(ID);

        assertEquals(expected, result);
    }

    @Test
    public void testGetTicketById_noExceptions_success() {
        Ticket expected = TicketFactory.getDefaultTicket();

        when(ticketRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        Ticket result = ticketService.getTicketById(ID);

        assertEquals(expected, result);
    }

    @Test(expected = TicketNotFoundException.class)
    public void testGetTicketById_TicketNotFoundException_fail() {
        when(ticketRepository.findById(anyInt())).thenReturn(Optional.empty());

        ticketService.getTicketById(ID);
    }

    @Test
    public void testCalculateAvailableTickets_shouldReturnCorrectAvailableTickets_success() {
        Projection projection = ProjectionFactory.getDefaultProjection();
        int capacity = 100;
        int totalTickets = 50;

        when(ticketRepository.countByProjectionId(anyInt())).thenReturn(totalTickets);

        int expectedAvailableTickets = capacity - totalTickets;
        int actualAvailableTickets = ticketService.calculateAvailableTickets(projection);

        assertEquals(expectedAvailableTickets, actualAvailableTickets);
    }

    @Test(expected = NoAvailableTicketsException.class)
    public void testCalculateAvailableTickets_shouldThrowNoAvailableTicketsException() {
        Projection projection = ProjectionFactory.getDefaultProjection();
        int totalTickets = 100;

        when(ticketRepository.countByProjectionId(anyInt())).thenReturn(totalTickets);

        ticketService.calculateAvailableTickets(projection);
    }
    @Test(expected = DateNotValidException.class)
    public void testAddTicket_dateNotValid_throwsDateNotValidException() {
        LocalDate programDate = LocalDate.now().minusDays(1);
        LocalTime projectionStartTime = LocalTime.now().minusHours(1);

        Projection projection = ProjectionFactory.getDefaultProjection();
        projection.getProgram().setProgramDate(programDate);
        projection.setStartTime(projectionStartTime);

        TicketRequest request = TicketFactory.getDefaultTicketRequest();
        request.setProjectionId(projection.getId());

        when(projectionService.getProjectionById(anyInt())).thenReturn(projection);

        ticketService.addTicket(request);
    }



    @Test
    public void testGetTicketsByDateBetween() {
        List<Ticket> expected = TicketFactory.getDefaultTicketList();

        when(ticketRepository.findTicketsByDateOfPurchaseBetween(any(), any())).thenReturn(expected);

        List<Ticket> tickets = ticketService.getTicketsByDateBetween(START_DATE, END_DATE);

        assertEquals(expected, tickets);
    }
}
