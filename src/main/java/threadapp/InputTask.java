package threadapp;

import java.util.Scanner;
import java.util.Random;

public class InputTask implements Runnable {
    private final GameState state;
    private final Scanner scanner = new Scanner(System.in);
    private final Random random = new Random();

    public InputTask(GameState state) {
        this.state = state;
    }

    @Override
    public void run() {
        // 게임 시작 시 전광판에 메시지 표시
        state.addLog("🎮 게임 시작! Enter를 눌러 공격하세요!");

        while (state.isRunning()) {
            // 1. 유저 기절 체크 (기절 중엔 입력을 막고 대기)
            if (state.isPlayerStunned()) {
                try {
                    Thread.sleep(200); 
                } catch (InterruptedException e) {}
                continue; 
            }

            // 2. 입력 대기 (이게 있어야 사용자가 Enter를 칠 때까지 기다립니다)
            scanner.nextLine();
            if (!state.isRunning()) break;

            // 3. 데미지 계산 및 공격 실행
            int damage = 10 + random.nextInt(21);
            state.attackBoss(damage);

            // 4. 크리티컬 판정 (25 이상) -> 게임 화면 전광판에 출력
            if (damage >= 25) {
                state.addLog("🔥 [CRITICAL] 보스가 5초간 기절합니다!");
                
                // 보스 기절 타이머 스레드
                new Thread(() -> {
                    state.setBossStunned(true);
                    try { Thread.sleep(5000); } catch (InterruptedException e) {}
                    state.setBossStunned(false);
                    state.addLog("⚠️ 보스가 기절에서 깨어났습니다!");
                }).start();
            } else {
                // 일반 공격 메시지를 전광판(로그 큐)으로 전송
                state.addLog("⚔️ 유저 공격! -" + damage + "hp (Boss HP: " + state.getBossHp() + ")");
            }

            // 5. 내 개발용 콘솔에는 간단히 표시 (선택 사항)
            System.out.println("Log sent to Game Screen: -" + damage);
        }
    }
}