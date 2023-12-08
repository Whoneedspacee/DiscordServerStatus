import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener {

    private static JDA api = null;
    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        this.saveConfig();
        String token = this.getConfig().getString("bot-token");
        if(token == null || token.equals("")) {
            this.getConfig().set("bot-token", "");
            this.saveConfig();
            return;
        }
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                api = JDABuilder.createDefault(token).build();
                api.getPresence().setStatus(OnlineStatus.ONLINE);
                updatePlayerCount(Bukkit.getOnlinePlayers().size());
            }
        };
        runnable.runTaskAsynchronously(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        updatePlayerCount(Bukkit.getOnlinePlayers().size());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        updatePlayerCount(Bukkit.getOnlinePlayers().size() - 1);
    }

    public static void updatePlayerCount(int count) {
        if(api == null) {
            return;
        }
        String status_string = count + "/" + Bukkit.getMaxPlayers() + " players online";
        api.getPresence().setActivity(Activity.playing(status_string));
    }

}
