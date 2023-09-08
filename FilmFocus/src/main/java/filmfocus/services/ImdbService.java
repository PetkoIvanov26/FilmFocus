package filmfocus.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ImdbService {

  private final RestTemplate restTemplate;

  private String imdbKey;

  private String baseUrl;

  public ImdbService(
    RestTemplate restTemplate, @Value("${imdb.key}") String imdbKey, @Value("${imdb.url}") String baseUrl) {
    this.restTemplate = restTemplate;
    this.imdbKey = imdbKey;
    this.baseUrl = baseUrl;
  }

  public void setImdbKey(String imdbKey) {
    this.imdbKey = imdbKey;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getMovies(String filter) {
    String searchUrl;

    if ("top".equals(filter)) {
      searchUrl = baseUrl + "Top250Movies/" + imdbKey;
    } else if ("coming soon".equals(filter)) {
      searchUrl = baseUrl + "ComingSoon/" + imdbKey;
    } else if ("box office".equals(filter)) {
      searchUrl = baseUrl + "BoxOfficeAllTime/" + imdbKey;
    } else {
      throw new IllegalArgumentException("Invalid filter type");
    }

    String unfilteredMovies = restTemplate.getForObject(searchUrl, String.class);

    return filterMoviesTopMovies(unfilteredMovies);
  }

  private String filterMoviesTopMovies(String responseBody) {
    JSONObject responseJson = new JSONObject(responseBody);
    JSONArray movies = responseJson.getJSONArray("items");
    JSONArray filteredMovies = new JSONArray();

    for (int i = 0; i < movies.length(); i++) {
      JSONObject movie = movies.getJSONObject(i);
      JSONObject filteredMovie = new JSONObject();

      filteredMovie.put("title", movie.getString("title"));
      filteredMovie.put("year", movie.getString("year"));

      if (movie.has("image")) {
        filteredMovie.put("image", movie.getString("image"));
      }

      filteredMovies.put(filteredMovie);
    }

    JSONObject resultJson = new JSONObject();
    resultJson.put("movies", filteredMovies);

    return resultJson.toString(2);
  }
}

