/*	filename: MoveService.java
 * 	last modified: 06/24/2020
 * 	description: Abstract parent class to CpuMoveService, LocalPlayerMoveService,
 * 					and RemoteMoveService classes. Main purposes is to use
 * 					polymorphism to call startMoveThread in child classes,
 * 					without need to test typeof class in game.
 */

package threading;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import controller.MoveProperties;
import data.Game;
import data.Player;
import gui.ChessBoard;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public abstract class MoveService extends Service<Object> {

	protected final Game game;
	protected final Player player;
	protected final ChessBoard chessboard;
	protected final ReentrantLock lock;
	MoveProperties mp;
	
	public MoveService(Game g, ChessBoard cb, Player p, Lock l) {
		
		this.player = p;
		this.chessboard = cb;
		this.lock = (ReentrantLock) l;
		this.game = g;
	}

	protected abstract MoveProperties startMoveThread();
	
	/*	name: createTask
	 * 	description: Entry point for new task. Call the startMoveThread
	 * 					of the child class.
	 */
	@Override
	protected Task<Object> createTask() {

		return new Task<Object>() {

			@Override
			protected Object call() throws Exception {
				mp = null;
				return startMoveThread();
			}
			
		};

	}
	
}
