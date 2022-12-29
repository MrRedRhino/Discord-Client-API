package org.pipeman.dccapi.wrappers;

import org.json.JSONObject;
import org.pipeman.dccapi.DCCAPIUtils;

public record MessageSnapshot(long messageID, long channelID, Long guildID) {
    public static MessageSnapshot from(JSONObject o) {
        return new MessageSnapshot(
                Long.parseLong(getID(o)),
                Long.parseLong(o.getString("channel_id")),
                DCCAPIUtils.parseLong(o.optString("guild_id")).orElse(null)
        );
    }

    private static String getID(JSONObject object) {
        return object.optString("message_id", object.optString("id", ""));
    }

    public boolean isInGuild() {
        return guildID != null;
    }
}
