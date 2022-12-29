package org.pipeman.dccapi.wrappers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Message {
    private final MessageAuthor author;
    private final String content;
    private final Channel channel;
    private final long id;
    private final boolean mentionEveryone;
    private final MessageSnapshot messageReference;
    private final Guild guild;
    private final List<Attachment> attachments;

    public Message(MessageAuthor author, String content, Channel channel, long id, boolean mentionEveryone, MessageSnapshot messageReference, Guild guild, List<Attachment> attachments) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.channel = channel;
        this.mentionEveryone = mentionEveryone;
        this.messageReference = messageReference;
        this.guild = guild;
        this.attachments = attachments;
    }

    public static Message from(JSONObject d) {
        return new Message(
                MessageAuthor.from(d.getJSONObject("author")),
                d.getString("content"),
                Channel.from(d),
                Long.parseLong(d.getString("id")),
                d.getBoolean("mention_everyone"),
                getRefMessage(d),
                Guild.from(d),
                getAttachments(d)
        );
    }

    private static MessageSnapshot getRefMessage(JSONObject d) {
        if (d.isNull("referenced_message")) return null;
        else return MessageSnapshot.from(d.getJSONObject("message_reference"));
    }

    private static List<Attachment> getAttachments(JSONObject d) {
        JSONArray attachments = d.getJSONArray("attachments");
        JSONArray embeds = d.getJSONArray("embeds");
        List<Attachment> out = new ArrayList<>(attachments.length() + embeds.length());

        if (attachments.length() > 0) {
            for (int i = 0; i < attachments.length(); i++)
                out.add(Attachment.from(attachments.getJSONObject(i), false));
        }

        if (embeds.length() > 0) {
            for (int i = 0; i < embeds.length(); i++) {
                if (embeds.getJSONObject(i).has("url")) {
                    String url = embeds.getJSONObject(i).getString("url");
                    out.add(new Attachment(url.substring(url.lastIndexOf("/") + 1), url, true));
                }
            }
        }
        return out;
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

    public List<Attachment> attachments() {
        return attachments;
    }
}
