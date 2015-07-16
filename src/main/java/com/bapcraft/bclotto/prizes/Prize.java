package com.bapcraft.bclotto.prizes;

import java.util.UUID;

import com.bapcraft.bclotto.Drawing;

public abstract class Prize {

	public Prize() {
		
	}
	
	public abstract void onWin(Drawing pot, UUID winner);
	
}
