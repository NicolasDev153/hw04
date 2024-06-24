import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

/**
 * Handles user interaction.
 * processes user commands and delegates tasks to appropriate services.
 */
public class ChatBot {
    private final WeatherService weatherService;
    private final CurrencyService currencyService;

    public ChatBot(String weatherApiKey, String currencyApiKey) {
        weatherService = new WeatherService(weatherApiKey);
        currencyService = new CurrencyService(currencyApiKey);
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Greetings! I'm your AI assistant. How may I help you today?");
            System.out.println("You can ask me about weather or currency exchange rates.");
            System.out.println("For weather: 'weather in <city>'");
            System.out.println("For exchange rates: 'exchange <amount> <from> to <to>'");
            System.out.println("To exit, simply type 'bye'.");

            while (true) {
                System.out.print("\nYou: ");
                String input = scanner.nextLine().trim().toLowerCase();

                if (input.equals("bye")) {
                    System.out.println("AI: It was a pleasure assisting you. Have a great day!");
                    break;
                }

                processInput(input);
            }
        }
    }

    /**
     * Processes user input and calls appropriate service methods.
     *
     * @param input The user's input string
     */
    private void processInput(String input) {
        if (input.startsWith("weather in ")) {
            String city = input.substring(11);
            weatherService.getWeather(city);
        } else if (input.startsWith("exchange ")) {
            String[] parts = input.substring(9).split(" ");
            if (parts.length == 4 && parts[2].equals("to")) {
                try {
                    double amount = Double.parseDouble(parts[0]);
                    currencyService.getExchangeRate(parts[1], parts[3], amount);
                } catch (NumberFormatException e) {
                    System.out.println("AI: I'm sorry, but I couldn't understand the amount. Please provide a valid number.");
                }
            } else {
                System.out.println("AI: I apologize, but I didn't quite catch that. Please use the format: exchange <amount> <from> to <to>");
            }
        } else {
            System.out.println("AI: I'm afraid I don't understand. Could you please rephrase your request?");
            System.out.println("Remember, you can ask about weather using 'weather in <city>' or currency exchange using 'exchange <amount> <from> to <to>'.");
        }
    }

    public static void main(String[] args) {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            prop.load(fis);
        } catch (IOException e) {
            System.out.println("Error: Could not read config file.");
            e.printStackTrace();
            return;
        }

        String weatherApiKey = prop.getProperty("weather.api.key");
        String currencyApiKey = prop.getProperty("currency.api.key");

        if (weatherApiKey == null || currencyApiKey == null) {
            System.out.println("Error: API keys not found in config file.");
            return;
        }

        ChatBot bot = new ChatBot(weatherApiKey, currencyApiKey);
        bot.start();
    }
}