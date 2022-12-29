package org.pipeman.dccapi.wrappers;

import org.json.JSONObject;

public record Guild(Long id) {
    public static Guild from(JSONObject d) {
        return d.has("guild_id") ? new Guild(Long.parseLong(d.getString("guild_id"))) : null;
    }
}
