package threadapp;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameState {

    // 1. 공유 자원 (Primitive Type 사용)
    private int playerHp;
    private int bossHp;
    private final int MAX_PLAYER_HP = 100;
    private final int MAX_BOSS_HP = 200;
    private volatile boolean playerStunned = false; // 유저 기절
    private volatile boolean bossStunned = false; // 보스 기절
    
    private boolean isRunning = true;

    // 2. 로그 큐 (렌더링 스레드가 가져다 씀)
    // 큐 자체는 스레드 안전한 것을 쓰는 게 좋습니다 (synchronized 블록 밖에서도 접근하므로)
    private Queue<String> logQueue = new ConcurrentLinkedQueue<>();

    public GameState() {
        this.playerHp = MAX_PLAYER_HP;
        this.bossHp = MAX_BOSS_HP;
    }

    // ========================================================
    //  핵심 로직: 상태 변경 (WRITE) - 반드시 synchronized 필요
    // ========================================================

    // 플레이어가 보스를 공격
    public synchronized void attackBoss(int dmg) {
        if (!isRunning) return; // 게임 끝났으면 무시

        this.bossHp -= dmg;
        
        // HP 보정 (음수 방지)
        if (this.bossHp < 0) this.bossHp = 0;

        // 게임 종료 체크 (이 부분이 Atomic으로는 하기 힘든 부분)
        if (this.bossHp == 0) {
            isRunning = false;
            addLog("🎉 VICTORY! 보스를 처치했습니다!");
        }
    }

    // 보스가 플레이어를 공격
    public synchronized void attackPlayer(int dmg) {
        if (!isRunning) return;

        this.playerHp -= dmg;

        if (this.playerHp < 0) this.playerHp = 0;

        if (this.playerHp == 0) {
            isRunning = false;
            addLog("💀 GAME OVER! 당신은 사망했습니다...");
        }
    }

    // 플레이어 회복
    public synchronized void healPlayer(int amount) {
        if (!isRunning) return;

        this.playerHp += amount;
        
        // 최대 체력 초과 방지
        if (this.playerHp > MAX_PLAYER_HP) {
            this.playerHp = MAX_PLAYER_HP;
        }
    }

    // 로그 추가 (로그는 자주 발생하므로 별도 동기화 처리 대신 ConcurrentQueue 사용)
    public void addLog(String msg) {
        logQueue.offer(msg);
        // 로그가 너무 많이 쌓이면 오래된 것 삭제 (메모리 관리 & 화면 깔끔함)
        if (logQueue.size() > 6) {
            logQueue.poll();
        }
    }

    // ========================================================
    //  조회 로직 (READ) - 읽을 때도 synchronized 필요
    //  (쓰는 도중에 읽으면 엉뚱한 값을 읽을 수 있음 - 가시성 문제)
    // ========================================================

    public synchronized int getPlayerHp() {
        return playerHp;
    }

    public synchronized int getBossHp() {
        return bossHp;
    }
    
    public synchronized int getMaxPlayerHp() {
        return MAX_PLAYER_HP;
    }
    
    public synchronized int getMaxBossHp() {
        return MAX_BOSS_HP;
    }

    public synchronized boolean isRunning() {
        return isRunning;
    }
    
    public synchronized void setPlayerStunned(boolean stunned) {
    	this.playerStunned = stunned;
    }
    public synchronized boolean isPlayerStunned() {
    	return playerStunned;
    }
    
    public synchronized void setBossStunned(boolean stunned) {
    	this.bossStunned = stunned;
    }
    public synchronized boolean isBossStunned() {
    	return bossStunned;
    }
    
    public void stop() {
		isRunning = false;
	}

	// 큐는 그 자체로 Thread-safe 하므로 그냥 반환
	public Queue<String> getLogQueue() {
		return logQueue;
	}
}