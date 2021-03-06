package de.eldoria.shepard.commandmodules.ticketsystem.util;

import de.eldoria.shepard.basemodules.commanddispatching.util.ArgumentParser;
import de.eldoria.shepard.commandmodules.ticketsystem.commands.Ticket;
import de.eldoria.shepard.commandmodules.ticketsystem.commands.TicketSettings;
import de.eldoria.shepard.commandmodules.ticketsystem.data.TicketData;
import de.eldoria.shepard.wrapper.MessageEventDataWrapper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Holds methods used in {@link Ticket} and {@link TicketSettings}.
 */
public final class TicketHelper {
    private TicketHelper() {
    }

    /**
     * Removes the roles from a user, but secures, that he keeps all necessary roles for other tickets.
     *
     * @param parser         parser for user and role parsing
     * @param ticketData     data for updating
     * @param messageContext Received event of the message.
     * @param member         member to change roles
     * @param rolesToRemove  roles as string list
     */
    public static void removeAndUpdateTicketRoles(TicketData ticketData, ArgumentParser parser,
                                                  MessageEventDataWrapper messageContext,
                                                  Member member, List<Role> rolesToRemove) {
        //Get all other ticket channels of the owner
        List<String> channelIds = ticketData.getChannelIdsByOwner(messageContext.getGuild(),
                member.getUser(), messageContext);

        List<TextChannel> channels = parser.getTextChannels(messageContext.getGuild(), channelIds);

        //Create a set of all roles the player should keep.
        Set<Role> newRoleSet = new HashSet<>();
        for (TextChannel channel : channels) {
            List<Role> roles = parser.getRoles(messageContext.getGuild(),
                    ticketData.getChannelOwnerRoles(messageContext.getGuild(), channel));
            newRoleSet.addAll(roles);
        }

        //Removes all roles for the current ticket
        for (Role role : rolesToRemove) {
            messageContext.getGuild().removeRoleFromMember(member, role).queue();
        }

        //Adds all roles for the other tickets. needed if two ticket types use the same role or
        // if there are more than one ticket channel with this type.
        for (Role role : newRoleSet) {
            messageContext.getGuild().addRoleToMember(member, role).queue();
        }
    }
}
