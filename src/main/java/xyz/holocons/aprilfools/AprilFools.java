package xyz.holocons.aprilfools;

import github.scarsz.discordsrv.DiscordSRV;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class AprilFools extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {

    private final DiscordListener listener = new DiscordListener(this);
    private boolean activation = false;

    @Override
    public void onEnable() {
        DiscordSRV.api.subscribe(listener);
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("schizopeko").setExecutor(this);
        this.getCommand("schizopeko").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("aprilfools.use")) {
            sender.hasPermission("No permission!");
            return true;
        }

        if (args.length == 0) {
            var msg = Component.text();
            msg.append(Component.text("Current status: ", NamedTextColor.YELLOW));
            if (isNotActivated()) {
                msg.append(Component.text("false", NamedTextColor.RED));
            } else {
                msg.append(Component.text("true", NamedTextColor.GREEN));
            }

            msg.append(Component.newline(), Component.text("/aprilfools <true/false>"));
            sender.sendMessage(msg.build());
            return true;
        }

        if (args[1].equalsIgnoreCase("true")) {
            setActivation(true);
            sender.sendMessage(Component.text("April Fools enabled!", NamedTextColor.GREEN));
        } else if (args[1].equalsIgnoreCase("false")) {
            setActivation(false);
            sender.sendMessage(Component.text("April Fools disabled!", NamedTextColor.RED));
        } else {
            sender.sendMessage(
                    Component.text("Unknown value!")
                            .append(Component.newline())
                            .append(Component.text("/aprilfools <true/false>"))
            );
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return List.of("true", "false");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        if (isNotActivated())
            return;

        var msg = event.message();

        int currentTick = getServer().getCurrentTick();

        // 33% equal rates of nothing, peko, or motherfucker
        // Probably not exactly equal cuz multiples lmao
        if (currentTick % 3 == 0) {
            msg = msg.append(Component.text(" peko"));
        } else if (currentTick % 2 == 0) {
            msg = msg.append(Component.text(" motherfucker"));
        }

        event.message(msg);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean isNotActivated() {
        return !activation;
    }

    public void setActivation(boolean activation) {
        this.activation = activation;
    }
}
