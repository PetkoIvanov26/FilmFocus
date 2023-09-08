package filmfocus.mappers;

import filmfocus.models.dtos.HallDto;
import filmfocus.testUtils.factories.CinemaFactory;
import filmfocus.testUtils.factories.HallFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static filmfocus.testUtils.constants.HallConstants.CAPACITY;
import static filmfocus.testUtils.constants.HallConstants.ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HallMapperTest {

    @Mock
    private CinemaMapper cinemaMapper;

    @InjectMocks
    private HallMapper hallMapper;

    @Test
    public void testMapHallToHallDto_success() {
        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(CinemaFactory.getDefaultCinemaDto());

        HallDto hallDto = hallMapper.mapHallToHallDto(HallFactory.getDefaultHall());

        assertEquals(ID, hallDto.getId());
        assertEquals(CAPACITY, hallDto.getCapacity());
        assertNotNull(hallDto.getCinema());
    }

    @Test
    public void testMapHallListToHallDtoList_success() {
        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(CinemaFactory.getDefaultCinemaDto());

        List<HallDto> hallDtoList = hallMapper.mapHallListToHallDtoList(HallFactory.getDefaultHallList());
        HallDto hallDto = hallDtoList.get(0);

        assertEquals(ID, hallDto.getId());
        assertEquals(CAPACITY, hallDto.getCapacity());
        assertNotNull(hallDto.getCinema());
    }
}