package com.bapcraft.bclotto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

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
		
		// Clear preset stuff...
		this.drawHistory = new ArrayList<Drawing>();
		
		Builder b = new Builder();
		Document xml = null;
		
		try {
			xml = b.build(this.historyFile);
		} catch (ParsingException pe) {
			pe.printStackTrace();
			Bukkit.getLogger().severe("Lottery History parsing failed.  Delete the file to fix.");
			Bukkit.shutdown();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			Bukkit.getLogger().severe("Something else happend that should not have when parsing the History.");
			Bukkit.shutdown();
		}
		
		// At this point the xml variable shouldn't be null.
		Element root = xml.getRootElement();
		Elements drawings = root.getChildElements("drawing");
		
		// Elements doesn't implement IIterator... ;_;
		for (int i = 0; i < drawings.size(); i++) {
			
			Drawing theD = new Drawing();
			Element curDraw = drawings.get(i);
			Elements tickets = curDraw.getChildElements("ticket");
			
			theD.state = null;
			theD.startTime = Long.parseLong(curDraw.getAttributeValue("start"));
			theD.drawTime = Long.parseLong(curDraw.getAttributeValue("done"));
			
			// Figure out the state.  Not the most efficient, but it's pretty.
			String stateName = curDraw.getAttributeValue("state");
			for (Drawing.DrawingState test : Drawing.DrawingState.values()) if (test.name.equals(stateName)) theD.state = test; 
			
			for (int j = 0; j < tickets.size(); j++) {
				
				Element t = tickets.get(j);
				UUID parsedUUID = UUID.fromString(t.getChild(0).getValue());
				
				theD.addTicket(new Ticket(parsedUUID)); // Add the ticket.
				
				String win = t.getAttributeValue("winner");
				if ("true".equals(win)) { // Reversed because we aren't sure that `win` is not null.
					theD.winner = parsedUUID; // Should be a text node.
				}
				
			}
			
		}
		
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
				
				Ticket t = cur.pot.get(j);
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
		
		// TODO Something about which prize was recieved.
		
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
