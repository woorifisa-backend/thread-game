package threadapp;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameState {

	private int playerHp;
	private int bossHp;
	private final int MAX_PLAYER_HP = 100;
	private final int MAX_BOSS_HP = 500;
	private boolean playerStunned = false; // 유저 기절
	private boolean bossStunned = false; // 보스 기절
	private volatile int bossStunVersion = 0;

	private boolean isRunning = true;

	private Queue<String> logQueue = new ConcurrentLinkedQueue<>();

	public GameState() {
		this.playerHp = MAX_PLAYER_HP;
		this.bossHp = MAX_BOSS_HP;
	}

	// 플레이어가 보스를 공격
	public synchronized void attackBoss(int dmg) {
		if (!isRunning)
			return; 

		this.bossHp -= dmg;

		// HP 보정 (음수 방지)
		if (this.bossHp < 0)
			this.bossHp = 0;
	}

	// 보스가 플레이어를 공격
	public synchronized void attackPlayer(int dmg) {
		if (!isRunning)
			return;

		this.playerHp -= dmg;

		if (this.playerHp < 0)
			this.playerHp = 0;
	}
	
	// 최신 스턴인지 판별
	public synchronized int stunBossAndGetVersion() {
	    bossStunned = true;
	    bossStunVersion++;
	    return bossStunVersion;
	}
	
	// 최신 스턴이 있으면 해제 금지
	public synchronized boolean tryClearBossStun(int version) {
	    if (bossStunVersion != version) return false;
	    bossStunned = false;
	    return true;
	}

	// 플레이어 회복
	public synchronized void healPlayer(int amount) {
		if (!isRunning)
			return;

		this.playerHp += amount;

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

	public Queue<String> getLogQueue() {
		return logQueue;
	}
}