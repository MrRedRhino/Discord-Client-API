package org.pipeman.dccapi.wrappers;

import org.json.JSONObject;

public record Channel(long id) {
    public static Channel from(JSONObject d) {
        return new Channel(Long.parseLong(d.getString("channel_id")));
    }
}
