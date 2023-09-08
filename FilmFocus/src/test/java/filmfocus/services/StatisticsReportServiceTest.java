package filmfocus.services;

import filmfocus.models.dtos.ItemDto;
import filmfocus.models.entities.Category;
import filmfocus.testUtils.constants.ItemConstants;
import filmfocus.testUtils.factories.OrderFactory;
import filmfocus.testUtils.factories.TicketFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static filmfocus.testUtils.constants.MovieConstants.ID;
import static filmfocus.testUtils.constants.MovieConstants.TITLE;
import static filmfocus.testUtils.constants.ReportConstants.END_DATE;
import static filmfocus.testUtils.constants.ReportConstants.START_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsReportServiceTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private TicketService ticketService;

    @Mock
    private MovieService movieService;

    @Mock
    private ItemService itemService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private StatisticsReportService statisticsReportService;

    @Test
    public void testGetPurchasedTicketsCountByMovieCategory_success() {
        when(categoryService.getCategoryById(anyInt())).thenReturn(new Category());
        when(ticketService.getTicketsByDateBetween(any(), any())).thenReturn(TicketFactory.getDefaultTicketList());

        int result = statisticsReportService.getPurchasedTicketsCountByMovieCategory(ID, START_DATE, END_DATE);

        assertEquals(1, result);
    }

    @Test
    public void testGetPurchasedTicketsCountByMovieTitle_success() {
        List<Integer> movieIds = Arrays.asList(1, 2, 3);
        when(movieService.getIdsOfMoviesByTitle(anyString())).thenReturn(movieIds);
        when(ticketService.getTicketsByDateBetween(any(), any())).thenReturn(TicketFactory.getDefaultTicketList());

        int result = statisticsReportService.getPurchasedTicketsCountByMovieTitle(TITLE, START_DATE, END_DATE);

        assertEquals(1, result);
    }

    @Test
    public void testGetPurchasedItemsCountByItemName_success() {
        when(itemService.getItemDtoByName(anyString())).thenReturn(new ItemDto());
        when(orderService.getOrdersByDateBetween(any(), any())).thenReturn(OrderFactory.getDefaultOrderList());

        int result = statisticsReportService.getPurchasedItemsCountByItemName(ItemConstants.NAME, START_DATE, END_DATE);

        Assert.assertEquals(1, result);
    }
}