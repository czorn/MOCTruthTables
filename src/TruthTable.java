import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.bukkit.block.Block;


public class TruthTable {

	public Block[] inputBlocks = new Block[3];
	public Block outputBlock;
	public String file;
	
	public TruthTable() {
		
	}
	
	public boolean evaluate()
	{
		ArrayList<String> lines = new ArrayList<String>();
		if(file != null && file.length() > 0) {
			try {
				Scanner scn = new Scanner(new File("plugins/MOCTruthTable/" + file + ".txt"));
				while(scn.hasNextLine()) 
					lines.add(scn.nextLine());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			for(String line : lines) {
				boolean result = processScenario(line);
				if(result == false)
					return false;
			}
		}
		return true;
	}
	
	/**
	 * Sets the powered state of the input blocks to
	 * how they are described in the line and compares the actual
	 * output to the expected.
	 * @param line		the description of input and output values
	 * @return			whether the actual output matches the expected
	 */
	private boolean processScenario(String line) {
		int lastIndex = line.length() - 1;
		int output = (int)line.charAt(lastIndex);
		
		line = line.substring(0, lastIndex);
		
		for(int i = 0; i < lastIndex; i++) {
			inputBlocks[i].setData((byte) ((line.charAt(i) == '1') ? (inputBlocks[i].getData() | 0x8) : (inputBlocks[i].getData() & ~0x8)));
		}
		
		// One tick from now, or now, check output
		return (outputBlock.isBlockPowered() ? output == 1 : output == 0);
	}
}
