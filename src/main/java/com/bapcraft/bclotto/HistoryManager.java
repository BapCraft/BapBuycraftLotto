package com.bapcraft.bclotto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;

import org.bukkit.Bukkit;

public class HistoryManager {

	public static final String HISTORY_FILE_NAME = "history.xml";
	
	public ArrayList<Drawing> drawHistory; // Includes current.
	public File historyFile;
	
	/**
	 * <summary>
	 * Sets up a new drawing, throwing away the old one.
	 * </summary>
	 * 
	 * @return Success.
	 */
	public boolean setupNewDrawing() {
		
		Drawing oldDraw = BCLotto.instance.activeDrawing;
		
		if (oldDraw.state == Drawing.DrawingState.READY) oldDraw.state = Drawing.DrawingState.CANCELLED;
		
		drawHistory.add(oldDraw);
		BCLotto.instance.activeDrawing = new Drawing();
		
		return false; // TODO Finish this.
		
	}
	
	/**
	 * <summary>
	 * Reads the history from the file, and parses it into the proper history ArrayList;
	 * </summary>
	 */
	private void readFromFile() {
		// TODO This logic.
	}
	
	/**
	 * <summary>
	 * Copies the drawing history to file, in the data folder. (XML)
	 * </summary>
	 */
	public void flushHistoryToFile() {
		
		Element root = new Element("lottery");
		Document xml = new Document(root);
		
		// Make all the history of drawings.
		for (int i = 0; i < this.drawHistory.size(); i++) {
			
			Drawing cur = this.drawHistory.get(i);
			Element drawEle = new Element("drawing");
			
			drawEle.addAttribute(new Attribute("state", cur.state.name));
			drawEle.addAttribute(new Attribute("start", Long.toString(cur.startTime)));
			drawEle.addAttribute(new Attribute("done", Long.toString(cur.drawTime)));
			
			xml.appendChild(drawEle);
			
			// This *does* make multiple identical element tags, but that's ok, I guess...
			for (int j = 0; j < cur.pot.size(); j++) {
				
				Ticket t = cur.pot.get(i);
				Element ticketEle = new Element("ticket");
				
				ticketEle.appendChild(t.player.toString());
				if (t.player == cur.getWinner_PASSIVE()) ticketEle.addAttribute(new Attribute("winner", "true"));
				
				drawEle.appendChild(ticketEle);
				
			}
			
		}
		
		// Prepare to write the file.
		String xmlData = xml.toXML();
		FileOutputStream fos = null;
		
		// Crazy huge try-catch-finally block.
		try {
			
			fos = new FileOutputStream(this.historyFile);
			
			// Write the actual data.
			for (int i = 0; i < xmlData.length(); i++) {
				fos.write((int) xmlData.charAt(i));
			}
			
			fos.close();
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace(); // I don't know what I should do at this point.
			}
			
		}
		
		fos = null;
		
	}
	
	/**
	 * Begins the process of setting up the history making.
	 */
	public void init() {
		
		this.historyFile = new File(BCLotto.instance.getDataFolder(), HISTORY_FILE_NAME);
		
		if (this.historyFile.exists()) {
			
			// Loads the file, because it exists.
			this.readFromFile();
			
		} else {
			
			// Creates the file, because it doesn't seem to be.
			
			try {
				
				this.historyFile.createNewFile();
				this.setupNewDrawing();
				
			} catch (IOException ioe) {
				
				ioe.printStackTrace();
				Bukkit.shutdown();
				
			}
			
		}
		
		this.flushHistoryToFile();
		
	}
	
	/**
	 * Cleans up, closes file handles, etc.
	 */
	public void deinit() {
		
		this.flushHistoryToFile();
		
	}
	
}
