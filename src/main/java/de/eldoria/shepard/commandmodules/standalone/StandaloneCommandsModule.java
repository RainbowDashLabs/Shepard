package de.eldoria.shepard.commandmodules.standalone;

import de.eldoria.shepard.commandmodules.SharedResources;
import de.eldoria.shepard.commandmodules.standalone.botconfig.Restart;
import de.eldoria.shepard.commandmodules.standalone.botconfig.Upgrade;
import de.eldoria.shepard.commandmodules.standalone.fun.LargeEmote;
import de.eldoria.shepard.commandmodules.standalone.fun.MagicConch;
import de.eldoria.shepard.commandmodules.standalone.fun.MassEffect;
import de.eldoria.shepard.commandmodules.standalone.fun.MockingSpongebob;
import de.eldoria.shepard.commandmodules.standalone.fun.Oha;
import de.eldoria.shepard.commandmodules.standalone.fun.Owo;
import de.eldoria.shepard.commandmodules.standalone.fun.RandomJoke;
import de.eldoria.shepard.commandmodules.standalone.fun.Say;
import de.eldoria.shepard.commandmodules.standalone.fun.Someone;
import de.eldoria.shepard.commandmodules.standalone.fun.Uwu;
import de.eldoria.shepard.commandmodules.standalone.util.Avatar;
import de.eldoria.shepard.commandmodules.standalone.util.Feedback;
import de.eldoria.shepard.commandmodules.standalone.util.GetRaw;
import de.eldoria.shepard.commandmodules.standalone.util.Help;
import de.eldoria.shepard.commandmodules.standalone.util.HireMe;
import de.eldoria.shepard.commandmodules.standalone.util.Home;
import de.eldoria.shepard.commandmodules.standalone.util.SystemInfo;
import de.eldoria.shepard.commandmodules.standalone.util.Test;
import de.eldoria.shepard.commandmodules.standalone.util.UserInfo;
import de.eldoria.shepard.commandmodules.standalone.util.Vote;
import de.eldoria.shepard.modulebuilder.ModuleBuilder;

public class StandaloneCommandsModule implements ModuleBuilder {
    @Override
    public void buildModule(SharedResources resources) {
        addAndInit(resources, new Restart(), new Upgrade());

        addAndInit(resources, new LargeEmote(), new MagicConch(), new MassEffect(), new MockingSpongebob(),
                new Oha(), new Owo(), new Uwu(), new RandomJoke(), new Say(), new Someone());

        addAndInit(resources, new Avatar(), new Feedback(), new GetRaw(), new Help(), new HireMe(),
                new Home(), new SystemInfo(), new UserInfo(), new Vote());
        if (resources.getConfig().getGeneralSettings().isBeta()) {
            addAndInit(new Test(), resources);
        }
    }
}
