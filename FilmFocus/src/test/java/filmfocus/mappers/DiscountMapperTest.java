package filmfocus.mappers;

import filmfocus.models.dtos.DiscountDto;
import filmfocus.testUtils.factories.DiscountFactory;
import org.junit.Test;

import java.util.List;

import static filmfocus.testUtils.constants.DiscountConstants.CODE;
import static filmfocus.testUtils.constants.DiscountConstants.ID;
import static filmfocus.testUtils.constants.DiscountConstants.PERCENTAGE;
import static filmfocus.testUtils.constants.DiscountConstants.TYPE;
import static org.junit.Assert.assertEquals;

public class DiscountMapperTest {

    private final DiscountMapper discountMapper = new DiscountMapper();

    @Test
    public void testMapDiscountToDiscountDto_success() {
        DiscountDto discountDto = discountMapper.mapDiscountToDiscountDto(DiscountFactory.getDefaultDiscount());

        assertEquals(discountDto.getId(), ID);
        assertEquals(discountDto.getType(), TYPE);
        assertEquals(discountDto.getCode(), CODE);
        assertEquals(discountDto.getPercentage(), PERCENTAGE);
    }

    @Test
    public void testMapDiscountListToDiscountDtoList_success() {
        List<DiscountDto> discountDtos =
                discountMapper.mapDiscountListToDiscountDtoList(DiscountFactory.getDefaultDiscountList());

        assertEquals(1, discountDtos.size());
        DiscountDto discountDto = discountDtos.get(0);
        assertEquals(discountDto.getId(), ID);
        assertEquals(discountDto.getType(), TYPE);
        assertEquals(discountDto.getCode(), CODE);
        assertEquals(discountDto.getPercentage(), PERCENTAGE);
    }
}