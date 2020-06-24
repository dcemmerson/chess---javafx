/*	filename: RemotePlayerMoveService.java
 * 	last modified: 06/24/2020
 * 	description: MoveService child class. RemotePlayerMoveService manages and
 * 					makes move received from remote player in a separate thread, as 
 * 					to not block GUI. This could just be done in main thread, but
 * 					since we need to create separate tasks to allow cpu to make
 * 					moves without blocking main thread (as not doing so would 
 * 					seriously impact performance), we are performing remote
 * 					player moves in a separate thread as well for streamlined code.
 */

package threading;

import java.util.concurrent.locks.Lock;

import controller.MoveProperties;
import data.Game;
import data.Player;
import gui.ChessBoard;

public class RemoteMoveService extends MoveService {
	private int fromX;
	private int fromY;
	private int toX;
	private int toY;
	
	public RemoteMoveService(Game g, ChessBoard cb, Player p, Lock l, int fromX, int fromY, int toX, int toY) {
		super(g, cb, p, l);
		
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}

	/*	name: startMoveThread
	 * 	description: Implements abstract method in parent class. Uses a reentrant 
	 * 					lock to synchronize moves and ensure local player is
	 * 					not allowed to move out of turn. Back-end chess data
	 * 					classes would block a player moving out of turn, so the
	 * 					lock isn't entirely necessary but it works well with 
	 * 					synchronizing cpu moves in CpuMoveService class.
	 * 
	 * 				This method is nearly identical to LocalPlayerMoveService.startMoveThread,
	 * 					except no need for defining interface that signals when the move was
	 * 					successfully made (since we already know the move that the remote
	 * 					player made, as we just received this information from ChessDataPacket
	 * 					received through the socket). Additionally, we are creating two separate
	 * 					classes for each of LocalPlayerMoveService and RemotePlayerMoveService
	 * 					to allow for potentially additional features to be added.
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
				mp = chessboard.remotePlayerMakeMove(player, this.fromX, this.fromY, this.toX, this.toY);
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
