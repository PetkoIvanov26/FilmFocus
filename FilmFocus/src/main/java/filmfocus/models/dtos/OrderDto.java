package filmfocus.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Data
public class OrderDto {

  private int id;
  private LocalDate dateOfPurchase;
  private UserDto user;
  private List<TicketDto> tickets;
  private List<ItemDto> items;
  private double totalPrice;
}
