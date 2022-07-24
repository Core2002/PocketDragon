package fun.fifu.pocketdragon;

import com.alkaidmc.alkaid.bukkit.event.AlkaidEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PocketDragon extends JavaPlugin implements Listener {
    public static PocketDragon plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        new AlkaidEvent(plugin).simple()
                .event(PlayerJoinEvent.class)
                .listener(event -> {
                    
                })
                .priority(EventPriority.HIGHEST)
                .ignore(false)
                .register();

    }

    @Override
    public void onDisable() {
        getLogger().info("感谢使用本插件");
    }

}
