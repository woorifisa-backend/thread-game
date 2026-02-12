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
        while (state.isRunning()) {
        	
        	// 1. 유저 기절 체크
        	if (state.isPlayerStunned()) {
                try {
                	System.out.println("기절 중");
                    Thread.sleep(200); // 1초마다 기절 풀렸는지 체크
                } catch (InterruptedException e) {}
                continue; 
            }
            
            // 1. 엔터 입력 대기
            scanner.nextLine();

            // 2. 게임 중단 여부 재확인
            if (!state.isRunning()) break;

            // 3. 랜덤 데미지 계산 (가중치: 10 ~ 30)
            int damage = 10 + random.nextInt(21);

            
            // 4. 25 이상이면 크리티컬 발생 -> 보스 5초 기절
            if (damage >= 25) {
                state.addLog("🔥 CRITICAL! 보스가 5초간 기절합니다!");
                System.out.println(" >> 🔥 [CRITICAL] 강력한 일격! 보스가 기절했습니다!");
                
                // 새로운 스레드를 열어 5초 뒤에 기절을 풀어줌
                new Thread(() -> {
                    state.setBossStunned(true);
                    try { Thread.sleep(5000); } catch (InterruptedException e) {}
                    state.setBossStunned(false);
                    state.addLog("⚠️ 보스가 정신을 차렸습니다!");
                }).start();
            }
            
            // 5. 보스 공격 (새로운 GameState 방식: synchronized 메서드 호출)
            state.attackBoss(damage);

            // 6. 로그 추가 (이제 GameState가 로그 큐를 관리하므로 직접 메시지 전달)
            state.addLog("💥 유저 공격: -" + damage + "(남은 HP: " + state.getBossHp() + ")");

            // 7. 콘솔에 즉시 표시 (로그 큐와 별개로 사용자 피드백 제공)
            System.out.printf(" >> ⚔️ [공격 성공!] 보스에게 %d의 데미지를 입혔습니다! (남은 HP: %d)\n", 
                    damage, state.getBossHp());
        }
    }
}

