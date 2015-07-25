package com.bapcraft.bclotto.prizes;

import java.util.UUID;

import com.bapcraft.bclotto.Drawing;

public class PrizeNotifyAdmin extends Prize {

	@Override
	public void onWin(Drawing pot, UUID winner) {
		/*
		 * TODO Make it notify an admin.
		 * (This doesn't exactly matter, because everyone finds out.
		 * It was only really for making *sure* an admin/mod finds out.
		 * 
		 */
	}

}
