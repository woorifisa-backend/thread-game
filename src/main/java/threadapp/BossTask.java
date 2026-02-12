package threadapp;

public class BossTask implements Runnable {
	private GameState gameState;

	public BossTask(GameState gameState) {
		super();
		this.gameState = gameState;
	}

	@Override
	public void run() {
		while (gameState.isRunning()) {
			try {
				Thread.sleep(1500);

				if (gameState.isBossStunned()) {
					gameState.addLog("\u001B[33m😵 [보스] 으악! 기절했습니다.. (Boss HP: " + gameState.getBossHp() + ")\u001B[0m");
					Thread.sleep(1500);
					continue;
				}

				int dmg = (int) (Math.random() * 30) + 20;
				if (dmg >= 35) {
					gameState.attackPlayer(dmg);
					gameState.addLog("\u001B[31m🔥 [보스->유저] 공격! -" + dmg + "HP (User HP: " + gameState.getPlayerHp() + ")\u001B[0m");

					gameState.setPlayerStunned(true);
					new Thread(() -> {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						gameState.setPlayerStunned(false);
						gameState.addLog("\u001B[33m⚠️ 유저가 깨어났습니다!\u001B[0m");
					}).start();

				} else {
					gameState.attackPlayer(dmg);
					gameState.addLog("⚔️ [보스->유저] 공격! -" + dmg + "HP (User HP: " + gameState.getPlayerHp() + ")");
				}
			} catch (InterruptedException e) {
				break;
			}
			
			if (gameState.getPlayerHp() == 0) {
				gameState.stop();
				gameState.addLog("💀 GAME OVER! 당신은 사망했습니다...");
			}
		}

	}

}
