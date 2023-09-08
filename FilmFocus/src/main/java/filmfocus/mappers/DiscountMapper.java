package filmfocus.mappers;

import filmfocus.models.dtos.DiscountDto;
import filmfocus.models.entities.Discount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DiscountMapper {

  private static final Logger log = LoggerFactory.getLogger(DiscountMapper.class);

  public DiscountDto mapDiscountToDiscountDto(Discount discount) {
    log.info(String.format("The discount %s is being mapped to a discount DTO", discount.getType()));
    return new DiscountDto(discount.getId(), discount.getType(), discount.getCode(), discount.getPercentage());
  }

  public List<DiscountDto> mapDiscountListToDiscountDtoList(List<Discount> discounts) {
    return discounts.stream()
                    .map(this::mapDiscountToDiscountDto)
                    .collect(Collectors.toList());
  }
}
