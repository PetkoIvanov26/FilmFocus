package filmfocus.services;

import filmfocus.exceptions.HallNotFoundException;
import filmfocus.mappers.HallMapper;
import filmfocus.models.dtos.HallDto;
import filmfocus.models.entities.Cinema;
import filmfocus.models.entities.Hall;
import filmfocus.repositories.HallRepository;
import filmfocus.testUtils.factories.CinemaFactory;
import filmfocus.testUtils.factories.HallFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static filmfocus.testUtils.constants.HallConstants.ID;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HallServiceTest {

    @Mock
    private HallRepository hallRepository;

    @Mock
    private HallMapper hallMapper;

    @Mock
    private CinemaService cinemaService;

    @InjectMocks
    private HallService hallService;

    @Test
    public void testAddHall_noExceptions_success() {
        Hall expected = HallFactory.getDefaultHall();
        when(hallRepository.save(any())).thenReturn(expected);
        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());

        Hall hall = hallService.addHall(HallFactory.getDefaultHallRequest());

        assertEquals(expected, hall);
    }

    @Test
    public void testGetHallByCinemaId_cinemaFound_success() {
        Cinema cinema = CinemaFactory.getDefaultCinema();
        List<HallDto> expected = HallFactory.getDefaultHallDtoList();

        when(cinemaService.getCinemaById(anyInt())).thenReturn(cinema);
        when(hallMapper.mapHallListToHallDtoList(any())).thenReturn(expected);
        when(hallRepository.findAllByCinemaId(anyInt())).thenReturn(HallFactory.getDefaultHallList());

        List<HallDto> result = hallService.getHallsByCinemaId(cinema.getId());

        assertEquals(expected, result);
    }

    @Test
    public void testGetHallById_hallFound_success() {
        Hall expected = HallFactory.getDefaultHall();
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        Hall hall = hallService.getHallById(ID);

        assertEquals(expected, hall);
    }

    @Test(expected = HallNotFoundException.class)
    public void testGetHallById_hallNotFound_throwsHallNotFoundException() {
        when(hallRepository.findById(anyInt())).thenReturn(Optional.empty());
        hallService.getHallById(ID);
    }

    @Test
    public void testGetHallDtoById_hallDtoFound_success() {
        HallDto expected = HallFactory.getDefaultHallDto();
        when(hallMapper.mapHallToHallDto(any())).thenReturn(expected);
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(new Hall()));

        HallDto hall = hallService.getHallDtoById(ID);

        assertEquals(expected, hall);
    }

    @Test
    public void testUpdateHall_hallUpdated_success() {
        HallDto expected = HallFactory.getDefaultHallDto();
        when(hallMapper.mapHallToHallDto(any())).thenReturn(expected);
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(HallFactory.getDefaultHall()));
        when(hallRepository.save(any())).thenReturn(HallFactory.getDefaultHall());

        HallDto hall = hallService.updateHall(HallFactory.getDefaultHallRequest(), ID);

        assertEquals(expected, hall);
    }

    @Test
    public void testDeleteHall_hallDeleted_success() {
        HallDto expected = HallFactory.getDefaultHallDto();
        when(hallMapper.mapHallToHallDto(any())).thenReturn(expected);
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(HallFactory.getDefaultHall()));

        HallDto hall = hallService.deleteHall(ID);

        assertEquals(expected, hall);
    }
}

