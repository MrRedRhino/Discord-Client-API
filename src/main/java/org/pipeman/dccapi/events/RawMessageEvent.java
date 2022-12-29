package org.pipeman.dccapi.events;

import org.json.JSONObject;

public record RawMessageEvent(JSONObject data) implements DiscordEvent {
}
