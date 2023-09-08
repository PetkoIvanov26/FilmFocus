package filmfocus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import filmfocus.services.HallService;
import filmfocus.testUtils.factories.CinemaFactory;
import filmfocus.testUtils.factories.HallFactory;
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

import static filmfocus.testUtils.constants.HallConstants.CAPACITY;
import static filmfocus.testUtils.constants.HallConstants.ID;
import static filmfocus.utils.constants.URIConstants.CINEMAS_ID_HALLS_PATH;
import static filmfocus.utils.constants.URIConstants.HALLS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.HALLS_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
public class HallControllerTest {

  private static final String RETURN_OLD = "returnOld";
  private final static ObjectMapper objectMapper = new ObjectMapper();

  private MockMvc mockMvc;

  @Mock
  private HallService hallService;

  @InjectMocks
  private HallController hallController;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(hallController)
      .build();
  }

  @Test
  public void testAddHall_hallAdded_success() throws Exception {
    String json = objectMapper.writeValueAsString(HallFactory.getDefaultHallRequest());
    when(hallService.addHall(any())).thenReturn(HallFactory.getDefaultHall());

    mockMvc.perform(post(HALLS_PATH)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().stringValues("Location", HALLS_PATH + "/" + ID));
  }

  @Test
  public void testGetHallsByCinemaId_noExceptions_success() throws Exception {
    when(hallService.getHallsByCinemaId(anyInt())).thenReturn(HallFactory.getDefaultHallDtoList());

    mockMvc.perform(get(CINEMAS_ID_HALLS_PATH, ID))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].capacity").value(CAPACITY))
           .andExpect(jsonPath("$[0].cinema.id").value(CinemaFactory.getDefaultCinemaDto().getId()))
           .andExpect(jsonPath("$[0].cinema.address").value(CinemaFactory.getDefaultCinemaDto().getAddress()))
           .andExpect(jsonPath("$[0].cinema.city").value(CinemaFactory.getDefaultCinemaDto().getCity()));
  }

  @Test
  public void testUpdateHall_returnOldTrue_success() throws Exception {
    when(hallService.updateHall(any(), anyInt())).thenReturn(HallFactory.getDefaultHallDto());
    String json = objectMapper.writeValueAsString(HallFactory.getDefaultHallRequest());

    mockMvc.perform(put(HALLS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.capacity").value(CAPACITY))
           .andExpect(jsonPath("$.cinema.id").value(CinemaFactory.getDefaultCinemaDto().getId()))
           .andExpect(jsonPath("$.cinema.address").value(CinemaFactory.getDefaultCinemaDto().getAddress()))
           .andExpect(jsonPath("$.cinema.city").value(CinemaFactory.getDefaultCinemaDto().getCity()));
  }

  @Test
  public void testUpdateHall_returnOldFalse_success() throws Exception {
    when(hallService.updateHall(any(), anyInt())).thenReturn(HallFactory.getDefaultHallDto());
    String json = objectMapper.writeValueAsString(HallFactory.getDefaultHallRequest());

    mockMvc.perform(put(HALLS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteHall_returnOldTrue_success() throws Exception {
    when(hallService.deleteHall(anyInt())).thenReturn(HallFactory.getDefaultHallDto());

    mockMvc.perform(delete(HALLS_ID_PATH, ID)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.capacity").value(CAPACITY))
           .andExpect(jsonPath("$.cinema.id").value(CinemaFactory.getDefaultCinemaDto().getId()))
           .andExpect(jsonPath("$.cinema.address").value(CinemaFactory.getDefaultCinemaDto().getAddress()))
           .andExpect(jsonPath("$.cinema.city").value(CinemaFactory.getDefaultCinemaDto().getCity()));
  }

  @Test
  public void testDeleteHall_returnOldFalse_success() throws Exception {
    when(hallService.deleteHall(anyInt())).thenReturn(HallFactory.getDefaultHallDto());

    mockMvc.perform(delete(HALLS_ID_PATH, ID))
           .andExpect(status().isNoContent());
  }
}