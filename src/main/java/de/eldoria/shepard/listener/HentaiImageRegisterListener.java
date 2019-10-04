package de.eldoria.shepard.listener;

import de.eldoria.shepard.messagehandler.MessageSender;
import de.eldoria.shepard.minigames.guessgame.ConfigurationType;
import de.eldoria.shepard.minigames.guessgame.ImageConfiguration;
import de.eldoria.shepard.minigames.guessgame.ImageRegister;
import de.eldoria.shepard.wrapper.MessageEventDataWrapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.Color;

public class HentaiImageRegisterListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        MessageEventDataWrapper wrapper = new MessageEventDataWrapper(event);
        ImageRegister register = ImageRegister.getInstance();
        ConfigurationType configurationState = register.getConfigurationState(wrapper);
        if (configurationState != ConfigurationType.NONE) {
            if (event.getMessage().getAttachments().size() == 1) {
                if (configurationState == ConfigurationType.CROPPED) {
                    MessageSender.sendMessage("Cropped Image Registered." + System.lineSeparator()
                            + "Please send the full image", event.getChannel());
                }
                register.addImage(wrapper,
                        event.getMessage().getAttachments().get(0).getUrl());

            }
        }

        if (register.getConfigurationState(wrapper) == ConfigurationType.CONFIGURED) {
            ImageConfiguration configuration = register.getConfiguration(wrapper);

            if (register.registerConfiguration(wrapper)) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("Added new " + (configuration.isHentai() ? "hentai" : "non hentai")
                                + " image set.")
                        .setThumbnail(configuration.getCroppedImage())
                        .setImage(configuration.getFullImage())
                        .setDescription("Registered with thumbnail and full image.")
                        .setColor(Color.green);

                event.getChannel().sendMessage(builder.build()).queue();

            }
        }
    }
}