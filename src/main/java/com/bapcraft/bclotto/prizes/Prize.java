package com.bapcraft.bclotto.prizes;

import java.util.UUID;

import com.bapcraft.bclotto.Drawing;

public abstract class Prize {

	public Prize() {
		
	}
	
	/**
	 * <summary>
	 * Handles what to do when a player is selected.  This is only
	 * called once per win, on one subclass instance of class Prize.
	 * </summary>
	 * 
	 * @param pot The Drawing instance that took place.
	 * @param winner The UUID of the playwe that was selected.
	 */
	public abstract void onWin(Drawing pot, UUID winner);
	
}
