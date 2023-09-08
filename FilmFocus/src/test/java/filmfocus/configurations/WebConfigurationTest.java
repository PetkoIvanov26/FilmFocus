package filmfocus.configurations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class WebConfigurationTest {

  @Spy
  private WebConfiguration webConfiguration;

  @Test
  public void testRestTemplate() {
    RestTemplate restTemplate = webConfiguration.restTemplate();

    assertNotNull(restTemplate);
  }

  @Test
  public void testRandom() {
    Random random = webConfiguration.random();

    assertNotNull(random);
  }
}
