package ligiraldo10.me.wallplugin.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Wall {
    private final List<Location> baseLocs;
    private List<Block> blocks;
    private int height;
    private Material type;

    public Wall(Material type, List<Location> baseLocs, int height){
        this.baseLocs = baseLocs;
        this.height = height;
        this.type = type;
        blocks = new ArrayList<>();
    }

    public void build(){
        for (int locIndex = 0; locIndex < baseLocs.size(); locIndex++) {
            Location entry = baseLocs.get(locIndex).clone();
            for (int i = 0; i < height; i++) {
                entry.getBlock().setType(type);
                blocks.add(entry.getBlock());
                entry.add(0,1,0);

            }
        }
    }
    public void destroy(){
        for (int i = 0; i < blocks.size(); i++){
            Block block = blocks.get(i);
            block.setType(Material.AIR);
        }
    }


    public void setType(Material type){
        this.type = type;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public List<Location> getBaseLocs(){
        return baseLocs;
    }

    public int getHeight(){
        return height;
    }

    public List<Block> getBlocks(){
        return blocks;
    }
}
