package com.zornchris.moctruthtables;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.material.Lever;
import org.bukkit.plugin.Plugin;

import com.zornchris.moctruthtables.events.ScenarioResultEvent;


public class TruthTable {

	public Block[] inputBlocks = new Block[3];
	public Block outputBlock;
	public String file;
	private ArrayList<String> lines;
	private Plugin plugin;
	
	public TruthTable(Plugin p) { plugin = p; }
	
	public void evaluate()
	{
		lines = new ArrayList<String>();
		if(file != null && file.length() > 0) {
			try {
				Scanner scn = new Scanner(new File("plugins/MOCTruthTables/" + file + ".txt"));
				while(scn.hasNextLine()) 
					lines.add(scn.nextLine());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			Task t = new Task(this);
			t.run();
		}
	}
	
	/**
     * Sets the powered state of the input blocks to
     * how they are described in the line and compares the actual
     * output to the expected.
     * @param line      the description of input and output values
     * @return          whether the actual output matches the expected
     */
    
    public boolean checkOutput(String line) {
        int lastIndex = line.length() - 1;
        char output = line.charAt(lastIndex);
        System.out.println("Output: " + output + " Result: " + outputBlock.isBlockPowered());
        return (outputBlock.isBlockPowered() ? output == '1' : output == '0');
    }
    
    public void setBlocks(String line) {
        System.out.println("Setting up : " + line);
        int lastIndex = line.length() - 1;
        line = line.substring(0, lastIndex);
        
        for(int i = 0; i < lastIndex; i++) {
            
            // Toggle the lever if we needed to
            Block target = inputBlocks[i];
            Lever l = (Lever)inputBlocks[i].getState().getData();
            
            if((line.charAt(i) == '1' && !l.isPowered()) || (line.charAt(i) == '0' && l.isPowered()))
                net.minecraft.server.Block.byId[target.getType().getId()].interact(((CraftWorld)target.getWorld()).getHandle(), target.getX(), target.getY(), target.getZ(), null);
        }
    }
	
	@Override
	public String toString() {
	    String x = "Inputs: [";
	    for(int i = 0; i < inputBlocks.length; i++) 
	        x += (inputBlocks[i] != null) ? i : '_' + " ";
	    x += "]";
	    x += " Output: " + ((outputBlock != null) ? '1' : '_');
	    
	    return x;
	}
	
	private class Task implements Runnable {

	    private int currentIndex;
	    private final int DELAY = 10;
	    private TruthTable parent;
	    
	    public Task(TruthTable p) {
	        currentIndex = -1;
	        parent = p;
	    }
	    
        @Override
        public void run() {
            
            // If we've already setup the first test
            if(currentIndex > -1) {
                boolean result = checkOutput(lines.get(currentIndex));
                
                // If the current test fails
                if(!result) {
                    System.out.println("Fail");
                    plugin.getServer().getPluginManager().callEvent(new ScenarioResultEvent(false, parent));
                    return;
                }
                // If the test passes
                else {
                    currentIndex++;
                    
                    // If there are more tests, setup the blocks
                    if(currentIndex < lines.size()) {
                        setBlocks(lines.get(currentIndex));
                        plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, this, DELAY);
                    }
                    
                    // Otherwise, we've passed them all
                    else {
                        System.out.println("Pass");
                        plugin.getServer().getPluginManager().callEvent(new ScenarioResultEvent(true, parent));
                    }
                }
            }
            
            // The first test
            else {
                currentIndex++;
                setBlocks(lines.get(currentIndex));                
                plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, this, DELAY);
            }
        }
	    
	}
}
