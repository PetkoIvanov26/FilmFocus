package filmfocus.mappers;

import filmfocus.models.dtos.CinemaDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static filmfocus.testUtils.constants.CinemaConstants.ID;
import static filmfocus.testUtils.constants.CinemaConstants.ADDRESS;
import static filmfocus.testUtils.constants.CinemaConstants.CITY;
import static filmfocus.testUtils.constants.CinemaConstants.AVERAGE_RATING;
import static filmfocus.testUtils.factories.CinemaFactory.getDefaultCinema;
import static filmfocus.testUtils.factories.CinemaFactory.getDefaultCinemaList;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CinemaMapperTest {

    @InjectMocks
    private CinemaMapper cinemaMapper;

    @Test
    public void testMapCinemaToCinemaDto_success() {
        CinemaDto cinemaDto = cinemaMapper.mapCinemaToCinemaDto(getDefaultCinema());

        assertEquals(cinemaDto.getId(), ID);
        assertEquals(cinemaDto.getAddress(), ADDRESS);
        assertEquals(cinemaDto.getCity(), CITY);
        assertEquals(cinemaDto.getAverageRating(), AVERAGE_RATING, 0.0);
    }

    @Test
    public void testMapCinemaToCinemaDtoList() {
        List<CinemaDto> cinemaDtos = cinemaMapper.mapCinemaToCinemaDtoList(getDefaultCinemaList());

        CinemaDto cinemaDto = cinemaDtos.get(0);
        assertEquals(cinemaDto.getId(), ID);
        assertEquals(cinemaDto.getAddress(), ADDRESS);
        assertEquals(cinemaDto.getCity(), CITY);
        assertEquals(cinemaDto.getAverageRating(), AVERAGE_RATING, 0.0);
    }
}