package threadapp;

public class RenderTask implements Runnable {
    private GameState state;
    private StringBuilder buffer = new StringBuilder();

    // ANSI 컬러 코드
    final String RED = "\u001B[31m";
    final String GREEN = "\u001B[32m";
    final String YELLOW = "\u001B[33m";
    final String RESET = "\u001B[0m";
    final String CYAN = "\u001B[36m";
    final String WHITE = "\u001B[37m";

    public RenderTask(GameState state) {
        this.state = state;
    }

    @Override
    public void run() {
        // 커서 숨기기 (터미널 설정)
        System.out.print("\u001B[?25l");

        while (state.isRunning()) {
            try {
                renderFrame();
                Thread.sleep(500); // 20 FPS (0.05초마다 갱신)
            } catch (InterruptedException e) {
                break;
            }
        }

        // 게임 종료 후 마지막 상태 그리기
        renderFrame();

        // 커서 다시 보이기
        System.out.println("\n" + RESET + "엔터를 누르면 종료됩니다...");
        System.out.print("\u001B[?25h");
    }

    private void renderFrame() {
        buffer.setLength(0); // 버퍼 비우기

        // 1. 커서를 (0,0)으로 이동 (화면 지우기 대신 덮어쓰기 -> 깜빡임 방지)
        buffer.append("\u001B[H");

        // 2. 타이틀
        buffer.append(RED + "========================================\n" + RESET);
        buffer.append(RED + "            BOSS RAID (THREAD)          \n" + RESET);
        buffer.append(RED + "========================================\n\n" + RESET);

        // 3. 보스 상태 (기절 시 상태 표시 변경)
        int bHp = state.getBossHp();
        int maxBHp = state.getMaxBossHp();

        String bossStatus = state.isBossStunned() ? YELLOW + "(:dizzy_face: STUNNED!)" + RESET : "";
        buffer.append(" [ BOSS ] HP: " + bHp + " / " + maxBHp + " " + bossStatus + "\n");
        buffer.append(drawBar(bHp, maxBHp, 40, RED)).append("\n\n");

        // 보스 아스키 아트
        if (state.isBossStunned()) {
            buffer.append(YELLOW + "      (  @ . @   )  < 윽...  \n");
            buffer.append("      (    ~     )       \n" + RESET);
        } else {
            buffer.append(CYAN + "      (  `·.¸¸.·´  )      \n");
            buffer.append("      (   O    O   )      \n");
        }
        buffer.append(CYAN + "       (    __    )       \n" + RESET);
        buffer.append("\n\n");

        // 4. 플레이어 상태
        int pHp = state.getPlayerHp();
        int maxPHp = state.getMaxPlayerHp();
        buffer.append(" [ ME ]   HP: " + pHp + " / " + maxPHp + "\n");
        buffer.append(drawBar(pHp, maxPHp, 40, GREEN)).append("\n");

        String status = pHp < 30 ? RED + "DANGER!" : GREEN + "GOOD";
        buffer.append(" Status: " + status + RESET + "\n");

        // 5. 로그 창
        buffer.append("----------------------------------------\n");
        buffer.append(YELLOW + " [ LOG WINDOW ]\n" + RESET);

        // GameState의 로그 큐에서 가져와서 출력
        for (String log : state.getLogQueue()) {
            // 잔상 제거를 위해 공백으로 채움
            String paddedLog = String.format("%-60s", log);
            buffer.append(" > ").append(paddedLog).append("\n");
        }
        buffer.append("----------------------------------------\n");
        buffer.append(" Command: [Enter] 공격! (크리티컬 시 보스 기절)\n");

        // 한 번에 출력 (I/O 성능 최적화)
        System.out.print(buffer.toString());
    }

    // 게이지 바 그리기 유틸
    private String drawBar(int current, int max, int size, String color) {
        if (max <= 0) max = 1; // 0 나누기 방지
        int filled = (int) ((double) current / max * size);
        if (filled < 0) filled = 0;
        if (filled > size) filled = size;

        StringBuilder sb = new StringBuilder();
        sb.append(color).append("[");
        for (int i = 0; i < size; i++) {
            if (i < filled) sb.append("█");
            else sb.append("░");
        }
        sb.append("]").append(RESET);
        return sb.toString();
    }
    
    
}