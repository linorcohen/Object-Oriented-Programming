import java.util.Scanner;

class Chat {

    public static void main(String[] args) {
        ChatterBot[] bots = new ChatterBot[2];
        bots[0] = new ChatterBot("Voldemort",
                new String[]{
                        "say " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER +
                                "? okay: " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER,
                        "you want me to say " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER
                                + " , do you? alright: " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER,
                        "if you insist: " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER,
                        "you asked so nice! fine : " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER
                },
                new String[]{
                        "what " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER,
                        "say i should say " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER,
                        "what if i don't want to say " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER + " ?",
                });

        bots[1] = new ChatterBot("Harry Potter",
                new String[]{
                        "say " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER +
                                "? okay: " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER,
                        "fine. i'll say " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER,
                        "say " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER +
                                " ? that's what you expect from me?"
                },
                new String[]{
                        "no no no, " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER,
                        "say say " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER,
                        "I don't want to say " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER,
                        "i feel like not saying " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER
                });

        String statement = "Avada Kadabra";

        Scanner scanner = new Scanner(System.in);
        while (true) {
            for (ChatterBot bot : bots) {
                statement = bot.replyTo(statement);
                System.out.print(bot.getName() + ": " + statement);
                scanner.nextLine();
            }
        }


    }

}
