package threadapp;

public class Main {

    public static void main(String[] args) {
        
        GameState gameState = new GameState();

        Thread bossThread = new Thread(new BossTask(gameState), "BossThread");
        Thread inputThread = new Thread(new InputTask(gameState), "InputThread");
        Thread regenThread = new Thread(new RegenTask(gameState), "RegenThread");
        Thread renderThread = new Thread(new RenderTask(gameState), "RenderThread");

        renderThread.start();
        bossThread.start();
        regenThread.start();
        inputThread.start(); 

        try {
            inputThread.join();
            
            // 게임 종료 시 스레드들 정리
            bossThread.interrupt();
            regenThread.interrupt();
            renderThread.interrupt();
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("메인 스레드 종료.");
    }
}