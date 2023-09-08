package filmfocus.mappers;

import filmfocus.models.dtos.TicketDto;
import filmfocus.testUtils.constants.ProjectionConstants;
import filmfocus.testUtils.factories.TicketFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static filmfocus.testUtils.constants.TicketConstants.DATE_OF_PURCHASE;
import static filmfocus.testUtils.constants.TicketConstants.ID;
import static filmfocus.testUtils.factories.ProjectionFactory.getDefaultProjectionDto;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TicketMapperTest {

    @Mock
    private ProjectionMapper projectionMapper;

    @InjectMocks
    private TicketMapper ticketMapper;

    @Test
    public void testMapTicketToTicketDto_success() {
        when(projectionMapper.mapProjectionToProjectionDto(any())).thenReturn(getDefaultProjectionDto());

        TicketDto ticket = ticketMapper.mapTicketToTicketDto(TicketFactory.getDefaultTicket());

        assertEquals(ticket.getId(), ID);
        assertEquals(ticket.getDateOfPurchase(), DATE_OF_PURCHASE);
        assertEquals(ticket.getProjection().getStartTime(), ProjectionConstants.START_TIME);
    }

    @Test
    public void testMapTicketsToTicketsDto_success() {
        when(projectionMapper.mapProjectionToProjectionDto(any())).thenReturn(getDefaultProjectionDto());

        List<TicketDto> ticketDtos = ticketMapper.mapTicketToDtoList(TicketFactory.getDefaultTicketList());
        TicketDto ticket = ticketDtos.get(0);

        assertEquals(ticket.getId(), ID);
        assertEquals(ticket.getDateOfPurchase(), DATE_OF_PURCHASE);
        assertEquals(ticket.getProjection().getStartTime(), ProjectionConstants.START_TIME);
    }
}
