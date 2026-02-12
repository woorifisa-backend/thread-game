package threadapp;

public class Main {

    public static void main(String[] args) {
        
        // 1. 게임판(공유 자원) 생성
        GameState gameState = new GameState();

        // 2. 각 스레드 객체 생성 (GameState 주입)
        // BossTask는 이전에 작성하신 것 사용 (5초 기절 로직 포함된 것)
        Thread bossThread = new Thread(new BossTask(gameState), "BossThread");
        Thread inputThread = new Thread(new InputTask(gameState), "InputThread");
//        Thread regenThread = new Thread(new RegenTask(gameState), "RegenThread");
        Thread renderThread = new Thread(new RenderTask(gameState), "RenderThread");

        // 3. 스레드 시작
        renderThread.start(); // 화면부터 켜고
        bossThread.start();
//        regenThread.start();
        inputThread.start();  // 입력 대기

        try {
            // 4. 게임이 끝날 때까지 메인 스레드 대기 (InputTask가 끝나면 여기도 풀림)
            inputThread.join();
            
            // 게임 종료 시 다른 스레드들도 정리
            bossThread.interrupt();
//            regenThread.interrupt();
            renderThread.interrupt();
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("메인 스레드 종료.");
    }
}