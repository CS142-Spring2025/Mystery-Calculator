import java.util.*;

public class FortuneEngine {
    private static final List<String> SECRET_QUOTES = Arrays.asList(
        "The eagle flies at midnight.",
        "Whispers in the static are never wrong.",
        "When the clock strikes thirteen, listen.",
        "All mirrors are one-way if you look hard enough.",
        "The raven knows the way to the tower.",
        "It’s always darkest before the upload.",
        "Follow the sound of the broken code."
    );

    private static final Random random = new Random();

    public static String getRandomQuote() {
        return SECRET_QUOTES.get(random.nextInt(SECRET_QUOTES.size()));
    }
}
