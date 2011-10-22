
//
import java.util.HashMap;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

public class MOCTruthTable extends JavaPlugin {
	private HashMap<Integer, TruthTable> truthTables = new HashMap<Integer, TruthTable>();

    public void onDisable() {
       System.out.println("Goodbye world!");
    }

    public void onEnable() {
    	//TTPlayerListener playerListener = new TTPlayerListener();
    	PluginManager pm = getServer().getPluginManager();
        
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
    	if(cmd.getName().equalsIgnoreCase("moctt")) {
    		if(args.length > 1 && args[0].equalsIgnoreCase("set")) {
    			Player p = (Player) sender;
    			Block lookingAt = p.getTargetBlock(null, 100);
    			
    			char block = args[1].charAt(0);
    			int num = Integer.parseInt(args[1].substring(1));
    			
    			TruthTable current = truthTables.get(num);
    			if(current == null)
    				current = new TruthTable();
    			
    			switch(block) {
    				case 'a':
    				case 'A':
    					current.inputBlocks[0] = lookingAt;
    					break;
    				case 'b':
    				case 'B':
    					current.inputBlocks[1] = lookingAt;
    					break;
    				case 'c':
    				case 'C':
    					current.inputBlocks[2] = lookingAt;
    					break;
    				case 'd':
    				case 'D':
    					current.outputBlock = lookingAt;
    					break;
    			}
    		}
    		return true;
    	}
    	return false; 
    }
    
    private class TTPlayerListener extends PlayerListener { 
    	
    }
}