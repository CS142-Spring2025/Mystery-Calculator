import java.util.*;

public class FortuneEngine {

    private static final List<String> MISSIONS = Arrays.asList(
            "Infiltrate the tech lab and retrieve the encrypted drive.",
            "Follow the courier without being detected.",
            "Meet the informant under the old clock tower.",
            "Neutralize the surveillance grid in Sector 7.",
            "Intercept the black briefcase at Central Station.",
            "Plant the device inside the CEO’s office unnoticed.",
            "Track the rogue agent’s signal across the border.",
            "Secure the stolen prototype before midnight.",
            "Fake a power outage to cover the extraction.",
            "Obtain the voice sample from the diplomat.",
            "Disable the drone patrols silently.",
            "Recover the lost cipher module from Vault 9.",
            "Create a diversion at the gala event.",
            "Tap into the encrypted call from Room 804.",
            "Hide the microchip in the museum artifact.",
            "Escape the compound before the shift change.",
            "Leave no trace in the server room operation.",
            "Record the code phrase during the interrogation.",
            "Replace the briefcase at the metro terminal.",
            "Blend in with the operatives at the auction."
    );

    private static final Random random = new Random();

    // Access message - displayed immediately
    public static String getAccessMessage() {
        return "Access Granted";
    }

    // Welcome message - displayed after 5 seconds
    public static String getWelcomeMessage() {
        return "Welcome Agent, your next mission is:";
    }

    // Random mission - displayed after another 5 seconds
    public static String getMission() {
        return MISSIONS.get(random.nextInt(MISSIONS.size()));
    }

    // Goodbye message - final message
    public static String getGoodbyeMessage() {
        return "Goodbye Agent, Be Careful out there";
    }
}
