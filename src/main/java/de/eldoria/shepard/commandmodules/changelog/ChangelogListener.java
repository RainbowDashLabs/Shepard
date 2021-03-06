package de.eldoria.shepard.commandmodules.changelog;

import de.eldoria.shepard.messagehandler.MessageSender;
import de.eldoria.shepard.modulebuilder.requirements.ReqDataSource;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.lineSeparator;

public class ChangelogListener extends ListenerAdapter implements ReqDataSource {

    private ChangelogData changelogData;

    /**
     * Create a new changelog listener.
     */
    public ChangelogListener() {

    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        manageChangelog(event.getUser(), event.getGuild(), event.getRoles(), true);
    }

    @Override
    public void onGuildMemberRoleRemove(@Nonnull GuildMemberRoleRemoveEvent event) {
        manageChangelog(event.getUser(), event.getGuild(), event.getRoles(), false);
    }

    private void manageChangelog(User user, Guild guild, List<Role> roles, boolean add) {
        String channelId = changelogData.getChannel(guild, null);

        if (channelId == null) return;

        TextChannel channelById = guild.getTextChannelById(channelId);
        if (channelById == null) return;
        List<String> changelogRoles = changelogData.getRoles(guild, null);

        List<String> observedRoles = new ArrayList<>();

        roles.forEach(role -> {
            if (changelogRoles.contains(role.getId())) {
                observedRoles.add(role.getAsMention());
            }
        });

        if (observedRoles.size() == 0) {
            return;
        }

        if (add) {
            MessageSender.sendSimpleTextBox("[+] " + user.getAsTag(),
                    String.join(lineSeparator(), observedRoles), Color.green, channelById);
        } else {
            MessageSender.sendSimpleTextBox("[-] " + user.getAsTag(),
                    String.join(lineSeparator(), observedRoles), Color.red, channelById);
        }
    }

    @Override
    public void addDataSource(DataSource source) {
        changelogData = new ChangelogData(source);
    }
}
