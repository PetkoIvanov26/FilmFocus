package filmfocus.controllers;

import filmfocus.services.IncomeReportService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static filmfocus.testUtils.constants.ReportConstants.END_DATE;
import static filmfocus.testUtils.constants.ReportConstants.ID;
import static filmfocus.testUtils.constants.ReportConstants.INCOMES;
import static filmfocus.testUtils.constants.ReportConstants.START_DATE;
import static filmfocus.utils.constants.URIConstants.REPORTS_CINEMAS_ID_INCOMES_PATH;
import static filmfocus.utils.constants.URIConstants.REPORTS_HALLS_ID_INCOMES_PATH;
import static filmfocus.utils.constants.URIConstants.REPORTS_ITEMS_ID_INCOMES_PATH;
import static filmfocus.utils.constants.URIConstants.REPORTS_MOVIES_ID_INCOMES_PATH;
import static filmfocus.utils.constants.URIConstants.REPORTS_USERS_ID_INCOMES_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
public class IncomeReportControllerTest {

  private MockMvc mockMvc;

  @Mock
  private IncomeReportService incomeReportService;

  @InjectMocks
  private IncomeReportController incomeReportController;

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(incomeReportController)
      .build();
  }

  @Test
  public void testGetAllIncomesByCinemaId_doubleReturned_success() throws Exception {
    when(incomeReportService.getAllIncomesByCinemaId(anyInt(), any(), any())).thenReturn(INCOMES);

    mockMvc.perform(get(REPORTS_CINEMAS_ID_INCOMES_PATH, ID)
                      .queryParam("startDate", String.valueOf(START_DATE))
                      .queryParam("endDate", String.valueOf(END_DATE)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").value(INCOMES));
  }

  @Test
  public void testGetAllIncomesByHallId_doubleReturned_success() throws Exception {
    when(incomeReportService.getAllIncomesByHallId(anyInt(), any(), any())).thenReturn(INCOMES);

    mockMvc.perform(get(REPORTS_HALLS_ID_INCOMES_PATH, ID)
                      .queryParam("startDate", String.valueOf(START_DATE))
                      .queryParam("endDate", String.valueOf(END_DATE)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").value(INCOMES));
  }

  @Test
  public void testGetAllIncomesByItemId_doubleReturned_success() throws Exception {
    when(incomeReportService.getAllIncomesByItemId(anyInt(), any(), any())).thenReturn(INCOMES);

    mockMvc.perform(get(REPORTS_ITEMS_ID_INCOMES_PATH, ID)
                      .queryParam("startDate", String.valueOf(START_DATE))
                      .queryParam("endDate", String.valueOf(END_DATE)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").value(INCOMES));
  }

  @Test
  public void testGetAllIncomesByMovieId_doubleReturned_success() throws Exception {
    when(incomeReportService.getAllIncomesByMovieId(anyInt(), any(), any())).thenReturn(INCOMES);

    mockMvc.perform(get(REPORTS_MOVIES_ID_INCOMES_PATH, ID)
                      .queryParam("startDate", String.valueOf(START_DATE))
                      .queryParam("endDate", String.valueOf(END_DATE)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").value(INCOMES));
  }

  @Test
  public void testGetAllIncomesByUserId_doubleReturned_success() throws Exception {
    when(incomeReportService.getAllIncomesByUserId(anyInt(), any(), any())).thenReturn(INCOMES);

    mockMvc.perform(get(REPORTS_USERS_ID_INCOMES_PATH, ID)
                      .queryParam("startDate", String.valueOf(START_DATE))
                      .queryParam("endDate", String.valueOf(END_DATE)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").value(INCOMES));
  }
}