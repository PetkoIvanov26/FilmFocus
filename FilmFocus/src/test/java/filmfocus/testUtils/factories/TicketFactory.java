package filmfocus.testUtils.factories;

import filmfocus.models.dtos.TicketDto;
import filmfocus.models.entities.Ticket;
import filmfocus.models.requests.TicketRequest;

import java.util.Collections;
import java.util.List;

import static filmfocus.testUtils.constants.TicketConstants.ID;
import static filmfocus.testUtils.constants.TicketConstants.DATE_OF_PURCHASE;
import static filmfocus.testUtils.factories.ProjectionFactory.getDefaultProjection;
import static filmfocus.testUtils.factories.ProjectionFactory.getDefaultProjectionDto;
import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class TicketFactory {

  private TicketFactory() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static TicketRequest getDefaultTicketRequest() {
    return new TicketRequest(ID);
  }

  public static Ticket getDefaultTicket() {
    return new Ticket(ID, DATE_OF_PURCHASE, getDefaultProjection());
  }

  public static List<Ticket> getDefaultTicketList() {
    return Collections.singletonList(getDefaultTicket());
  }

  public static TicketDto getDefaultTicketDto() {
    return new TicketDto(ID, DATE_OF_PURCHASE, getDefaultProjectionDto());
  }

  public static List<TicketDto> getDefaultTicketDtoList() {
    return Collections.singletonList(getDefaultTicketDto());
  }

  public static List<Integer> getDefaultIdList() {
    return Collections.singletonList(ID);
  }
}




