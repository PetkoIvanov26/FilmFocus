package filmfocus.testUtils.factories;

import filmfocus.models.dtos.HallDto;
import filmfocus.models.entities.Hall;
import filmfocus.models.requests.HallRequest;

import java.util.Collections;
import java.util.List;

import static filmfocus.testUtils.constants.HallConstants.CAPACITY;
import static filmfocus.testUtils.constants.HallConstants.ID;
import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class HallFactory {

  private HallFactory() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static HallRequest getDefaultHallRequest() {
    return new HallRequest(CAPACITY, ID);
  }

  public static Hall getDefaultHall() {
    return new Hall(ID, CAPACITY, CinemaFactory.getDefaultCinema());
  }

  public static List<Hall> getDefaultHallList() {
    return Collections.singletonList(getDefaultHall());
  }

  public static HallDto getDefaultHallDto() {
    return new HallDto(ID, CAPACITY, CinemaFactory.getDefaultCinemaDto());
  }

  public static List<HallDto> getDefaultHallDtoList() {
    return Collections.singletonList(getDefaultHallDto());
  }
}