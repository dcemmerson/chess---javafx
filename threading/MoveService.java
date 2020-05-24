package threading;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import controller.MoveProperties;
import data.Game;
import data.Player;
import gui.ChessBoard;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class MoveService extends Service<Object> {

	protected final Game game;
	protected final Player player;
	protected final ChessBoard chessboard;
	protected final ReentrantLock lock;
//	boolean obtainedLock = false;
	MoveProperties mp;
	
	public MoveService(Game g, ChessBoard cb, Player p, Lock l) {
		
		this.player = p;
		this.chessboard = cb;
		this.lock = (ReentrantLock) l;
		this.game = g;
	}

	protected MoveProperties startMoveThread() {
		System.out.println("wrong startmovethread method");
		return null;
	}
	
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

	protected String getGameOverMsg() {
		String str = "Game over!\n";
		
		// If it's white's turn and we got here, that means why has no moves and is in checkmate.
		if(player.isWhite()) {
			str += "Black wins!";
		}
		else {
			str += "White wins!";
		}
		
		return str;
	}

	
}
