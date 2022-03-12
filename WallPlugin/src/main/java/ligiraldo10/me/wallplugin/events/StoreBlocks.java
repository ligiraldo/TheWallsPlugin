package ligiraldo10.me.wallplugin.events;

import ligiraldo10.me.wallplugin.WallPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StoreBlocks implements Listener {
    public static HashMap<Location, Material> editedblocks = new HashMap<>();
    private final WallPlugin plugin;
    private final List<Material> mats;
    private final List<Material> woods;

    public StoreBlocks(WallPlugin plugin) {
        this.plugin = plugin;
        mats = tools();
        woods = stripable();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (WallPlugin.started && event.getPlayer().getWorld().equals(Bukkit.getWorld(WallPlugin.WallsWorld))) {
            Block block = event.getBlock();
            Location bLoc = block.getLocation();
            editedblocks.put(bLoc, event.getBlockReplacedState().getType());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (WallPlugin.started && event.getPlayer().getWorld().equals(Bukkit.getWorld(WallPlugin.WallsWorld))) {
            Block block = event.getBlock();
            event.setCancelled(true);
            for (Wall wall : plugin.walls) {
                if (wall.getBlocks().contains(block))
                    return;

            }

            editedblocks.put(block.getLocation(), block.getType());
            event.setCancelled(false);
        }
    }
    @EventHandler
    public void onLiquidPlace(PlayerBucketEmptyEvent event){
        World world = Bukkit.getWorld(WallPlugin.WallsWorld);
        Player player = event.getPlayer();
        if (player.getWorld().equals(world)){
            Block block = event.getBlock();
            if (editedblocks.containsKey(block.getLocation())) return;
            editedblocks.put(block.getLocation(), block.getType());

        }
    }
    @EventHandler
    public void onLiquidPickUp(PlayerBucketFillEvent event){
        World world = Bukkit.getWorld(WallPlugin.WallsWorld);
        Player player = event.getPlayer();
        if (WallPlugin.started && player.getWorld().equals(world) && !editedblocks.containsKey(event.getBlock().getLocation())){
            ItemStack item = event.getItemStack();
            if (item.getType().equals(Material.WATER_BUCKET)){
                editedblocks.put(event.getBlock().getLocation(), Material.WATER);
            }
            if (item.getType().equals(Material.LAVA_BUCKET)){
                editedblocks.put(event.getBlock().getLocation(), Material.LAVA);
            }
        }
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        if (!event.hasBlock() || !event.hasItem())
            return;
        World world = Bukkit.getWorld(WallPlugin.WallsWorld);
        if (event.getPlayer().getWorld().equals(world) && WallPlugin.started) {

            ItemStack item = event.getItem();
            Block block = event.getClickedBlock();
            if (mats.contains(item.getType()) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                if (block.getType().equals(Material.DIRT) || block.getType().equals(Material.GRASS_BLOCK)){
                    event.setCancelled(true);
                }else if(woods.contains(block.getType())){
                    event.setCancelled(true);
                }

            }



        }
    }

    private List<Material> tools() {
        Material[] tools = new Material[18];
        tools[0] = Material.WOODEN_SHOVEL;
        tools[1] = Material.STONE_SHOVEL;
        tools[2] = Material.IRON_SHOVEL;
        tools[3] = Material.GOLDEN_SHOVEL;
        tools[4] = Material.DIAMOND_SHOVEL;
        tools[5] = Material.NETHERITE_SHOVEL;
        tools[6] = Material.WOODEN_AXE;
        tools[7] = Material.STONE_AXE;
        tools[8] = Material.IRON_AXE;
        tools[9] = Material.GOLDEN_AXE;
        tools[10] = Material.DIAMOND_AXE;
        tools[11] = Material.NETHERITE_AXE;
        tools[12] = Material.WOODEN_HOE;
        tools[13] = Material.STONE_HOE;
        tools[14] = Material.IRON_HOE;
        tools[15] = Material.GOLDEN_HOE;
        tools[16] = Material.DIAMOND_HOE;
        tools[17] = Material.NETHERITE_HOE;
        return Arrays.stream(tools).toList();
    }

    private List<Material> stripable(){
        List<Material> mats = new ArrayList<>();
        mats.add(Material.CRIMSON_HYPHAE);
        mats.add(Material.WARPED_HYPHAE);
        mats.add(Material.ACACIA_LOG);
        mats.add(Material.BIRCH_LOG);
        mats.add(Material.OAK_LOG);
        mats.add(Material.SPRUCE_LOG);
        mats.add(Material.DARK_OAK_LOG);
        mats.add(Material.JUNGLE_LOG);
        return mats;
    }
}

