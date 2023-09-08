package filmfocus.testUtils.factories;

import filmfocus.models.dtos.CinemaDto;
import filmfocus.models.entities.Cinema;
import filmfocus.models.requests.CinemaRequest;

import java.util.Collections;
import java.util.List;

import static filmfocus.testUtils.constants.CinemaConstants.ADDRESS;
import static filmfocus.testUtils.constants.CinemaConstants.CITY;
import static filmfocus.testUtils.constants.CinemaConstants.ID;
import static filmfocus.testUtils.constants.MovieConstants.RATING;
import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class CinemaFactory {

  private CinemaFactory() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static CinemaRequest getDefaultCinemaRequest() {
    return new CinemaRequest(ADDRESS, CITY);
  }

  public static Cinema getDefaultCinema() {
    return new Cinema(ID, ADDRESS, CITY, RATING);
  }

  public static CinemaDto getDefaultCinemaDto() {
    return new CinemaDto(ID, ADDRESS, CITY, RATING);
  }

  public static List<CinemaDto> getDefaultCinemaDtoList() {
    return Collections.singletonList(getDefaultCinemaDto());
  }

  public static List<Cinema> getDefaultCinemaList() {
    return Collections.singletonList(getDefaultCinema());
  }
}