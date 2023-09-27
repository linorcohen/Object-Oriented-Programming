import java.util.*;

/**
 * Base file for the ChatterBot exercise.
 * The bot's replyTo method receives a statement.
 * If it starts with the constant REQUEST_PREFIX, the bot returns
 * whatever is after this prefix. Otherwise, it returns one of
 * a few possible replies as supplied to it via its constructor.
 * In this case, it may also include the statement after
 * the selected reply (coin toss).
 *
 * @author Dan Nirel
 */
class ChatterBot {

    /**
     * prefix for a legal request
     */
    static final String REQUEST_PREFIX = "say ";

    /**
     * legal request placeholder
     */
    static final String REQUESTED_PHRASE_PLACEHOLDER = "<phrase>";

    /**
     * illegal request placeholder
     */
    static final String ILLEGAL_REQUEST_PLACEHOLDER = "<request>";

    Random rand = new Random();
    String[] repliesToIllegalRequest;
    String[] repliesToLegalRequest;
    String name;

    /**
     * constructor.
     * initials ChatterBot with the given array of legal requests,
     * array of illegal requests, and the name of the bot.
     *
     * @param name                    - string of the name of the bot
     * @param repliesToLegalRequest   - string[] of legal requests
     * @param repliesToIllegalRequest - string[] of illegal requests
     */
    ChatterBot(String name, String[] repliesToLegalRequest, String[] repliesToIllegalRequest) {
        this.repliesToLegalRequest = new String[repliesToLegalRequest.length];
        for (int i = 0; i < repliesToLegalRequest.length; i = i + 1) {
            this.repliesToLegalRequest[i] = repliesToLegalRequest[i];
        }
        this.repliesToIllegalRequest = new String[repliesToIllegalRequest.length];
        for (int i = 0; i < repliesToIllegalRequest.length; i = i + 1) {
            this.repliesToIllegalRequest[i] = repliesToIllegalRequest[i];
        }
        this.name = name;
    }

    /**
     * @return the chat bot name.
     */
    String getName() {
        return this.name;
    }

    /**
     * this function reply to the current statement that was giving.
     * if the statement starts with the REQUEST_PREFIX - returns legal reply.
     * else, returns illegal reply.
     *
     * @param statement - string of the given statement
     * @return a reply that was chosen from the correct pattern.
     */
    String replyTo(String statement) {
        if (statement.startsWith(REQUEST_PREFIX)) {
            String phrase = statement.replaceFirst(REQUEST_PREFIX, "");
            return replacePlaceholderInARandomPattern(repliesToLegalRequest, REQUESTED_PHRASE_PLACEHOLDER,
                    phrase);
        }
        return replacePlaceholderInARandomPattern(repliesToIllegalRequest, ILLEGAL_REQUEST_PLACEHOLDER,
                statement);
    }

    /**
     * this function takes a random number, selects a pattern from the given pattern array,
     * and replace its placeholder with the given phrase.
     *
     * @param patternsArray - string[] of patterns to choose from
     * @param placeholder   = string of the correct placeholder
     * @param phrase        - string of the phrase to replace with
     * @return the chosen pattern with the given phrase
     */
    String replacePlaceholderInARandomPattern(String[] patternsArray, String placeholder, String phrase) {
        int randomIndex = rand.nextInt(patternsArray.length);
        return patternsArray[randomIndex].replaceAll(placeholder, phrase);
    }

}
