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
        // 게임이 시작되었음을 알림
        while (state.isRunning()) {
            // 1. 사용자 입력 대기 (Enter)
            scanner.nextLine(); 

            // 2. 입력 대기 중에 게임이 끝났는지 체크
            if (!state.isRunning()) break;

            // 3. 데미지 계산 (가중치 로직: 10 ~ 30 랜덤)
            int damage = 10 + random.nextInt(21); 

            // 4. GameState를 통해 보스 공격
            int currentBossHp = state.attackBoss(damage);

            // 5. 결과 출력
            System.out.printf("⚔️ [USER ATTACK] %d의 데미지! (남은 Boss HP: %d)\n", 
                               damage, (currentBossHp < 0 ? 0 : currentBossHp));

            // 6. 승리 조건 확인
            if (currentBossHp <= 0) {
                System.out.println("\n✨ 축하합니다! 보스가 쓰러졌습니다! ✨");
                state.stop(); // 전체 스레드 중단 신호
            }
        }
    }
}