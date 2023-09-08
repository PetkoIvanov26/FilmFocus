package filmfocus.services;

import filmfocus.exceptions.DiscountAlreadyExistsException;
import filmfocus.exceptions.DiscountNotFoundException;
import filmfocus.mappers.DiscountMapper;
import filmfocus.models.dtos.DiscountDto;
import filmfocus.models.entities.Discount;
import filmfocus.models.requests.DiscountRequest;
import filmfocus.repositories.DiscountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static filmfocus.utils.constants.ExceptionMessages.DISCOUNT_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE;

@Service
public class DiscountService {

  private static final Logger log = LoggerFactory.getLogger(DiscountService.class);

  private final DiscountRepository discountRepository;
  private final DiscountMapper discountMapper;

  @Autowired
  public DiscountService(DiscountRepository discountRepository, DiscountMapper discountMapper) {
    this.discountRepository = discountRepository;
    this.discountMapper = discountMapper;
  }

  public Discount addDiscount(DiscountRequest request) {
    if (discountRepository.existsByType(request.getType()) || discountRepository.existsByCode(request.getCode())) {

      log.error(String.format("Exception thrown: %s", DISCOUNT_ALREADY_EXISTS_MESSAGE));

      throw new DiscountAlreadyExistsException(DISCOUNT_ALREADY_EXISTS_MESSAGE);
    }

    log.info(String.format("An attempt to add a discount with type %s", request.getType()));

    return discountRepository.save(new Discount(request.getType(), request.getCode(), request.getPercentage()));
  }

  public List<Discount> getAllDiscounts() {
    log.info("All discounts were requested from the database");

    return discountRepository.findAll();
  }

  public List<DiscountDto> getAllDiscountDtos() {
    log.info("An attempt to map all discounts to discount DTOs");

    return discountMapper.mapDiscountListToDiscountDtoList(getAllDiscounts());
  }

  public Discount getDiscountById(int id) {
    log.info(String.format("An attempt to get a discount with an id %d", id));

    return discountRepository.findById(id).orElseThrow(() -> {

      log.error(String.format("Exception caught: %s", DISCOUNT_NOT_FOUND_MESSAGE));

      throw new DiscountNotFoundException(DISCOUNT_NOT_FOUND_MESSAGE);
    });
  }

  public Discount getDiscountByCode(String code) {
    log.info(String.format("An attempt to get a discount with a code %s", code));

    return discountRepository.findByCode(code).orElseThrow(() -> {

      log.error(String.format("Exception caught: %s", DISCOUNT_NOT_FOUND_MESSAGE));

      throw new DiscountNotFoundException(DISCOUNT_NOT_FOUND_MESSAGE);
    });
  }

  public Discount getDiscountByType(String type) {
    log.info(String.format("An attempt to get a discount with a type %s", type));

    return discountRepository.findByType(type).orElseThrow(() -> {

      log.error(String.format("Exception caught: %s", DISCOUNT_NOT_FOUND_MESSAGE));

      throw new DiscountNotFoundException(DISCOUNT_NOT_FOUND_MESSAGE);
    });
  }

  public DiscountDto getDiscountDtoByType(String type) {
    log.info("An attempt to map discount to discount DTO");

    return discountMapper.mapDiscountToDiscountDto(getDiscountByType(type));
  }

  public DiscountDto updateDiscount(DiscountRequest request, int id) {
    DiscountDto discount = discountMapper.mapDiscountToDiscountDto(getDiscountById(id));

    discountRepository.save(new Discount(id, request.getType(), request.getCode(), request.getPercentage()));

    log.info(String.format("Discount with id %d was updated", id));

    return discount;
  }

  public DiscountDto deleteDiscount(int id) {
    Discount discount = getDiscountById(id);

    discountRepository.deleteById(id);

    log.info(String.format("Discount with id %d was deleted", id));

    return discountMapper.mapDiscountToDiscountDto(discount);
  }

  public double applyDiscount(double totalPrice, String discountCode) {
    return totalPrice - (totalPrice * getDiscountByCode(discountCode).getPercentage() / 100);
  }
}
