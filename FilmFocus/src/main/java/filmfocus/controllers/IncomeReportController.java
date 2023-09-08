package filmfocus.controllers;

import filmfocus.services.IncomeReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static filmfocus.utils.constants.URIConstants.REPORTS_CINEMAS_ID_INCOMES_PATH;
import static filmfocus.utils.constants.URIConstants.REPORTS_HALLS_ID_INCOMES_PATH;
import static filmfocus.utils.constants.URIConstants.REPORTS_ITEMS_ID_INCOMES_PATH;
import static filmfocus.utils.constants.URIConstants.REPORTS_MOVIES_ID_INCOMES_PATH;
import static filmfocus.utils.constants.URIConstants.REPORTS_USERS_ID_INCOMES_PATH;

@RestController
public class IncomeReportController {

  private static final Logger log = LoggerFactory.getLogger(IncomeReportController.class);

  private final IncomeReportService incomeReportService;

  @Autowired
  public IncomeReportController(IncomeReportService incomeReportService) {
    this.incomeReportService = incomeReportService;
  }

  @GetMapping(REPORTS_CINEMAS_ID_INCOMES_PATH)
  public ResponseEntity<Double> getAllIncomesByCinemaId(
    @PathVariable int id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

    double cinemaIncome = incomeReportService.getAllIncomesByCinemaId(id, startDate, endDate);
    log.info(String.format("All incomes by cinema id %d report requested", id));

    return ResponseEntity.ok(cinemaIncome);
  }

  @GetMapping(REPORTS_HALLS_ID_INCOMES_PATH)
  public ResponseEntity<Double> getAllIncomesByHallId(
    @PathVariable int id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

    double hallIncome = incomeReportService.getAllIncomesByHallId(id, startDate, endDate);
    log.info(String.format("All incomes by hall id %d report requested", id));

    return ResponseEntity.ok(hallIncome);
  }

  @GetMapping(value = REPORTS_ITEMS_ID_INCOMES_PATH)
  public ResponseEntity<Double> getAllIncomesByItemId(
    @PathVariable int id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

    double itemIncome = incomeReportService.getAllIncomesByItemId(id, startDate, endDate);
    log.info(String.format("All incomes by hall id %d report requested", id));

    return ResponseEntity.ok(itemIncome);
  }

  @GetMapping(REPORTS_MOVIES_ID_INCOMES_PATH)
  public ResponseEntity<Double> getAllIncomesByMovieId(
    @PathVariable int id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

    double movieIncome = incomeReportService.getAllIncomesByMovieId(id, startDate, endDate);
    log.info(String.format("All incomes by hall id %d report requested", id));

    return ResponseEntity.ok(movieIncome);
  }

  @GetMapping(REPORTS_USERS_ID_INCOMES_PATH)
  public ResponseEntity<Double> getAllIncomesByUserId(
    @PathVariable int id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

    double userIncome = incomeReportService.getAllIncomesByUserId(id, startDate, endDate);
    log.info(String.format("All incomes by hall id %d report requested", id));

    return ResponseEntity.ok(userIncome);
  }
}
