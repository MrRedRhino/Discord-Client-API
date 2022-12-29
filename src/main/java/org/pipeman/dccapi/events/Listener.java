package org.pipeman.dccapi.events;

import org.pipeman.dccapi.events.messages.MessageCreateEvent;
import org.pipeman.dccapi.events.messages.MessageDeleteEvent;
import org.pipeman.dccapi.events.messages.MessageEditEvent;
import org.pipeman.dccapi.events.ws.DisconnectedEvent;
import org.pipeman.dccapi.events.ws.LoggedInEvent;

public interface Listener {
    // message events
    default void onMessageCreate(MessageCreateEvent event) {
    }

    default void onMessageDelete(MessageDeleteEvent event) {
    }

    default void onMessageEdit(MessageEditEvent event) {
    }

    // websocket events
    default void onRawMessage(RawMessageEvent event) {
    }

    default void onLogin(LoggedInEvent event) {
    }

    default void onDisconnected(DisconnectedEvent event) {
    }
}
