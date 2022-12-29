package org.pipeman.dccapi;

import org.json.JSONObject;
import org.pipeman.dccapi.events.EventEmitter;
import org.pipeman.dccapi.wrappers.MessageAuthor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DiscordClient {
    private final DCConnection connection;
    private final EventEmitter eventEmitter = new EventEmitter();
    private final HttpClient HTTP = HttpClient.newHttpClient();
    private final String token;
    private long accountID = 0;

    private DiscordClient(String token) {
        this.token = token;
        connection = new DCConnection(token, eventEmitter, l -> accountID = l);
        connection.connect();
    }

    public static DiscordClient connect(String token) {
        return new DiscordClient(token);
    }

    public EventEmitter getEventEmitter() {
        return eventEmitter;
    }

    public void disconnect() {
        connection.close();
    }

    public long getMyID() {
        return accountID;
    }

    public boolean isMe(MessageAuthor author) {
        return accountID == author.id();
    }

    public void sendTyping(long channelID) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://discord.com/api/v9/channels/" + channelID + "/typing"))
                .header("authorization", token)
                .header("Content-Type", "application/json")
                .build();
        sendHttpRequest(request);
    }

    public void sendMessage(long channelID, String content) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://discord.com/api/v9/channels/" + channelID + "/messages"))
                .header("authorization", token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"content\":\"" + quote(content) + "\"}"))
                .build();
        sendHttpRequest(request);
    }

    public void editMessage(long messageID, long channelID, String newContent) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://discord.com/api/v9/channels/" + channelID + "/messages/" + messageID))
                .header("authorization", token)
                .header("Content-Type", "application/json")
                .method(
                        "PATCH",
                        HttpRequest.BodyPublishers.ofString("{\"content\":\"" + quote(newContent) + "\"}")
                )
                .build();
        sendHttpRequest(request);
    }

    public void setStatus(String text) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://discord.com/api/v9/users/@me/settings"))
                .header("authorization", token)
                .header("Content-Type", "application/json")
                .method("PATCH",
                        HttpRequest.BodyPublishers.ofString(
                                new JSONObject().put("custom_status", new JSONObject()
                                                .put("emoji_id", JSONObject.NULL)
                                                .put("emoji_name", JSONObject.NULL)
                                                .put("expires_at", JSONObject.NULL)
                                                .put("text", text))
                                        .toString())
                )
                .build();
        sendHttpRequest(request);
    }

    private static String quote(String s) {
        return s.replace("\n", "\\n").replace("\"", "\\\"");
    }

    private void sendHttpRequest(HttpRequest request) {
        try {
            HTTP.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
