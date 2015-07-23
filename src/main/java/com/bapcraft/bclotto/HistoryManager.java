package com.bapcraft.bclotto;

import java.util.ArrayList;

public class HistoryManager {

	public ArrayList<Drawing> oldDrawings;
	
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
		
		oldDrawings.add(oldDraw);
		BCLotto.instance.activeDrawing = new Drawing();
		
		return false; // TODO Finish this.
		
	}
	
	public void init() {
		
	}
	
	public void deinit() {
		
	}
	
}
