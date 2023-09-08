package filmfocus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import filmfocus.models.dtos.TicketDto;
import filmfocus.services.TicketService;
import filmfocus.testUtils.factories.TicketFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static filmfocus.testUtils.constants.ProjectionConstants.ID;
import static filmfocus.testUtils.constants.TicketConstants.DATE_OF_PURCHASE;
import static filmfocus.utils.constants.URIConstants.PROJECTIONS_ID_TICKETS_PATH;
import static filmfocus.utils.constants.URIConstants.TICKETS_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
public class TicketControllerTest {

  private final static ObjectMapper objectMapper = new ObjectMapper();

  private MockMvc mockMvc;

  @Mock
  private TicketService ticketService;

  @InjectMocks
  private TicketController ticketController;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(ticketController)
      .build();
  }

  @Test
  public void testAddTicket_ticketAdded_success() throws Exception {
    String json = objectMapper.writeValueAsString(TicketFactory.getDefaultTicketRequest());
    when(ticketService.addTicket(any())).thenReturn(TicketFactory.getDefaultTicket());

    mockMvc.perform(post(TICKETS_PATH)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", TICKETS_PATH + "/" + ID));
  }

  @Test
  public void testGetTicketsByProjectionId() throws Exception {
    List<TicketDto> defaultTicketDtoList = TicketFactory.getDefaultTicketDtoList();
    TicketDto defaultTicketDto = defaultTicketDtoList.get(0);
    when(ticketService.getTicketsByProjectionId(anyInt())).thenReturn(defaultTicketDtoList);

    mockMvc.perform(get(PROJECTIONS_ID_TICKETS_PATH, ID))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].dateOfPurchase[0]").value(DATE_OF_PURCHASE.getYear()))
           .andExpect(jsonPath("$[0].dateOfPurchase[1]").value(DATE_OF_PURCHASE.getMonthValue()))
           .andExpect(jsonPath("$[0].dateOfPurchase[2]").value(DATE_OF_PURCHASE.getDayOfMonth()))
           .andExpect(jsonPath("$[0].projection.id").value(defaultTicketDto.getProjection().getId()))
           .andExpect(jsonPath("$[0].projection.hall").value(defaultTicketDto.getProjection().getHall()))
           .andExpect(jsonPath("$[0].projection.price").value(defaultTicketDto.getProjection().getPrice()))
           .andExpect(jsonPath("$[0].projection.program.programDate[0]").value(
             defaultTicketDto.getProjection().getProgram().getProgramDate().getYear()))
           .andExpect(jsonPath("$[0].projection.program.programDate[1]").value(
             defaultTicketDto.getProjection().getProgram().getProgramDate().getMonthValue()))
           .andExpect(jsonPath("$[0].projection.program.programDate[2]").value(
             defaultTicketDto.getProjection().getProgram().getProgramDate().getDayOfMonth()))
           .andExpect(jsonPath("$[0].projection.program.cinema").value(
             defaultTicketDto.getProjection().getProgram().getCinema()))
           .andExpect(
             jsonPath("$[0].projection.movie.id").value(defaultTicketDto.getProjection().getMovie().getId()))
           .andExpect(
             jsonPath("$[0].projection.movie.title").value(defaultTicketDto.getProjection().getMovie().getTitle()))
           .andExpect(jsonPath("$[0].projection.movie.averageRating").value(
             defaultTicketDto.getProjection().getMovie().getAverageRating()))
           .andExpect(jsonPath("$[0].projection.movie.description").value(
             defaultTicketDto.getProjection().getMovie().getDescription()))
           .andExpect(jsonPath("$[0].projection.movie.releaseDate[0]").value(
             defaultTicketDto.getProjection().getMovie().getReleaseDate().getYear()))
           .andExpect(jsonPath("$[0].projection.movie.releaseDate[1]").value(
             defaultTicketDto.getProjection().getMovie().getReleaseDate().getMonthValue()))
           .andExpect(jsonPath("$[0].projection.movie.releaseDate[2]").value(
             defaultTicketDto.getProjection().getMovie().getReleaseDate().getDayOfMonth()))
           .andExpect(jsonPath("$[0].projection.movie.runtime").value(
             defaultTicketDto.getProjection().getMovie().getRuntime().getSeconds()))
           .andExpect(jsonPath("$[0].projection.movie.category").value(
             defaultTicketDto.getProjection().getMovie().getCategory()))
           .andExpect(
             jsonPath("$[0].projection.startTime[0]").value(defaultTicketDto.getProjection().getStartTime().getHour()))
           .andExpect(jsonPath("$[0].projection.startTime[1]").value(
             defaultTicketDto.getProjection().getStartTime().getMinute()));
  }
}