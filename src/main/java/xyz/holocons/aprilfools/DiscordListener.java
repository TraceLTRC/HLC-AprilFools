package xyz.holocons.aprilfools;

import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePostProcessEvent;
import github.scarsz.discordsrv.api.events.VentureChatMessagePostProcessEvent;
import github.scarsz.discordsrv.dependencies.kyori.adventure.text.Component;

public class DiscordListener {

    private final AprilFools plugin;
    private boolean DEBUG;

    public DiscordListener(AprilFools plugin) {
        this.plugin = plugin;
        this.DEBUG = true;
    }

    /*
    Add "peko" or "motherfucker" most of the time to the ingame chat from discord messages.
     */
    @Subscribe(priority = ListenerPriority.HIGHEST)
    public void discordMessageToMinecraft(DiscordGuildMessagePostProcessEvent event) {
        if (plugin.isNotActivated() || event.isCancelled())
            return;

        var msg = event.getMinecraftMessage();

        int currentTick = plugin.getServer().getCurrentTick();

        // 33% equal rates of nothing, peko, or motherfucker
        // Probably not exacly equal cuz multiples lmao
        if (currentTick % 3 == 0) {
            msg = msg.append(Component.text(" peko"));
        } else if (currentTick % 2 == 0) {
            msg = msg.append(Component.text(" motherfucker"));
        }

        event.setMinecraftMessage(msg);
    }

    /*
    Remove " peko" or " motherfucker" from a minecraft message if it contains it at the last word.
    Does it only once, so people who got pekonoted won't get their entire peko chat removed.
    Case Sensitive.
     */
    @Subscribe(priority = ListenerPriority.HIGHEST)
    public void minecraftMessageToDiscord(VentureChatMessagePostProcessEvent event) {
        if (plugin.isNotActivated() || event.isCancelled())
            return;

        var msg = event.getProcessedMessage();

        // Edge cases that idk if it'll happen
        if (msg.equals(" peko") || msg.equals(" motherfucker") || msg.equals(""))
            return;

        if (msg.endsWith(" peko")) {
            msg = msg.substring(0, msg.length() - 5);
            event.setProcessedMessage(msg);
        } else if (msg.endsWith(" motherfucker")) {
            msg = msg.substring(0, msg.length() - 13);
            event.setProcessedMessage(msg);
        }
    }

}
