package ligiraldo10.me.wallplugin.events;

import ligiraldo10.me.wallplugin.WallPlugin;
import ligiraldo10.me.wallplugin.scoreboard.Board;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import java.util.ArrayList;
import java.util.List;


public class Event implements Listener {
    private final WallPlugin plugin;

    public Event(WallPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        World world = Bukkit.getWorld(WallPlugin.WallsWorld);
        if (!player.getWorld().equals(world)) return;
        if (Board.scoreboard.getTeam("Red").hasPlayer(player)) {
            Board.scoreboard.getTeam("Red").removePlayer(player);
            player.setGameMode(GameMode.SPECTATOR);
        }
        if (Board.scoreboard.getTeam("Yellow").removePlayer(player)) {
            Board.scoreboard.getTeam("Yellow").removePlayer(player);
            player.setGameMode(GameMode.SPECTATOR);
        }
        if (Board.scoreboard.getTeam("Blue").hasPlayer(player)) {
            Board.scoreboard.getTeam("Blue").removePlayer(player);
            player.setGameMode(GameMode.SPECTATOR);
        }
        if (Board.scoreboard.getTeam("Green").hasPlayer(player)) {
            Board.scoreboard.getTeam("Green").removePlayer(player);
            player.setGameMode(GameMode.SPECTATOR);
        }
        gameOver();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        World world = Bukkit.getWorld(WallPlugin.WallsWorld);
        if (!player.getWorld().equals(world)) return;
        if (Board.scoreboard.getTeam("Red").hasPlayer(player)) {
            Board.scoreboard.getTeam("Red").removePlayer(player);
        }
        if (Board.scoreboard.getTeam("Yellow").removePlayer(player)) {
            Board.scoreboard.getTeam("Yellow").removePlayer(player);
        }
        if (Board.scoreboard.getTeam("Blue").hasPlayer(player)) {
            Board.scoreboard.getTeam("Blue").removePlayer(player);
        }
        if (Board.scoreboard.getTeam("Green").hasPlayer(player)) {
            Board.scoreboard.getTeam("Green").removePlayer(player);

        }
        gameOver();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = Bukkit.getWorld(WallPlugin.WallsWorld);
        if (player.getWorld().equals(world)) {
            player.teleport(Bukkit.getWorld("Spawn").getSpawnLocation());
        }
    }

    private boolean starting = false;

    @EventHandler
    public void onPlayerJoin(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = Bukkit.getWorld(WallPlugin.WallsWorld);
        if (player.getWorld().equals(world)) {
            player.getInventory().clear();
            if (world.getPlayers().size() >= 4) {
                if (WallPlugin.started || starting) return;
                starting = true;
                new BukkitRunnable() {
                    private int time = 15 * 20;

                    @Override
                    public void run() {
                        if (time % 20 == 0) {
                            for (Player p : world.getPlayers()) {
                                p.sendTitle("Round will start in " + time / 20 + " Seconds", "", 3, 20, 0);
                            }
                        }
                        if (world.getPlayers().size() < 2) {
                            starting = false;
                            this.cancel();
                            return;
                        }
                        if (time <= 0) {
                            starting = false;
                            startGame(world);
                            this.cancel();
                            return;
                        }
                        time--;
                    }
                }.runTaskTimer(plugin, 1, 1);
            }
        }
    }

    private void startGame(World world) {
        int team = 0;
        for (int i = 0; i < world.getPlayers().size(); i++) {
            Player player = world.getPlayers().get(i);
            player.setScoreboard(Board.scoreboard);
            player.setHealth(20);
            player.setFoodLevel(20);
            if (team == 0) {
                Board.scoreboard.getTeam("Red").addPlayer(player);
                player.teleport(new Location(world, -84, 57, -37));
            }
            if (team == 1) {
                Board.scoreboard.getTeam("Blue").addPlayer(player);
                player.teleport(new Location(world, 35, 57, -37));
            }
            if (team == 2) {
                Board.scoreboard.getTeam("Yellow").addPlayer(player);
                player.teleport(new Location(world, -84, 57, 83));
            }
            if (team == 3) {
                Board.scoreboard.getTeam("Green").addPlayer(player);
                player.teleport(new Location(world, 35, 57, 83));
            }
            team++;

            if (team > 3)
                team = 0;
        }
        kitPlayer();
        Board.updateScoreBoard();
        WallPlugin.started = true;
        timeOut();
        for (int i = 1; i <= 8; i++) {
            int x = plugin.WallFile.getConfig().getInt("walls.wall" + i + ".x1");
            int y = plugin.WallFile.getConfig().getInt("walls.wall" + i + ".y1");
            int z = plugin.WallFile.getConfig().getInt("walls.wall" + i + ".z1");
            int x1 = plugin.WallFile.getConfig().getInt("walls.wall" + i + ".x2");
            int y1 = plugin.WallFile.getConfig().getInt("walls.wall" + i + ".y2");
            int z1 = plugin.WallFile.getConfig().getInt("walls.wall" + i + ".z2");
            int height = plugin.WallFile.getConfig().getInt("walls.wall" + i + ".height");
            String mat = plugin.WallFile.getConfig().getString("walls.wall" + i + ".material");
            Location loc1 = new Location(world, x, y, z);
            Location loc2 = new Location(world, x1, y1, z1);
            List<Location> locs = new ArrayList<>();
            locs.add(loc2);
            locs.add(loc1);
            if (x1 != x) {
                int max = Math.max(x1, x);
                int min = Math.min(x1, x);
                for (int tx = min; tx <= max; tx++) {
                    locs.add(new Location(world, tx, y, z));
                }
            } else {
                int max = Math.max(z1, z);
                int min = Math.min(z1, z);
                for (int tz = min; tz <= max; tz++) {
                    locs.add(new Location(world, x, y, tz));
                }
            }
            plugin.walls[i-1] = new Wall(Material.matchMaterial(mat), locs, height);
            plugin.walls[i-1].build();
        }
        dropWalls();
    }


    private void timeOut() {
        World world = Bukkit.getWorld(WallPlugin.WallsWorld);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!WallPlugin.started) {
                    this.cancel();
                    return;
                }
                if (WallPlugin.gameTime < 0) {
                    sendMessageAll("No one wins! :(", true);
                    for (int i = 0; i < Board.teams.size(); i++) {
                        Team team = Board.teams.get(i);
                        if (team.getSize() != 0) {
                            for (int j = 0; j < world.getPlayers().size(); j++) {
                                Player player = world.getPlayers().get(j);
                                if (team.hasPlayer(player))
                                    team.removePlayer(player);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (player.isOnline()) {
                                            player.teleport(Bukkit.getWorld("Spawn").getSpawnLocation());
                                            player.getInventory().clear();
                                            player.setGameMode(GameMode.SURVIVAL);
                                        }
                                    }
                                }.runTaskLater(plugin, 5 * 20);
                            }
                        }
                    }
                    WallPlugin.started = false;
                    WallPlugin.gameTime = WallPlugin.TOTAL_GAME_TIME;
                    WallPlugin.wallTime = WallPlugin.TOTAL_WALL_TIME;
                    replaceMap();
                    killEntities();
                    this.cancel();
                    return;
                }
                WallPlugin.gameTime--;
            }
        }.runTaskTimer(plugin, 1, 1);
    }

    private void dropWalls(){
        new BukkitRunnable(){
            @Override
            public void run(){
                if (!WallPlugin.started){
                    this.cancel();
                    return;
                }
                if (WallPlugin.wallTime <= 1){
                    for (Wall wall : plugin.walls) {
                        wall.destroy();

                    }
                    sendMessageAll("The Walls Have Fallen!", true);
                    this.cancel();
                }else WallPlugin.wallTime--;
            }
        }.runTaskTimer(plugin, 1, 1);
    }

    private void sendMessageAll(String message, boolean title) {
        World world = Bukkit.getWorld(WallPlugin.WallsWorld);
        for (int i = 0; i < world.getPlayers().size(); i++) {
            Player player = world.getPlayers().get(i);
            if (title)
                player.sendTitle(message, "", 0, 40, 0);
            else player.sendMessage(message);
        }
    }

    private void gameOver() {
        int teamElim = 0;
        World world = Bukkit.getWorld(WallPlugin.WallsWorld);
        if (Board.scoreboard.getTeam("Red").getSize() == 0) teamElim++;
        if (Board.scoreboard.getTeam("Blue").getSize() == 0) teamElim++;
        if (Board.scoreboard.getTeam("Yellow").getSize() == 0) teamElim++;
        if (Board.scoreboard.getTeam("Green").getSize() == 0) teamElim++;
        if (teamElim == 3) {
            for (int i = 0; i < Board.teams.size(); i++) {
                Team team = Board.teams.get(i);
                if (team.getSize() != 0) {
                    for (int j = 0; j < world.getPlayers().size(); j++) {
                        Player player = world.getPlayers().get(j);
                        if (team.hasPlayer(player))
                            team.removePlayer(player);
                        player.sendTitle(team.getColor() + team.getName() + " Won", "", 0, 40, 0);
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                if (player.isOnline()) {
                                    player.teleport(Bukkit.getWorld("Spawn").getSpawnLocation());
                                    player.setGameMode(GameMode.SURVIVAL);
                                    player.getInventory().clear();
                                }
                            }
                        }.runTaskLater(plugin, 5 * 20);
                    }
                    WallPlugin.started = false;
                    WallPlugin.gameTime = WallPlugin.TOTAL_GAME_TIME;
                    WallPlugin.wallTime = WallPlugin.TOTAL_WALL_TIME;
                    replaceMap();
                    gameOver();
                    killEntities();
                }
            }
        }
    }

    private void replaceMap() {
        for (Location loc : StoreBlocks.editedblocks.keySet()) {
            loc.getBlock().setType(StoreBlocks.editedblocks.get(loc));
        }
        StoreBlocks.editedblocks.clear();
    }

    private void killEntities() {
        new BukkitRunnable() {
            @Override
            public void run() {
                World world = Bukkit.getWorld(WallPlugin.WallsWorld);
                for (int i = 0; i < world.getEntities().size(); i++) {
                    Entity entity = world.getEntities().get(i);
                    if (entity instanceof Item) {
                        Item item = (Item) entity;
                        item.setTicksLived(900000);
                    }
                }
            }
        }.runTaskLater(plugin, 20);

    }

    private void kitPlayer(){
        World world = Bukkit.getWorld(WallPlugin.WallsWorld);
        for(int i = 0; i < world.getPlayers().size(); i++){
            Player player = world.getPlayers().get(i);
            player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
            player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
            player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
            player.getInventory().addItem(new ItemStack(Material.STONE_SHOVEL));
            player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
            player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
            player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        }
    }
}

