package org.pipeman.dccapi.events.messages;

import org.json.JSONObject;
import org.pipeman.dccapi.events.DiscordEvent;
import org.pipeman.dccapi.wrappers.Message;
import org.pipeman.dccapi.wrappers.MessageAuthor;

public record MessageCreateEvent(Message message) implements DiscordEvent {
    public static MessageCreateEvent from(JSONObject d) {
        return new MessageCreateEvent(Message.from(d));
    }
}
