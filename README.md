## Small Discord API wrapper that supports normal and bot accounts.

Example:

```java
public class Main {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        DiscordClient client = DiscordClient.connect("Your token");
        client.getEventEmitter().addListener(new Listener() {
            @Override
            public void onMessageCreate(MessageCreateEvent event) {
                Message message = event.message();
                if (client.isMe(message.author()) && message.content().startsWith("!annoy ")) {
                    client.editMessage(message.id(), message.channel().id(), makeAnnoying(message.content()));
                }
            }

            @Override
            public void onLogin(LoggedInEvent event) {
                System.out.println("Logged in! :)");
            }
        });
    }

    private static String makeAnnoying(String s) {
        StringBuilder out = new StringBuilder(s.length() - 7);
        for (char c : s.toLowerCase().substring(7).toCharArray()) {
            out.append(random.nextBoolean() ? Character.toUpperCase(c) : c);
        }
        return out.toString();
    }
}
```
This script makes your messages that start with `!annoy ` annoying to read.
