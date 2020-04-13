package de.eldoria.shepard.webapi.apiobjects.botlists;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class DiscordBotlistComRequests {
    @SerializedName("shard_id")
    private int shardId;
    private int guilds;
    private long users;
    @SerializedName("voice_connections")
    private int voiceConnections;

    public DiscordBotlistComRequests(int shardId, int guilds, long users, int voiceConnections) {
        this.shardId = shardId;
        this.guilds = guilds;
        this.users = users;
        this.voiceConnections = voiceConnections;
    }
}