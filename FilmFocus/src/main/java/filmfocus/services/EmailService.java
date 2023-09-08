package filmfocus.services;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import filmfocus.models.entities.Order;
import filmfocus.models.entities.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final String apiKey;
  private final String apiSecretKey;

  @Autowired
  public EmailService(@Value("${api.key}") String apiKey, @Value("${api.secret}") String apiSecretKey) {
    this.apiKey = apiKey;
    this.apiSecretKey = apiSecretKey;
  }

  public void sendOrderConfirmationEmail(User user, Order orderDetails) throws MailjetSocketTimeoutException,
    MailjetException {
    String senderEmail = "annargeorgieva21@gmail.com";
    String senderName = "FilmFocus";

    String recipientEmail = user.getEmail();
    String recipientName = user.getFirstName() + " " + user.getLastName();

    MailjetClient client;
    MailjetRequest request;

    client = new MailjetClient(apiKey, apiSecretKey, new ClientOptions("v3.1"));
    request = new MailjetRequest(Emailv31.resource)
      .property(Emailv31.MESSAGES, new JSONArray()
        .put(new JSONObject()
               .put(Emailv31.Message.FROM, new JSONObject()
                 .put("Email", senderEmail)
                 .put("Name", senderName))
               .put(Emailv31.Message.TO, new JSONArray()
                 .put(new JSONObject()
                        .put("Email", recipientEmail)
                        .put("Name", recipientName)))
               .put(Emailv31.Message.SUBJECT, "Your order confirmation")
               .put(Emailv31.Message.HTMLPART,
                    "<h3>Dear " + recipientName + ",</h3><p>You successfully created the order number: <strong>" +
                    orderDetails.getId() + "</strong>. The total price of the order is: <strong>" +
                    orderDetails.getTotalPrice() + " lv.</strong></p>")
        ));

    client.post(request);
  }

  public void sendPasswordConfirmationEmail(User user, String newPassword) throws MailjetSocketTimeoutException,
    MailjetException {
    String senderEmail = "annargeorgieva21@gmail.com";
    String senderName = "FilmFocus";

    String recipientEmail = user.getEmail();
    String recipientName = user.getFirstName() + " " + user.getLastName();

    MailjetClient client;
    MailjetRequest request;

    client = new MailjetClient(apiKey, apiSecretKey, new ClientOptions("v3.1"));
    request = new MailjetRequest(Emailv31.resource)
      .property(Emailv31.MESSAGES, new JSONArray()
        .put(new JSONObject()
               .put(Emailv31.Message.FROM, new JSONObject()
                 .put("Email", senderEmail)
                 .put("Name", senderName))
               .put(Emailv31.Message.TO, new JSONArray()
                 .put(new JSONObject()
                        .put("Email", recipientEmail)
                        .put("Name", recipientName)))
               .put(Emailv31.Message.SUBJECT, "Your password recovery confirmation")
               .put(Emailv31.Message.HTMLPART,
                    "<h3>Dear " + recipientName +
                    ",</h3><p>You successfully changed your password. Here is your new password: <strong>" +
                    newPassword + "</strong></p>")
        ));

    client.post(request);
  }
  public void sendRegistrationConfirmationEmail(User user) throws MailjetSocketTimeoutException,
    MailjetException {
    String senderEmail = "annargeorgieva21@gmail.com";
    String senderName = "FilmFocus";

    String recipientEmail = user.getEmail();
    String recipientName = user.getFirstName() + " " + user.getLastName();

    String discountMessage = "As our user here is your discount code for online reservation: 5555";

    MailjetClient client;
    MailjetRequest request;

    client = new MailjetClient(apiKey, apiSecretKey, new ClientOptions("v3.1"));
    request = new MailjetRequest(Emailv31.resource)
      .property(Emailv31.MESSAGES, new JSONArray()
        .put(new JSONObject()
               .put(Emailv31.Message.FROM, new JSONObject()
                 .put("Email", senderEmail)
                 .put("Name", senderName))
               .put(Emailv31.Message.TO, new JSONArray()
                 .put(new JSONObject()
                        .put("Email", recipientEmail)
                        .put("Name", recipientName)))
               .put(Emailv31.Message.SUBJECT, "Your registration confirmation")
               .put(Emailv31.Message.HTMLPART,
                    "<h3>Dear " + recipientName +
                    ",</h3><p>You have successfully registered. " + discountMessage + "</p>")
        ));

    client.post(request);
  }
}


