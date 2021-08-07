package uk.co.hopperelec.mc.dsmpantimountain;

import com.pg85.otg.interfaces.ICachedBiomeProvider;
import com.pg85.otg.spigot.gen.OTGSpigotChunkGen;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {
    ICachedBiomeProvider worldBiomes;

    @Override
    public void onEnable() {
        final World world = Bukkit.getWorld("world");
        if (world == null) {
            this.setEnabled(false);
            return;
        }

        final OTGSpigotChunkGen chunkGen = ((OTGSpigotChunkGen) world.getGenerator());
        if(chunkGen == null) {
            this.setEnabled(false);
            return;
        }
        worldBiomes = chunkGen.generator.getCachedBiomeProvider();

        getServer().getPluginManager().registerEvents(this,this);
    }

    public boolean testBlock(Block block) {
        if (block.getWorld().getName().equals("world"))
            return worldBiomes.getBiomeConfig(block.getX(), block.getZ()).getName().equals("Extreme Hills+");
        return false;
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (testBlock(event.getBlock()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (testBlock(event.getBlock())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You cannot break blocks in the surrounding mountains");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (testBlock(event.getBlock())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You cannot place blocks in the surrounding mountains");
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            if (testBlock(block)) event.setCancelled(true);
        }
    }
}