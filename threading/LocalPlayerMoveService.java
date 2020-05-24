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
				else {
					
					String gameOverStr = getGameOverMsg();

					mp = new MoveProperties(gameOverStr, null);
				}

			}
			chessboard.enablePieceActionListeners();
			
			chessboard.setPlayerInterface(localPlayerInterface());
			
			while(!hasMoved) {
				Thread.sleep(100);
			}
			
		} catch (InterruptedException e) {
			System.out.println("Thread sleep interrupted.");
		} finally {

			System.out.println("finally unlocking");
			lock.unlock();

		}

		return mp;
	}
	
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
