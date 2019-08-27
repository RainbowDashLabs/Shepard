package de.eldoria.shepard.database;

import de.eldoria.shepard.messagehandler.MessageSender;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DbUtil {
    private static final Pattern ID_PATTERN = Pattern.compile("(?:<[@#!&]{1,2})?(?<id>[0-9]{18})(?:>)?");

    private DbUtil() {
    }

    /**
     * Extracts an id from discord's formatting.
     *
     * @param id the formatted id.
     * @return the extracted id.
     */
    public static String getIdRaw(String id) {
        Matcher matcher = ID_PATTERN.matcher(id);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("lol dis is not a id");
        }
        return matcher.group(1);
    }

    /**
     * Handles SQL Exceptions.
     *
     * @param ex    SQL Exception
     * @param event Event for error sending to channel to inform user.
     */
    public static void handleException(SQLException ex, MessageReceivedEvent event) {
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());

        if (event != null) {
            MessageSender.sendSimpleError("Ups. Looks like my Database has a small hickup."
                    + System.lineSeparator()
                    + "Can you give me another try, pls?", event.getChannel());
        }
    }

    /**
     * Returns from a list of role ids all valid roles.
     *
     * @param guild guild for role lookup
     * @param args  array of role id
     * @return list of valid roles
     */
    public static List<Role> getValidRoles(Guild guild, String[] args) {
        List<Role> roles = new ArrayList<>();
        for (String s : args) {
            Role role = guild.getRoleById(DbUtil.getIdRaw(s));
            if (role != null) {
                roles.add(role);
            }
        }
        return roles;
    }


}
