package threadapp;

import java.util.concurrent.atomic.AtomicInteger;

public class GameState {

	private AtomicInteger bossHp = new AtomicInteger(200);
	private AtomicInteger playerHp = new AtomicInteger(100);

	private volatile boolean running = true;

	public int attackBoss(int dmg) {
		return bossHp.addAndGet(-dmg);
	}

	public int attackPlayer(int dmg) {
		return playerHp.addAndGet(-dmg);
	}

	public int healPlayer(int amount) {
		return playerHp.addAndGet(amount);
	}

	public int getBossHp() {
		return bossHp.get();
	}

	public int getPlayerHp() {
		return playerHp.get();
	}

	public boolean isRunning() {
		return running;
	}

	public void stop() {
		running = false;
	}
}
