package filmfocus.testUtils.factories;

import filmfocus.models.dtos.DiscountDto;
import filmfocus.models.entities.Discount;
import filmfocus.models.requests.DiscountRequest;

import java.util.Collections;
import java.util.List;

import static filmfocus.testUtils.constants.DiscountConstants.CODE;
import static filmfocus.testUtils.constants.DiscountConstants.ID;
import static filmfocus.testUtils.constants.DiscountConstants.PERCENTAGE;
import static filmfocus.testUtils.constants.DiscountConstants.TYPE;
import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class DiscountFactory {

  private DiscountFactory() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static DiscountRequest getDefaultDiscountRequest() {
    return new DiscountRequest(TYPE, CODE, PERCENTAGE);
  }

  public static Discount getDefaultDiscount() {
    return new Discount(ID, TYPE, CODE, PERCENTAGE);
  }

  public static List<Discount> getDefaultDiscountList() {
    return Collections.singletonList(getDefaultDiscount());
  }

  public static DiscountDto getDefaultDiscountDto() {
    return new DiscountDto(ID, TYPE, CODE, PERCENTAGE);
  }

  public static List<DiscountDto> getDefaultDiscountDtoList() {
    return Collections.singletonList(getDefaultDiscountDto());
  }
}
