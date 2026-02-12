package threadapp;

public class RegenTask implements Runnable {

    private final GameState state;

    private static final int HEAL_AMOUNT = 5;
    private static final int INTERVAL_MS = 2000;

    public RegenTask(GameState state) {
        this.state = state;
    }

    @Override
    public void run() {
        state.addLog("💖 힐러 스레드 출격! 2초마다 플레이어를 회복합니다.");

        while (state.isRunning()) {

            try {
                Thread.sleep(INTERVAL_MS);
            } catch (InterruptedException e) {
                break;
            }

            if (!state.isRunning()) break;

            int before = state.getPlayerHp();
            state.healPlayer(HEAL_AMOUNT); // GameState 내부에서 MAX_PLAYER_HP(100)로 제한
            int after = state.getPlayerHp();

            if (after == before) {
                continue;
            }

            state.addLog("💖 [힐러] HP +" + (after - before) + " 회복!");
        }
    }
}
