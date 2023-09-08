package filmfocus.services;

import filmfocus.exceptions.CinemaAlreadyExistsException;
import filmfocus.exceptions.CinemaNotFoundException;
import filmfocus.mappers.CinemaMapper;
import filmfocus.models.dtos.CinemaDto;
import filmfocus.models.entities.Cinema;
import filmfocus.repositories.CinemaRepository;
import filmfocus.testUtils.factories.CinemaFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static filmfocus.testUtils.constants.CinemaConstants.ADDRESS;
import static filmfocus.testUtils.constants.CinemaConstants.CITY;
import static filmfocus.testUtils.constants.CinemaConstants.ID;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CinemaServiceTest {

  @Mock
  private CinemaRepository cinemaRepository;

  @Mock
  private CinemaMapper cinemaMapper;

  @InjectMocks
  private CinemaService cinemaService;

  @Test
  public void testAddCinema_noExceptions_success() {
    Cinema expected = CinemaFactory.getDefaultCinema();
    when(cinemaRepository.save(any())).thenReturn(expected);

    Cinema cinema = cinemaService.addCinema(CinemaFactory.getDefaultCinemaRequest());

    assertEquals(expected, cinema);
  }

  @Test(expected = CinemaAlreadyExistsException.class)
  public void testAddCinema_cinemaExists_success() {
    when(cinemaRepository.findByCityAndAddress(anyString(), anyString())).thenReturn(Optional.of(new Cinema()));

    cinemaService.addCinema(CinemaFactory.getDefaultCinemaRequest());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetAllCinemas_cityAndAddressNull_throwsIllegalArgumentException() {
    cinemaService.getAllCinemas(null, null);
  }

  @Test
  public void testGetAllCinemas_cityAndAddressNotNull_success() {
    List<CinemaDto> expected = CinemaFactory.getDefaultCinemaDtoList();
    when(cinemaMapper.mapCinemaToCinemaDtoList(any())).thenReturn(expected);
    when(cinemaRepository.findAllByCityAndAddress(anyString(), anyString())).thenReturn(
      CinemaFactory.getDefaultCinemaList());

    List<CinemaDto> result = cinemaService.getAllCinemas(CITY, ADDRESS);

    assertEquals(expected, result);
  }

  @Test
  public void testGetAllCinemas_cityNotNull_success() {
    List<CinemaDto> expected = CinemaFactory.getDefaultCinemaDtoList();
    when(cinemaMapper.mapCinemaToCinemaDtoList(any())).thenReturn(expected);
    when(cinemaRepository.findAllByCity(anyString())).thenReturn(CinemaFactory.getDefaultCinemaList());

    List<CinemaDto> result = cinemaService.getAllCinemas(CITY, null);

    assertEquals(expected, result);
  }

  @Test
  public void testGetAllCinemas_addressNotNull_success() {
    List<CinemaDto> expected = CinemaFactory.getDefaultCinemaDtoList();
    when(cinemaMapper.mapCinemaToCinemaDtoList(any())).thenReturn(expected);
    when(cinemaRepository.findAllByAddress(anyString())).thenReturn(CinemaFactory.getDefaultCinemaList());

    List<CinemaDto> result = cinemaService.getAllCinemas(null, ADDRESS);

    assertEquals(expected, result);
  }

  @Test
  public void testGetCinemaById_cinemaFound_success() {
    Cinema expected = CinemaFactory.getDefaultCinema();
    when(cinemaRepository.findById(anyInt())).thenReturn(Optional.of(CinemaFactory.getDefaultCinema()));

    Cinema cinema = cinemaService.getCinemaById(ID);

    assertEquals(expected, cinema);
  }

  @Test(expected = CinemaNotFoundException.class)
  public void testGetCinemaById_cinemaNotFound_throwsCinemaNotFoundException() {
    cinemaService.getCinemaById(ID);
  }

  @Test
  public void testGetCinemaDtoById_cinemaDtoFound_success() {
    CinemaDto expected = CinemaFactory.getDefaultCinemaDto();
    when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(expected);
    when(cinemaRepository.findById(anyInt())).thenReturn(Optional.of(new Cinema()));

    CinemaDto cinemaDto = cinemaService.getCinemaDtoById(ID);

    assertEquals(expected, cinemaDto);
  }

  @Test
  public void testUpdateCinema_cinemaUpdated_success() {
    CinemaDto expected = CinemaFactory.getDefaultCinemaDto();
    when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(expected);
    when(cinemaRepository.findById(anyInt())).thenReturn(Optional.of(CinemaFactory.getDefaultCinema()));
    when(cinemaRepository.save(any())).thenReturn(CinemaFactory.getDefaultCinema());

    CinemaDto cinemaDto = cinemaService.updateCinema(CinemaFactory.getDefaultCinemaRequest(), ID);

    assertEquals(expected, cinemaDto);
  }

  @Test
  public void testDeleteCinema_cinemaDeleted_success() {
    CinemaDto expected = CinemaFactory.getDefaultCinemaDto();
    when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(expected);
    when(cinemaRepository.findById(anyInt())).thenReturn(Optional.of(CinemaFactory.getDefaultCinema()));

    CinemaDto cinemaDto = cinemaService.deleteCinema(ID);

    assertEquals(expected, cinemaDto);
  }

  @Test
  public void testUpdateCinemaAverageRating_success() {
    double newRating = 4.5;

    when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(CinemaFactory.getDefaultCinemaDto());
    when(cinemaRepository.findById(anyInt())).thenReturn(Optional.of(CinemaFactory.getDefaultCinema()));

    CinemaDto result = cinemaService.updateCinemaAverageRating(newRating, ID);

    assertEquals(newRating, result.getAverageRating(), 0.01);
  }
}