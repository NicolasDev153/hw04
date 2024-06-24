/**
 * Handles currency exchange rate queries by interfacing with an exchange rate API.
 */
public class CurrencyService {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";
    private final String apiKey;

    /**
     * Constructs a CurrencyService with the given API key.
     *
     * @param apiKey The API key for accessing the exchange rate service
     */
    public CurrencyService(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Retrieves and displays the current exchange rate for given currencies and amount.
     *
     * @param from The currency to convert from
     * @param to The currency to convert to
     * @param amount The amount to convert
     */
    public void getExchangeRate(String from, String to, double amount) {
        try {
            String urlStr = API_URL + apiKey + "/pair/" + from.toUpperCase() + "/" + to.toUpperCase();
            String response = HttpUtil.sendHttpRequest(urlStr);

            // Parse the JSON response
            String rateStr = response.split("\"conversion_rate\":")[1].split("}")[0];
            double rate = Double.parseDouble(rateStr);
            double convertedAmount = amount * rate;

            System.out.printf("AI: %.2f %s is equivalent to %.2f %s (exchange rate: %.4f).%n",
                    amount, from.toUpperCase(), convertedAmount, to.toUpperCase(), rate);
        } catch (Exception e) {
            System.out.println("AI: I'm sorry, but I couldn't fetch the exchange rate at the moment. " +
                    "Please try again later.");
            e.printStackTrace();
        }
    }
}