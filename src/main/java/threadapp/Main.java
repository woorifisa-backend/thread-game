package threadapp;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("⚔️ ===== THREAD RAID START ===== ⚔️");
        System.out.println("👉 ENTER를 누르면 공격합니다!\n");

        GameState state = new GameState();

        Thread inputThread = new Thread(new InputTask(state), "InputThread");
//        Thread bossThread = new Thread(new BossTask(state), "BossThread");
//        Thread regenThread = new Thread(new RegenTask(state), "RegenThread");

        
        Thread renderThread = new Thread(new RenderTask(state), "RenderThread");
        
        renderThread.start();
        inputThread.start();
//        bossThread.start();
//        regenThread.start();

        inputThread.join();
//        bossThread.join();
//        regenThread.join();
        renderThread.interrupt();

        System.out.println("\n🏁 ===== GAME OVER =====");
    }
}
