package org.pipeman.dccapi.wrappers;

import org.json.JSONObject;

public record MessageAuthor(String username, int discriminator, long id, boolean bot) {
    public static MessageAuthor from(JSONObject o) {
        return new MessageAuthor(
                o.getString("username"),
                Integer.parseInt(o.getString("discriminator")),
                Long.parseLong(o.getString("id")),
                o.optBoolean("bot", false)
        );
    }
}
