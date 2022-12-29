package org.pipeman.dccapi.events.messages;

import org.json.JSONObject;
import org.pipeman.dccapi.events.DiscordEvent;
import org.pipeman.dccapi.wrappers.Message;
import org.pipeman.dccapi.wrappers.MessageAuthor;
import org.pipeman.dccapi.wrappers.MessageSnapshot;

public record MessageEditEvent(MessageSnapshot newMessage) implements DiscordEvent {
    public static MessageEditEvent from(JSONObject d) {
        return new MessageEditEvent(MessageSnapshot.from(d));
    }
}
