package filmfocus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import filmfocus.services.CinemaService;
import filmfocus.testUtils.factories.CinemaFactory;
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

import static filmfocus.testUtils.constants.CinemaConstants.ADDRESS;
import static filmfocus.testUtils.constants.CinemaConstants.CITY;
import static filmfocus.testUtils.constants.CinemaConstants.ID;
import static filmfocus.utils.constants.URIConstants.CINEMAS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.CINEMAS_PATH;
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
public class CinemaControllerTest {

  private static final String RETURN_OLD = "returnOld";
  private final static ObjectMapper objectMapper = new ObjectMapper();
  private MockMvc mockMvc;

  @Mock
  private CinemaService cinemaService;

  @InjectMocks
  private CinemaController cinemaController;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(cinemaController)
      .build();
  }

  @Test
  public void testAddCinema_cinemaAdded_success() throws Exception {
    String json = objectMapper.writeValueAsString(CinemaFactory.getDefaultCinemaRequest());
    when(cinemaService.addCinema(any())).thenReturn(CinemaFactory.getDefaultCinema());

    mockMvc.perform(post(CINEMAS_PATH)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", CINEMAS_PATH + "/" + ID));
  }

  @Test
  public void testGetCinemas_getCinemaByCityAndAddress_success() throws Exception {
    when(cinemaService.getAllCinemas(CITY, ADDRESS)).thenReturn(CinemaFactory.getDefaultCinemaDtoList());

    mockMvc.perform(get(CINEMAS_PATH)
                      .param("city", CITY)
                      .param("address", ADDRESS))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].city").value(CITY))
           .andExpect(jsonPath("$[0].address").value(ADDRESS));
  }

  @Test
  public void testGetCinemas_getCinemaByCity_success() throws Exception {
    when(cinemaService.getAllCinemas(CITY, null)).thenReturn(CinemaFactory.getDefaultCinemaDtoList());

    mockMvc.perform(get(CINEMAS_PATH)
                      .param("city", CITY))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].city").value(CITY))
           .andExpect(jsonPath("$[0].address").value(ADDRESS));
  }

  @Test
  public void testGetCinemas_getCinemaByAddress_success() throws Exception {
    when(cinemaService.getAllCinemas(null, ADDRESS)).thenReturn(CinemaFactory.getDefaultCinemaDtoList());

    mockMvc.perform(get(CINEMAS_PATH)
                      .param("address", ADDRESS))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].city").value(CITY))
           .andExpect(jsonPath("$[0].address").value(ADDRESS));
  }

  @Test
  public void testUpdateCinema_returnOldTrue_success() throws Exception {
    when(cinemaService.updateCinema(any(), anyInt())).thenReturn(CinemaFactory.getDefaultCinemaDto());
    String json = objectMapper.writeValueAsString(CinemaFactory.getDefaultCinemaRequest());

    mockMvc.perform(put(CINEMAS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json)
                      .param(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.city").value(CITY))
           .andExpect(jsonPath("$.address").value(ADDRESS));
  }

  @Test
  public void testUpdateCinema_returnOldFalse_success() throws Exception {
    when(cinemaService.updateCinema(any(), anyInt())).thenReturn(CinemaFactory.getDefaultCinemaDto());
    String json = objectMapper.writeValueAsString(CinemaFactory.getDefaultCinemaRequest());

    mockMvc.perform(put(CINEMAS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteCinema_returnOldTrue_success() throws Exception {
    when(cinemaService.deleteCinema(anyInt())).thenReturn(CinemaFactory.getDefaultCinemaDto());

    mockMvc.perform(delete(CINEMAS_ID_PATH, ID)
                      .param(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.city").value(CITY))
           .andExpect(jsonPath("$.address").value(ADDRESS));
  }

  @Test
  public void testDeleteCinema_returnOldFalse_success() throws Exception {
    when(cinemaService.deleteCinema(anyInt())).thenReturn(CinemaFactory.getDefaultCinemaDto());

    mockMvc.perform(delete(CINEMAS_ID_PATH, ID))
           .andExpect(status().isNoContent());
  }
}
