/*	filename: CpuMoveService.java
 * 	last modified: 06/24/2020
 * 	description: MoveService child class. CpuMoveService manages cpu
 * 					trying to make a move in a separate thread, as 
 * 					to not block GUI.
 */
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

	/*	name: startMoveThread
	 * 	description: Implements abstract method in parent class. Continually tries
	 * 					to make cpu move (when local player is playing against cpu,
	 * 					or cpu vs cpu). Uses a reentrant lock to synchronize moves
	 * 					and ensure cpu doesn't try to move when it's not it's turn.
	 * 					Moving out of turn would be block by the back-end chess
	 * 					data classes, but continuously trying to make moves out of
	 * 					turn would be a waste of resources. 
	 */
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
