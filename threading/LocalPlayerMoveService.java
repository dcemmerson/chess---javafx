/*	filename: LocalPlayerMoveService.java
 * 	last modified: 06/24/2020
 * 	description: MoveService child class. LocalPlayerMoveService manages a
 * 					local player making a move in a separate thread, as 
 * 					to not block GUI. This could just be done in main thread, but
 * 					since we need to create separate tasks to allow cpu to make
 * 					moves without blocking main thread (as not doing so would 
 * 					seriously impact performance), we are performing local
 * 					player moves in a separate thread as well for streamlined code.
 */

package threading;

import java.util.concurrent.locks.Lock;

import controller.MoveProperties;
import controller.PlayerInterface;
import data.Game;
import data.Player;
import gui.ChessBoard;

public class LocalPlayerMoveService extends MoveService {
	boolean hasMoved;
	
	public LocalPlayerMoveService(Game g, ChessBoard cb, Player p, Lock l) {
		super(g, cb, p, l);
	}
	
	/*	name: startMoveThread
	 * 	description: Implements abstract method in parent class. Uses a reentrant 
	 * 					lock to synchronize moves and ensure local player is
	 * 					not allowed to move out of turn. Back-end chess data
	 * 					classes would block a player moving out of turn, so the
	 * 					lock isn't entirely necessary but it works well with 
	 * 					synchronizing cpu moves in CpuMoveService class.
	 */
	@Override
	protected MoveProperties startMoveThread() {
		hasMoved = false;

		try {
			while (!game.isEnded() && !lock.isHeldByCurrentThread()) {
				
				lock.lock();

				if (!player.isTurn()) {
					lock.unlock();
					Thread.sleep(500);
				}
			}
			
			if(!game.isEnded()) {
			chessboard.enablePieceActionListeners();
			
			chessboard.setPlayerInterface(localPlayerInterface());
			
				while(!hasMoved) {
					Thread.sleep(100);
				}
			}

		} catch (InterruptedException e) {
			System.out.println("Thread sleep interrupted.");
		} finally {
			lock.unlock();
		}

		return mp;
	}
	
	/*	name: localPlayerInterface
	 * 	description: Defines and returns PlayerInterface method. Allows the
	 * 					ChessBoard class to easily and cleanly signal back to
	 * 					our non-JavaFX thread that the user has made a successful
	 * 					move on chess board and we can now set appropriate flags
	 * 					in this class to end player's turn.
	 */
	private PlayerInterface localPlayerInterface() {
		return new PlayerInterface() {
			
			@Override
			public void signalMoveMade(MoveProperties moveProps) {
				mp = moveProps;
				hasMoved = true;
				chessboard.setPlayerInterface(null);
				chessboard.disablePieceActionListeners();
			}

		};
	}
}
