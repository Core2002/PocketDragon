package fun.fifu.pocketdragon;

import com.alkaidmc.alkaid.bukkit.command.AlkaidCommand;
import com.ericdebouwer.petdragon.PetDragon;
import com.ericdebouwer.petdragon.api.PetDragonAPI;
import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PocketDragon extends JavaPlugin implements Listener {
    public static PocketDragon plugin;

    private static final PetDragonAPI dragonAPI = PetDragonAPI.getInstance();

    @Override
    public void onLoad() {
        plugin = this;
    }

    private static final PetDragon pl = JavaPlugin.getPlugin(PetDragon.class);

    @Override
    public void onEnable() {
        new AlkaidCommand(plugin).simple()
                .command("RecyclePetDragon")
                .description("回收龙")
                .permission("petdragon.bypass.remove")
                .usage("/RecyclePetDragon")
                .aliases(List.of("rpd", "回收龙"))
                .executor((sender, command, label, args) -> {
                    Player player = (Player) sender;
                    recyclePetDragon(player);
                    return true;
                })
                .register();
    }

    @Override
    public void onDisable() {
        getLogger().info("感谢使用本插件");
    }

    public void recyclePetDragon(Player player) {
        boolean found = false;
        EnderDragon dragon = null;
        int range = 6;
        List<Entity> nearbyEnds = (List<Entity>) player.getWorld().getNearbyEntities(
                player.getLocation(), range, range, range, (e) -> pl.getFactory().isPetDragon(e));
        nearbyEnds.sort(Comparator.comparingDouble((e) ->
                e.getLocation().distanceSquared(player.getLocation())));

        if (!nearbyEnds.isEmpty()) {
            dragon = (EnderDragon) nearbyEnds.get(0);
            UUID owner = pl.getFactory().getOwner(dragon);
            if (!player.hasPermission("petdragon.bypass.remove") && !player.getUniqueId().equals(owner)) {
                assert owner != null;
                player.sendMessage("你无权收回该龙！");
                return;
            }
            found = true;
        } else {
            player.sendMessage("太远了捏~");
        }

        if (found) {
            if (Objects.equals(dragonAPI.getOwningPlayer(dragon), player.getUniqueId())) {
                PlayerInventory inventory = player.getInventory();
                inventory.getItemInMainHand();
                if (inventory.getItemInMainHand().getType() == Material.AIR) {
                    dragon.remove();
                    inventory.addItem(pl.getCustomItems().getEgg());
                    player.sendMessage("你收回了龙");
                } else {
                    player.sendMessage("请空手回收嗷");
                }
            }
        }

    }

}
