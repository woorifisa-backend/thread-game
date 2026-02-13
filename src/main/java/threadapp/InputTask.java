package threadapp;

import java.util.Random;
import java.util.Scanner;

public class InputTask implements Runnable {
	private final GameState state;
	private final Scanner scanner = new Scanner(System.in);
	private final Random random = new Random();

	public InputTask(GameState state) {
		this.state = state;
	}

	@Override
	public void run() {
		state.addLog("🎮 게임 시작! Enter를 눌러 공격하세요!");

		while (state.isRunning()) {

			scanner.nextLine();

			if (!state.isRunning())
				break;

			// 유저 기절 체크 (기절 중엔 입력을 막고 대기)
			if (state.isPlayerStunned()) {
				state.addLog("\u001B[33m😵 [유저] 기절중...... zzz (User HP: " + state.getPlayerHp() + ")\u001B[0m");
				continue;
			}

			// 데미지 계산 및 공격 실행
			int damage = 10 + random.nextInt(21);
			state.attackBoss(damage);

			// 크리티컬 (27 이상) - 보스 스턴(마지막 크리티컬 기준으로 연장)
			if (damage >= 27) {
				state.addLog(
						"\u001B[31m🔥 [유저->보스] 공격! -" + damage + "HP (Boss HP: " + state.getBossHp() + ")\u001B[0m");

				int version = state.stunBossAndGetVersion();

				new Thread(() -> {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException ignored) {
					}

					boolean cleared = state.tryClearBossStun(version);
					if (cleared) {
						state.addLog("\u001B[33m⚠️ 보스가 기절에서 깨어났습니다!\u001B[0m");
					}
				}, "BossStunTimer-" + version).start();

			} else {
				state.addLog("⚔️ [유저->보스] 공격! -" + damage + "HP (Boss HP: " + state.getBossHp() + ")");
			}

			if (state.getBossHp() == 0) {
				state.stop();
				state.addLog("🎉 VICTORY! 보스를 처치했습니다!");
			}
		}
	}
}
