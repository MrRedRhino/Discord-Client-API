package org.pipeman.dccapi.wrappers;

import org.json.JSONObject;

public record Attachment(String filename, String url, boolean isEmbedded) {
    public static Attachment from(JSONObject o, boolean isEmbedded) {
        return new Attachment(o.getString("filename"), o.getString("url"), isEmbedded);
    }
}
