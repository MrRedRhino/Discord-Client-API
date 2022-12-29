package org.pipeman.dccapi.wrappers;

import org.json.JSONObject;

import java.util.Optional;

public class Message {
    private final MessageAuthor author;
    private final String content;
    private final Channel channel;
    private final long id;
    private final boolean mentionEveryone;
    private final MessageSnapshot messageReference;
    private final Guild guild;

    public Message(MessageAuthor author, String content, Channel channel, long id, boolean mentionEveryone, MessageSnapshot messageReference, Guild guild) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.channel = channel;
        this.mentionEveryone = mentionEveryone;
        this.messageReference = messageReference;
        this.guild = guild;
    }

    public static Message from(JSONObject d) {
        return new Message(
                MessageAuthor.from(d.getJSONObject("author")),
                d.getString("content"),
                Channel.from(d),
                Long.parseLong(d.getString("id")),
                d.getBoolean("mention_everyone"),
                getRefMessage(d),
                Guild.from(d)
        );
    }

    private static MessageSnapshot getRefMessage(JSONObject d) {
        if (d.isNull("referenced_message")) return null;
        else return MessageSnapshot.from(d.getJSONObject("message_reference"));
    }

    public MessageAuthor author() {
        return author;
    }

    public String content() {
        return content;
    }

    public Channel channel() {
        return channel;
    }

    public boolean mentionEveryone() {
        return mentionEveryone;
    }

    public long id() {
        return id;
    }

    public Optional<MessageSnapshot> messageReference() {
        return Optional.ofNullable(messageReference);
    }

    public Optional<Guild> guild() {
        return Optional.ofNullable(guild);
    }

    public boolean isInGuild() {
        return guild != null;
    }
}
