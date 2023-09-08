package filmfocus.services;

import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import filmfocus.models.entities.Order;
import filmfocus.models.entities.User;
import filmfocus.testUtils.factories.OrderFactory;
import filmfocus.testUtils.factories.UserFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static filmfocus.testUtils.constants.UserConstants.PASSWORD;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

  @Mock
  private MailjetResponse mailjetResponse;

  @InjectMocks
  private EmailService emailService;

  @Test
  public void testSendOrderConfirmationEmail() throws JSONException, MailjetSocketTimeoutException, MailjetException {
    User user = UserFactory.getDefaultUser();
    Order order = OrderFactory.getDefaultOrder();
    JSONObject data = new JSONObject();
    when(mailjetResponse.getStatus()).thenReturn(200);
    data.put("Email", user.getEmail());
    when(mailjetResponse.getData()).thenReturn(new JSONArray().put(data));

    emailService.sendOrderConfirmationEmail(user, order);

    assertEquals(200, mailjetResponse.getStatus());
    assertEquals(user.getEmail(), mailjetResponse.getData().getJSONObject(0).getString("Email"));
  }

  @Test
  public void testSendPasswordConfirmationEmail() throws JSONException, MailjetSocketTimeoutException,
    MailjetException {
    User user = UserFactory.getDefaultUser();
    JSONObject data = new JSONObject();
    when(mailjetResponse.getStatus()).thenReturn(200);
    data.put("Email", user.getEmail());
    when(mailjetResponse.getData()).thenReturn(new JSONArray().put(data));

    emailService.sendPasswordConfirmationEmail(user, PASSWORD);

    assertEquals(200, mailjetResponse.getStatus());
    assertEquals(user.getEmail(), mailjetResponse.getData().getJSONObject(0).getString("Email"));
  }

  @Test
  public void testSendRegistrationConfirmationEmail() throws JSONException, MailjetSocketTimeoutException, MailjetException {

    User user = UserFactory.getDefaultUser();

    JSONObject data = new JSONObject();
    when(mailjetResponse.getStatus()).thenReturn(200);
    data.put("Email", user.getEmail());
    when(mailjetResponse.getData()).thenReturn(new JSONArray().put(data));

    emailService.sendRegistrationConfirmationEmail(user);

    assertEquals(200, mailjetResponse.getStatus());
    assertEquals(user.getEmail(), mailjetResponse.getData().getJSONObject(0).getString("Email"));
  }
}
