package org.pipeman.dccapi;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.pipeman.dccapi.events.EventEmitter;
import org.pipeman.dccapi.events.RawMessageEvent;
import org.pipeman.dccapi.events.messages.MessageCreateEvent;
import org.pipeman.dccapi.events.messages.MessageDeleteEvent;
import org.pipeman.dccapi.events.messages.MessageEditEvent;
import org.pipeman.dccapi.events.ws.DisconnectedEvent;
import org.pipeman.dccapi.events.ws.LoggedInEvent;

import java.net.URI;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

class DCConnection extends WebSocketClient {
    private final String token;
    private final EventEmitter eventEmitter;
    private Integer lastHeartBeatID = null;
    private final Consumer<Long> accountIDSetter;

    public DCConnection(URI serverUri, String token, EventEmitter eventEmitter, Consumer<Long> accountIDSetter) {
        super(serverUri);
        this.token = token;
        this.eventEmitter = eventEmitter;
        this.accountIDSetter = accountIDSetter;
    }

    public DCConnection(String token, EventEmitter eventEmitter, Consumer<Long> accountIDSetter) {
        this(URI.create("wss://gateway.discord.gg/?v=9&encoding=json"), token, eventEmitter, accountIDSetter);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        send("""
                {
                    "op": 2,
                    "d": {
                        "token": "%s",
                        "properties": {
                            "$os": "macos",
                            "$browser": "safari",
                            "$device": "pc"
                        }
                    }
                }
                """.formatted(token));
    }

    @Override
    public void onMessage(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("s") && !jsonObject.isNull("s")) lastHeartBeatID = jsonObject.getInt("s");

            if (jsonObject.getInt("op") == 10) {
                startHeartBeater(jsonObject.getJSONObject("d").getInt("heartbeat_interval"));
                return;
            }

            if (jsonObject.isNull("t") || jsonObject.getString("t").equals("SESSIONS_REPLACE")) return;

            if (jsonObject.getString("t").equals("READY")) accountIDSetter.accept(Long.parseLong(
                    jsonObject.getJSONObject("d").getJSONObject("user").getString("id")));

            JSONObject d = jsonObject.getJSONObject("d");
            switch (jsonObject.getString("t")) {
                case "READY" -> eventEmitter.emitEvent(new LoggedInEvent());

                case "MESSAGE_CREATE" -> eventEmitter.emitEvent(MessageCreateEvent.from(d));

                case "MESSAGE_DELETE" -> eventEmitter.emitEvent(MessageDeleteEvent.from(d));

                case "MESSAGE_UPDATE" -> eventEmitter.emitEvent(MessageEditEvent.from(d));

                default -> eventEmitter.emitEvent(new RawMessageEvent(jsonObject));
            }
        } catch (Exception e) {
            System.out.println(s);
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        eventEmitter.emitEvent(new DisconnectedEvent());
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    private void startHeartBeater(int delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    send(new JSONObject().put("op", 1).put("d",
                            lastHeartBeatID == null ? JSONObject.NULL : lastHeartBeatID).toString());
                } catch (WebsocketNotConnectedException ignored) {
                    cancel();
                }
            }
        }, (long) (delay * new Random().nextFloat()), delay);
    }
}
