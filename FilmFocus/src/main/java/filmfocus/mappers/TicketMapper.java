package filmfocus.mappers;

import filmfocus.models.dtos.TicketDto;
import filmfocus.models.entities.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketMapper {

  private static final Logger log = LoggerFactory.getLogger(TicketMapper.class);

  private final ProjectionMapper projectionMapper;

  @Autowired
  public TicketMapper(ProjectionMapper projectionMapper) {
    this.projectionMapper = projectionMapper;
  }

  public TicketDto mapTicketToTicketDto(Ticket ticket) {
    log.info(String.format("The ticket with an id %d is being mapped to a ticket DTO", ticket.getId()));
    return new TicketDto(ticket.getId(), ticket.getDateOfPurchase(),
                         projectionMapper.mapProjectionToProjectionDto(ticket.getProjection()));
  }

  public List<TicketDto> mapTicketToDtoList(List<Ticket> tickets) {
    return tickets.stream()
                  .map(this::mapTicketToTicketDto)
                  .collect(Collectors.toList());
  }
}
