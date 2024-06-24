import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Handles weather-related queries by interfacing with a weather API.
 */
public class WeatherService {
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather";
    private final String apiKey;

    /**
     * Constructs a WeatherService with the given API key.
     *
     * @param apiKey The API key for accessing the weather service
     */
    public WeatherService(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Retrieves and displays the current weather for a given city.
     *
     * @param city The name of the city to get weather for
     */
    public void getWeather(String city) {
        try {
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
            String urlStr = API_URL + "?q=" + encodedCity + "&appid=" + apiKey + "&units=metric";
            String response = HttpUtil.sendHttpRequest(urlStr);

            // Parse the JSON response
            String temp = response.split("\"temp\":")[1].split(",")[0];
            String description = response.split("\"description\":\"")[1].split("\"")[0];

            System.out.printf("AI: The current weather in %s is %.1f°C with %s.%n",
                    city, Double.parseDouble(temp), description);
        } catch (Exception e) {
            System.out.println("AI: I apologize, but I couldn't retrieve the weather information at the moment. " +
                    "Please try again later.");
            e.printStackTrace();
        }
    }
}