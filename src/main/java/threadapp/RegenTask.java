package threadapp;

public class RegenTask implements Runnable {

    private final GameState state;

    private static final int HEAL_AMOUNT = 20;
    private static final int INTERVAL_MS = 4000;

    public RegenTask(GameState state) {
        this.state = state;
    }

    @Override
    public void run() {
        state.addLog("\u001B[36m💖 힐러 출격! 4초마다 플레이어를 회복합니다.\u001B[0m");

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

            state.addLog("\u001B[36m💖 [힐러] HP +" + (after - before) + " 회복!\u001B[0m");
        }
    }
}
