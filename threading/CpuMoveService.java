package threading;

import java.util.concurrent.locks.Lock;

import controller.MoveProperties;
import data.Game;
import data.Player;
import gui.ChessBoard;

public class CpuMoveService extends MoveService {

	public CpuMoveService(Game g, ChessBoard cb, Player p, Lock l) {
		super(g, cb, p, l);
	}

	@Override
	protected MoveProperties startMoveThread() {

		try {

			while (!game.isEnded() && !lock.isHeldByCurrentThread()) {

				lock.lock();

				
				if (!player.isTurn()) {
					lock.unlock();
					Thread.sleep(200);
				}
				Thread.sleep(500);

			}
			if (!game.isEnded()) {
				mp = chessboard.cpuMakeMove(player);
				System.out.println("after chessboard cpumakemove");
			}
			else {
				
//				String gameOverStr = getGameOverMsg();

//				mp = new MoveProperties(gameOverStr, null);
			}

		} catch (InterruptedException e) {
			System.out.println("Thread sleep interrupted.");
		} 
		finally {
			lock.unlock();
		}

		return mp;
	}

}
