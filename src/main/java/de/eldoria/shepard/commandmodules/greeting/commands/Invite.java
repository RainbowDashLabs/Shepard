package de.eldoria.shepard.commandmodules.greeting.commands;

import de.eldoria.shepard.commandmodules.Command;
import de.eldoria.shepard.commandmodules.argument.Parameter;
import de.eldoria.shepard.commandmodules.argument.SubCommand;
import de.eldoria.shepard.commandmodules.command.Executable;
import de.eldoria.shepard.commandmodules.greeting.data.InviteData;
import de.eldoria.shepard.commandmodules.greeting.types.DatabaseInvite;
import de.eldoria.shepard.commandmodules.util.CommandCategory;
import de.eldoria.shepard.localization.enums.commands.GeneralLocale;
import de.eldoria.shepard.localization.util.TextLocalizer;
import de.eldoria.shepard.messagehandler.ErrorType;
import de.eldoria.shepard.messagehandler.MessageSender;
import de.eldoria.shepard.modulebuilder.requirements.ReqDataSource;
import de.eldoria.shepard.util.TextFormatting;
import de.eldoria.shepard.wrapper.MessageEventDataWrapper;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.eldoria.shepard.localization.enums.commands.admin.InviteLocale.AD_CODE;
import static de.eldoria.shepard.localization.enums.commands.admin.InviteLocale.A_CODE;
import static de.eldoria.shepard.localization.enums.commands.admin.InviteLocale.A_INVITE_NAME;
import static de.eldoria.shepard.localization.enums.commands.admin.InviteLocale.C_ADD_INVITE;
import static de.eldoria.shepard.localization.enums.commands.admin.InviteLocale.C_REFRESH_INVITES;
import static de.eldoria.shepard.localization.enums.commands.admin.InviteLocale.C_REMOVE_INVITE;
import static de.eldoria.shepard.localization.enums.commands.admin.InviteLocale.C_SHOW_INVITES;
import static de.eldoria.shepard.localization.enums.commands.admin.InviteLocale.DESCRIPTION;
import static de.eldoria.shepard.localization.enums.commands.admin.InviteLocale.M_ADDED_INVITE;
import static de.eldoria.shepard.localization.enums.commands.admin.InviteLocale.M_CODE;
import static de.eldoria.shepard.localization.enums.commands.admin.InviteLocale.M_INVITE_NAME;
import static de.eldoria.shepard.localization.enums.commands.admin.InviteLocale.M_REGISTERED_INVITES;
import static de.eldoria.shepard.localization.enums.commands.admin.InviteLocale.M_REMOVED_INVITE;
import static de.eldoria.shepard.localization.enums.commands.admin.InviteLocale.M_REMOVED_NON_EXISTENT_INVITES;
import static de.eldoria.shepard.localization.enums.commands.admin.InviteLocale.M_USAGE_COUNT;
import static de.eldoria.shepard.localization.util.TextLocalizer.localizeAllAndReplace;
import static java.lang.System.lineSeparator;

public class Invite extends Command implements Executable, ReqDataSource {

    private static final Pattern INVITE = Pattern.compile("([a-zA-Z0-9]{6,7})$");

    private InviteData inviteData;

    /**
     * Creates a new Invite command object.
     */
    public Invite() {
        super("invite",
                null,
                DESCRIPTION.tag,
                SubCommand.builder("invite")
                        .addSubcommand(C_ADD_INVITE.tag,
                                Parameter.createCommand("add"),
                                Parameter.createInput(A_CODE.tag, AD_CODE.tag, true),
                                Parameter.createInput(GeneralLocale.A_NAME.tag, A_INVITE_NAME.tag, true))
                        .addSubcommand(C_REMOVE_INVITE.tag,
                                Parameter.createCommand("remove"),
                                Parameter.createInput(A_CODE.tag, AD_CODE.tag, true))
                        .addSubcommand(C_REFRESH_INVITES.tag,
                                Parameter.createCommand("refresh"))
                        .addSubcommand(C_SHOW_INVITES.tag,
                                Parameter.createCommand("list"))
                        .build(),
                CommandCategory.ADMIN);
    }


    @Override
    public void execute(String label, String[] args, MessageEventDataWrapper messageContext) {
        String cmd = args[0];
        if (isSubCommand(cmd, 0)) {
            addInvite(args, messageContext);
            return;
        }
        if (isSubCommand(cmd, 1)) {
            removeInvite(args, messageContext);
            return;
        }
        if (isSubCommand(cmd, 2)) {
            refreshInvites(messageContext);
            return;
        }
        if (isSubCommand(cmd, 3)) {
            listInvites(messageContext);
            return;
        }
        MessageSender.sendSimpleError(ErrorType.INVALID_ACTION, messageContext.getTextChannel());
    }

    private void listInvites(MessageEventDataWrapper messageContext) {
        List<DatabaseInvite> invites = inviteData.getInvites(messageContext.getGuild(), messageContext);

        StringBuilder message = new StringBuilder();
        message.append(M_REGISTERED_INVITES.tag).append(lineSeparator());

        TextFormatting.TableBuilder tableBuilder = TextFormatting.getTableBuilder(
                invites, TextLocalizer.localizeAll(M_CODE.tag, messageContext.getGuild()),
                TextLocalizer.localizeAll(M_USAGE_COUNT.tag, messageContext.getGuild()),
                TextLocalizer.localizeAll(M_INVITE_NAME.tag, messageContext.getGuild()));
        for (DatabaseInvite invite : invites) {
            tableBuilder.next();
            tableBuilder.setRow(invite.getCode(), invite.getUsedCount() + "", invite.getSource());
        }
        message.append(tableBuilder);
        MessageSender.sendMessage(message.toString(), messageContext.getTextChannel());
    }

    private void refreshInvites(MessageEventDataWrapper messageContext) {
        messageContext.getGuild().retrieveInvites().queue(invites -> {
            if (inviteData.updateInvite(messageContext.getGuild(), invites, messageContext)) {
                MessageSender.sendMessage(M_REMOVED_NON_EXISTENT_INVITES.tag, messageContext.getTextChannel());
            }
        });
    }

    private void removeInvite(String[] args, MessageEventDataWrapper messageContext) {
        if (args.length != 2) {
            MessageSender.sendSimpleError(ErrorType.INVALID_ARGUMENT, messageContext.getTextChannel());
            return;
        }
        List<DatabaseInvite> databaseInvites = inviteData.getInvites(messageContext.getGuild(), messageContext);

        for (DatabaseInvite invite : databaseInvites) {
            if (invite.getCode().equals(args[1])) {
                if (inviteData.removeInvite(messageContext.getGuild(), args[1], messageContext)) {
                    MessageSender.sendMessage(M_REMOVED_INVITE.tag + " **" + invite.getSource()
                            + "**", messageContext.getTextChannel());
                    return;
                }
            }
        }
        MessageSender.sendSimpleError(ErrorType.NO_INVITE_FOUND, messageContext.getTextChannel());
    }

    private void addInvite(String[] args, MessageEventDataWrapper messageContext) {
        if (args.length < 3) {
            MessageSender.sendSimpleError(ErrorType.TOO_FEW_ARGUMENTS, messageContext.getTextChannel());
            return;
        }

        Matcher matcher = INVITE.matcher(args[1]);
        if (!matcher.find()) {
            MessageSender.sendSimpleError(ErrorType.NO_INVITE_FOUND, messageContext.getTextChannel());
        }
        String code = matcher.group(1);


        messageContext.getGuild().retrieveInvites().queue(invites -> {
            for (var invite : invites) {
                if (invite.getCode().equals(code)) {
                    String name = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                    if (inviteData.addInvite(messageContext.getGuild(), invite.getCode(), name,
                            invite.getUses(), messageContext)) {
                        MessageSender.sendMessage(localizeAllAndReplace(M_ADDED_INVITE.tag,
                                messageContext.getGuild(),
                                "**" + name + "**",
                                "**" + invite.getCode() + "**",
                                "**" + invite.getUses() + "**"), messageContext.getTextChannel());
                    }
                    return;
                }
            }
            MessageSender.sendSimpleError(ErrorType.NO_INVITE_FOUND, messageContext.getTextChannel());
        });
    }

    @Override
    public void addDataSource(DataSource source) {
        inviteData = new InviteData(source);
    }
}
