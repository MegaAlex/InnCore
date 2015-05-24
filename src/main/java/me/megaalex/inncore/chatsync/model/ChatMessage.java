package me.megaalex.inncore.chatsync.model;

public class ChatMessage {

    private String channel;
    private String prefix;
    private String player;
    private String suffix;
    private String message;

    private Long timestamp;

    public ChatMessage(String channel, String prefix, String player, String suffix,
                       String message, Long timestamp) {
        this.channel = channel;
        this.prefix = prefix;
        this.player = player;
        this.suffix = suffix;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getChannel() {
        return channel;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getPlayer() {
        return player;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getMessage() {
        return message;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
