package filmfocus.services;

import filmfocus.models.dtos.ItemDto;
import filmfocus.models.entities.Cinema;
import filmfocus.models.entities.Hall;
import filmfocus.models.entities.Movie;
import filmfocus.models.entities.Order;
import filmfocus.models.entities.User;
import filmfocus.testUtils.constants.ProjectionConstants;
import filmfocus.testUtils.factories.OrderFactory;
import filmfocus.testUtils.factories.TicketFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static filmfocus.testUtils.constants.ItemConstants.PRICE;
import static filmfocus.testUtils.constants.OrderConstants.TOTAL_PRICE;
import static filmfocus.testUtils.constants.ReportConstants.END_DATE;
import static filmfocus.testUtils.constants.ReportConstants.ID;
import static filmfocus.testUtils.constants.ReportConstants.START_DATE;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IncomeReportServiceTest {

    @Mock
    private OrderService orderService;

    @Mock
    private TicketService ticketService;

    @Mock
    private CinemaService cinemaService;

    @Mock
    private HallService hallService;

    @Mock
    private ItemService itemService;

    @Mock
    private MovieService movieService;

    @Mock
    private UserService userService;

    @InjectMocks
    private IncomeReportService incomeReportService;

    @Test
    public void testGetAllIncomesByCinemaId_doubleReturned_success() {
        when(cinemaService.getCinemaById(anyInt())).thenReturn(new Cinema());
        when(orderService.getOrdersByDateBetween(any(), any())).thenReturn(OrderFactory.getDefaultOrderList());

        double result = incomeReportService.getAllIncomesByCinemaId(ID, START_DATE, END_DATE);

        assertEquals(TOTAL_PRICE, result, 0.0);
    }

    @Test
    public void testGetAllIncomesByCinemaId_cinemaIdDifferent_returnsZero() {
        List<Order> orders = OrderFactory.getDefaultOrderList();
        orders.get(0).getTickets().get(0).getProjection().getHall().getCinema().setId(0);

        when(cinemaService.getCinemaById(anyInt())).thenReturn(new Cinema());
        when(orderService.getOrdersByDateBetween(any(), any())).thenReturn(orders);

        double result = incomeReportService.getAllIncomesByCinemaId(ID, START_DATE, END_DATE);

        assertEquals(0, result, 0.0);
    }

    @Test
    public void testGetAllIncomesByHallId_doubleReturned_success() {
        when(hallService.getHallById(anyInt())).thenReturn(new Hall());
        when(orderService.getOrdersByDateBetween(any(), any())).thenReturn(OrderFactory.getDefaultOrderList());

        double result = incomeReportService.getAllIncomesByHallId(ID, START_DATE, END_DATE);

        assertEquals(TOTAL_PRICE, result, 0.0);
    }

    @Test
    public void testGetAllIncomesByHallId_hallIdDifferent_returnsZero() {
        List<Order> orders = OrderFactory.getDefaultOrderList();
        orders.get(0).getTickets().get(0).getProjection().getHall().setId(0);

        when(hallService.getHallById(anyInt())).thenReturn(new Hall());
        when(orderService.getOrdersByDateBetween(any(), any())).thenReturn(orders);

        double result = incomeReportService.getAllIncomesByHallId(ID, START_DATE, END_DATE);

        assertEquals(0, result, 0.0);
    }

    @Test
    public void testGetAllIncomesByItemId_doubleReturned_success() {
        when(itemService.getItemDtoById(anyInt())).thenReturn(new ItemDto());
        when(orderService.getOrdersByDateBetween(any(), any())).thenReturn(OrderFactory.getDefaultOrderList());

        double result = incomeReportService.getAllIncomesByItemId(ID, START_DATE, END_DATE);

        assertEquals(PRICE, result, 0.0);
    }

    @Test
    public void testGetAllIncomesByMovieId_doubleReturned_success() {
        when(movieService.getMovieById(anyInt())).thenReturn(new Movie());
        when(ticketService.getTicketsByDateBetween(any(), any())).thenReturn(TicketFactory.getDefaultTicketList());

        double result = incomeReportService.getAllIncomesByMovieId(ID, START_DATE, END_DATE);

        assertEquals(ProjectionConstants.PRICE, result, 0.0);
    }

    @Test
    public void testGetAllIncomesByUserId_doubleReturned_success() {
        when(userService.getUserById(anyInt())).thenReturn(new User());
        when(orderService.getOrdersByDateBetween(any(), any())).thenReturn(OrderFactory.getDefaultOrderList());

        double result = incomeReportService.getAllIncomesByUserId(ID, START_DATE, END_DATE);

        assertEquals(TOTAL_PRICE, result, 0.0);
    }
}