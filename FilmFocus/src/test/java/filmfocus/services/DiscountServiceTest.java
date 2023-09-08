package filmfocus.services;

import filmfocus.exceptions.DiscountAlreadyExistsException;
import filmfocus.exceptions.DiscountNotFoundException;
import filmfocus.mappers.DiscountMapper;
import filmfocus.models.dtos.DiscountDto;
import filmfocus.models.entities.Discount;
import filmfocus.repositories.DiscountRepository;
import filmfocus.testUtils.factories.DiscountFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static filmfocus.testUtils.constants.DiscountConstants.CODE;
import static filmfocus.testUtils.constants.DiscountConstants.ID;
import static filmfocus.testUtils.constants.DiscountConstants.TYPE;
import static filmfocus.testUtils.constants.OrderConstants.TOTAL_PRICE;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiscountServiceTest {

  @Mock
  private DiscountRepository discountRepository;

  @Mock
  private DiscountMapper discountMapper;

  @InjectMocks
  private DiscountService discountService;

  @Test
  public void testAddDiscount_noExceptions_success() {
    Discount expected = DiscountFactory.getDefaultDiscount();
    when(discountRepository.save(any())).thenReturn(expected);

    Discount discount = discountService.addDiscount(DiscountFactory.getDefaultDiscountRequest());

    assertEquals(expected, discount);
  }

  @Test(expected = DiscountAlreadyExistsException.class)
  public void testAddDiscount_discountAlreadyExists_throwsDiscountAlreadyExistsException() {
    when(discountRepository.existsByType(any())).thenReturn(true);

    discountService.addDiscount(DiscountFactory.getDefaultDiscountRequest());
  }

  @Test
  public void testGetAllDiscounts_discountsFound_success() {
    List<Discount> expected = DiscountFactory.getDefaultDiscountList();
    when(discountRepository.findAll()).thenReturn(expected);

    List<Discount> result = discountService.getAllDiscounts();

    assertEquals(expected, result);
  }

  @Test
  public void testGetAllDiscountsDto_discountsFound_success() {
    List<DiscountDto> expected = DiscountFactory.getDefaultDiscountDtoList();
    when(discountRepository.findAll()).thenReturn(DiscountFactory.getDefaultDiscountList());
    when(discountMapper.mapDiscountListToDiscountDtoList(any())).thenReturn(expected);

    List<DiscountDto> result = discountService.getAllDiscountDtos();

    assertEquals(expected, result);
  }

  @Test
  public void testGetDiscountById_discountFound_success() {
    Discount expected = DiscountFactory.getDefaultDiscount();
    when(discountRepository.findById(anyInt())).thenReturn(Optional.of(expected));

    Discount discount = discountService.getDiscountById(ID);

    assertEquals(expected, discount);
  }

  @Test(expected = DiscountNotFoundException.class)
  public void testGetDiscountById_discountNotFound_throwsDiscountNotFoundException() {
    discountService.getDiscountById(ID);
  }

  @Test
  public void testGetDiscountByType_discountFound_success() {
    Discount expected = DiscountFactory.getDefaultDiscount();
    when(discountRepository.findByType(anyString())).thenReturn(Optional.of(expected));

    Discount discount = discountService.getDiscountByType(TYPE);

    assertEquals(expected, discount);
  }

  @Test(expected = DiscountNotFoundException.class)
  public void testGetDiscountByType_discountNotFound_throwDiscountNotFoundException() {
    when(discountRepository.findByType(anyString())).thenReturn(Optional.empty());

    discountService.getDiscountByType(TYPE);
  }

  @Test
  public void testGetDiscountByCode_discountFound_success() {
    Discount expected = DiscountFactory.getDefaultDiscount();
    when(discountRepository.findByCode(anyString())).thenReturn(Optional.of(expected));

    Discount discount = discountService.getDiscountByCode(CODE);

    assertEquals(expected, discount);
  }

  @Test(expected = DiscountNotFoundException.class)
  public void testGetDiscountByCode_discountNotFound_throwDiscountNotFoundException() {
    when(discountRepository.findByCode(anyString())).thenReturn(Optional.empty());

    discountService.getDiscountByCode(CODE);
  }

  @Test
  public void testGetDiscountDtoByType_discountFound_success() {
    DiscountDto expected = DiscountFactory.getDefaultDiscountDto();
    when(discountRepository.findByType(anyString())).thenReturn(Optional.of(DiscountFactory.getDefaultDiscount()));
    when(discountMapper.mapDiscountToDiscountDto(any())).thenReturn(expected);

    DiscountDto discountDto = discountService.getDiscountDtoByType(TYPE);

    assertEquals(expected, discountDto);
  }

  @Test
  public void testUpdateDiscount_discountUpdated_success() {
    DiscountDto expected = DiscountFactory.getDefaultDiscountDto();
    when(discountMapper.mapDiscountToDiscountDto(any())).thenReturn(expected);
    when(discountRepository.findById(anyInt())).thenReturn(Optional.of(new Discount()));
    when(discountRepository.save(any())).thenReturn(DiscountFactory.getDefaultDiscount());

    DiscountDto discountDto = discountService.updateDiscount(DiscountFactory.getDefaultDiscountRequest(), ID);

    assertEquals(expected, discountDto);
  }

  @Test
  public void testDeleteDiscount_discountDeleted_success() {
    DiscountDto expected = DiscountFactory.getDefaultDiscountDto();
    when(discountMapper.mapDiscountToDiscountDto(any())).thenReturn(expected);
    when(discountRepository.findById(anyInt())).thenReturn(Optional.of(DiscountFactory.getDefaultDiscount()));

    DiscountDto discountDto = discountService.deleteDiscount(ID);

    assertEquals(expected, discountDto);
  }

  @Test
  public void testApplyDiscount_success() {
    double expected = 90;
    Discount discount = DiscountFactory.getDefaultDiscount();
    when(discountRepository.findByCode(anyString())).thenReturn(Optional.of(discount));

    double result = discountService.applyDiscount(TOTAL_PRICE, CODE);

    assertEquals(expected, result, 0.0);
  }
}