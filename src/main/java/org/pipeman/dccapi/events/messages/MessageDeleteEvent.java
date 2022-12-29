package org.pipeman.dccapi.events.messages;

import org.json.JSONObject;
import org.pipeman.dccapi.events.DiscordEvent;
import org.pipeman.dccapi.wrappers.MessageSnapshot;

public record MessageDeleteEvent(MessageSnapshot message) implements DiscordEvent {
    public static MessageDeleteEvent from(JSONObject d) {
        return new MessageDeleteEvent(MessageSnapshot.from(d));
    }
}
