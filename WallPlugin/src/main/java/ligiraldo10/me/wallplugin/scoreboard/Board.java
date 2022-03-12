package ligiraldo10.me.wallplugin.scoreboard;

import ligiraldo10.me.wallplugin.WallPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;

public class Board {
    public static ScoreboardManager manager = Bukkit.getScoreboardManager();
    public static Scoreboard scoreboard = manager.getNewScoreboard();
    public static ArrayList<Team> teams = new ArrayList<>();

    public static void createBoard() {
        Objective obj = scoreboard.registerNewObjective("TEAMS", "dummy", "The Walls");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Team red = scoreboard.registerNewTeam("Red");
        red.setAllowFriendlyFire(false);
        red.setColor(ChatColor.RED);
        red.setSuffix(ChatColor.RED + " [Red]");
        teams.add(red);
        Team blue = scoreboard.registerNewTeam("Blue");
        blue.setColor(ChatColor.BLUE);
        blue.setSuffix(ChatColor.BLUE + " [Blue]");
        blue.setAllowFriendlyFire(false);
        teams.add(blue);
        Team yellow = scoreboard.registerNewTeam("Yellow");
        yellow.setColor(ChatColor.YELLOW);
        yellow.setSuffix(ChatColor.YELLOW + " [Yellow]");
        yellow.setAllowFriendlyFire(false);
        teams.add(yellow);
        Team green = scoreboard.registerNewTeam("Green");
        green.setColor(ChatColor.GREEN);
        green.setAllowFriendlyFire(false);
        green.setSuffix(ChatColor.GREEN + " [Green]");
        teams.add(green);
    }

    public static int prevTime = WallPlugin.gameTime / 20;
    public static int prevWallTime = WallPlugin.wallTime / 20;
    public static int prevR;
    public static int prevG;
    public static int prevB;
    public static int prevY;

    public static void updateScoreBoard() {
        Objective obj = scoreboard.getObjective("TEAMS");
        if (prevR != scoreboard.getTeam("Red").getSize()) {
            scoreboard.resetScores(ChatColor.RED + "Red Team: " + prevR);
        }
        Score red = obj.getScore(ChatColor.RED + "Red Team: " + scoreboard.getTeam("Red").getSize());
        prevR = scoreboard.getTeam("Red").getSize();

        if (prevG != scoreboard.getTeam("Green").getSize()) {
            scoreboard.resetScores(ChatColor.GREEN + "Green Team: " +prevG );
        }
        Score green = obj.getScore(ChatColor.GREEN + "Green Team: " + scoreboard.getTeam("Green").getSize());
        prevG = scoreboard.getTeam("Green").getSize();

        if (prevB != scoreboard.getTeam("Blue").getSize()) {
            scoreboard.resetScores(ChatColor.BLUE + "Blue Team: " + prevB);
        }
        Score blue = obj.getScore(ChatColor.BLUE + "Blue Team: " + scoreboard.getTeam("Blue").getSize());
        prevB = scoreboard.getTeam("Blue").getSize();

        if (prevY != scoreboard.getTeam("Yellow").getSize()) {
            scoreboard.resetScores(ChatColor.YELLOW + "Yellow Team: " + prevY);
        }
        Score yellow = obj.getScore(ChatColor.YELLOW + "Yellow Team: " + scoreboard.getTeam("Yellow").getSize());
        prevY = scoreboard.getTeam("Yellow").getSize();

        yellow.setScore(1);
        green.setScore(1);
        red.setScore(1);
        blue.setScore(1);

        //wall time
        if (prevWallTime != WallPlugin.wallTime / 20) {
            int minutes = prevWallTime * 20 / 1200;
            int seconds = (prevWallTime * 20 - minutes * 1200) / 20;
            String msg = minutes + ":" + seconds + " until the walls drop!";
            scoreboard.resetScores(ChatColor.GOLD + msg);
        }
        int minutes1 = WallPlugin.wallTime / 1200;
        int seconds1 = (WallPlugin.wallTime - minutes1 * 1200) / 20;
        String sent = minutes1 + ":" + seconds1 + " until the walls drop!";
        Score wallTimeLeft = obj.getScore(ChatColor.GOLD + sent);
        wallTimeLeft.setScore(0);
        prevWallTime = WallPlugin.wallTime / 20;


        //Game time
        if (prevTime != WallPlugin.gameTime / 20) {
            String msg;
            int minutes = prevTime * 20 / 1200;
            int seconds = (prevTime * 20 - minutes * 1200) / 20;
            msg = minutes + ":" + seconds + " remaining!";
            scoreboard.resetScores(ChatColor.GOLD + msg);
        }
        String msg;
        int minutes = WallPlugin.gameTime / 1200;
        int seconds = (WallPlugin.gameTime - minutes * 1200) / 20;
        msg = minutes + ":" + seconds + " remaining!";
        Score timeleft = obj.getScore(ChatColor.GOLD + msg);
        timeleft.setScore(0);
        prevTime = WallPlugin.gameTime / 20;


    }


}
