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
				System.out.println("after chessboard cpumakemove");
			}
			else {
				
				String gameOverStr = getGameOverMsg();

				mp = new MoveProperties(gameOverStr, null);
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
