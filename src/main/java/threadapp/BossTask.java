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
					gameState.addLog("😵 [보스] 으악! 기절했습니다... zzz");
					Thread.sleep(5000);
					continue;
				}

				int dmg = (int) (Math.random() * 20) + 10;
				if (dmg >= 25) {
					gameState.attackPlayer(dmg);
					gameState.addLog("🔥 [보스] \u001B[33m크리티컬!! " + dmg + "의 데미지!\u001B[0m");

                    gameState.setPlayerStunned(true);
					Thread stunSetting = new Thread(() -> {
						try {
							Thread.sleep(3000);
							
							
							 
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						gameState.setPlayerStunned(false);
						gameState.addLog("플레이어가 정신을 차렸습니다.");
					});

				} else {
					gameState.attackPlayer(dmg);
					gameState.addLog("👊 [보스] \u001B[33m공격! " + dmg + "의 데미지.\u001B[0m");
				}
			} catch (InterruptedException e) {
				break;
			}
		}

	}

	private void sleep(int i) {
		// TODO Auto-generated method stub
		
	}

}
