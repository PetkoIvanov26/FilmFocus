package filmfocus.controllers;

import filmfocus.models.dtos.DiscountDto;
import filmfocus.models.entities.Discount;
import filmfocus.models.requests.DiscountRequest;
import filmfocus.services.DiscountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static filmfocus.utils.constants.URIConstants.DISCOUNTS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.DISCOUNTS_PATH;

@RestController
public class DiscountController {

  private static final Logger log = LoggerFactory.getLogger(DiscountController.class);

  private final DiscountService discountService;

  @Autowired
  public DiscountController(DiscountService discountService) {
    this.discountService = discountService;
  }

  @PostMapping(DISCOUNTS_PATH)
  public ResponseEntity<Void> addDiscount(@RequestBody @Valid DiscountRequest discountRequest) {
    Discount discount = discountService.addDiscount(discountRequest);
    log.info("A request for a discount to be added has been submitted");

    URI location = UriComponentsBuilder
      .fromUriString(DISCOUNTS_ID_PATH)
      .buildAndExpand(discount.getId())
      .toUri();

    return ResponseEntity.created(location).build();
  }

  @GetMapping(DISCOUNTS_PATH)
  public ResponseEntity<List<DiscountDto>> getAllDiscounts() {
    List<DiscountDto> discounts = discountService.getAllDiscountDtos();
    log.info("All discounts were requested from the database");

    return ResponseEntity.ok(discounts);
  }

  @GetMapping(value = DISCOUNTS_PATH, params = "type")
  public ResponseEntity<DiscountDto> getDiscountByType(@RequestParam String type) {
    DiscountDto discount = discountService.getDiscountDtoByType(type);
    log.info(String.format("Discount with type %s has been requested from database", type));

    return ResponseEntity.ok(discount);
  }

  @PutMapping(DISCOUNTS_ID_PATH)
  public ResponseEntity<DiscountDto> updateDiscount(
    @RequestBody @Valid DiscountRequest request,
    @PathVariable int id,
    @RequestParam(required = false) boolean returnOld) {

    DiscountDto discountDto = discountService.updateDiscount(request, id);
    log.info(String.format("Discount with id %d was updated", id));

    return returnOld ? ResponseEntity.ok(discountDto) : ResponseEntity.noContent().build();
  }

  @DeleteMapping(DISCOUNTS_ID_PATH)
  public ResponseEntity<DiscountDto> deleteDiscount(
    @PathVariable int id, @RequestParam(required = false) boolean returnOld) {

    DiscountDto discountDto = discountService.deleteDiscount(id);
    log.info(String.format("Discount with id %d was deleted", id));

    return returnOld ? ResponseEntity.ok(discountDto) : ResponseEntity.noContent().build();
  }
}