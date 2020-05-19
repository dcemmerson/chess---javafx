package controller;

import java.io.IOException;
import java.util.concurrent.locks.Lock;

import data.Game;
import data.Player;
import gui.ChessBoard;

public class CpuMoveService extends MoveService {

	public CpuMoveService(Game g, ChessBoard cb, Player p, Lock l) {
		super(g, cb, p, l);
		System.out.println("cpu constructor");
	}

	@Override
	protected MoveProperties startMoveThread() {
//		obtainedLock = false;
		try {

			while (!game.isEnded() /* && !obtainedLock */ && !lock.isHeldByCurrentThread()) {

				lock.lock();

				if (!player.isTurn()) {
					lock.unlock();
					Thread.sleep(500);
				}

			}
			if (!game.isEnded()) {
				mp = chessboard.cpuMakeMove(player);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		finally {
			lock.unlock();
		}

		return mp;
	}

}
