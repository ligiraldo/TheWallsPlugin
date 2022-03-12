package ligiraldo10.me.wallplugin;

import ligiraldo10.me.wallplugin.events.Event;
import ligiraldo10.me.wallplugin.events.StoreBlocks;
import ligiraldo10.me.wallplugin.events.Wall;
import ligiraldo10.me.wallplugin.scoreboard.Board;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class WallPlugin extends JavaPlugin {
    public static String WallsWorld = "WallsWorld";
    public static final int TOTAL_GAME_TIME = 15*60*20;
    public static final int TOTAL_WALL_TIME = 7*60*20;
    public static int gameTime = TOTAL_GAME_TIME;
    public static int wallTime = TOTAL_WALL_TIME;
    public static boolean started;
    public Wall[] walls;

    public ConfigFile WallFile;

    @Override
    public void onEnable() {
        registerEvents();
        Board.createBoard();
        walls = new Wall[8];
        this.WallFile = new ConfigFile(this, "walls");
        new BukkitRunnable(){
            @Override
            public void run(){
                if (Bukkit.getWorld(WallsWorld) != null) {
                    World world = Bukkit.getWorld(WallsWorld);
                    for (int i = 0; i < world.getPlayers().size(); i++) {
                        Player player = world.getPlayers().get(i);
                        Board.updateScoreBoard();
                        player.setScoreboard(Board.scoreboard);
                    }
                }
            }
        }.runTaskTimer(this, 120, 1);

        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerEvents(){
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new Event(this), this);
        pm.registerEvents(new StoreBlocks(this), this);
    }

}
