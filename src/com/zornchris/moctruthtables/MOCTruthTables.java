package com.zornchris.moctruthtables;

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

public class MOCTruthTables extends JavaPlugin {
	private HashMap<String, TruthTable> truthTables = new HashMap<String, TruthTable>();

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
    	    Player p = (Player) sender;
    	    
    	    /* '/moctt set 1 a' */
    		if(args.length > 2 && args[0].equalsIgnoreCase("set")) {
    			
    			Block lookingAt = p.getTargetBlock(null, 100);
    			System.out.println(lookingAt);
    			
    			char block = args[2].charAt(0);
    			char scenario = args[1].charAt(0);
    			
    			TruthTable current = truthTables.get(String.valueOf(scenario));
    			if(current == null) {
    				current = new TruthTable(this);
    			}
    			
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
    				case 'o':
    				case 'O':
    					current.outputBlock = lookingAt;
    					break;
    				default:
    				    p.sendMessage("[MOCTT] Block not set");
    				    return true;
    			}
    			if(block == 'o' || block == 'O')
    			    p.sendMessage("[MOCTT] Set Block as the output in scenario " + scenario);
    			else
    			    p.sendMessage("[MOCTT] Set Block as input " + block + " in scenario " + scenario);
    			truthTables.put(String.valueOf(scenario), current);
    			System.out.println(current);
    		}
    		/* '/moctt test 1 and' */
    		else if( args.length > 2 && args[0].equalsIgnoreCase("test") ) {
    		    TruthTable t = truthTables.get(args[1]);
    		    System.out.println(t);
    		    t.file = args[2];
    		    t.evaluate();
    		}
    		else
    		    p.sendMessage("[moctt] Invalid arguments.");
    		
    		return true;
    	}
    	return false; 
    }
    
    private class TTPlayerListener extends PlayerListener { 
    	
    }
}