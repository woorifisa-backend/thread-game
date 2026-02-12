# ⚔️ Java Multi-threaded Raid Game

자바의 **멀티스레딩(Multi-threading)**과 **동기화(Synchronization)** 기법을 활용하여 제작한 실시간 콘솔 액션 RPG 게임입니다. 유저, 보스, 힐러, 렌더러가 각각 독립된 스레드에서 상호작용하며 실시간 전투 경험을 제공합니다.

## 🚀 Key Features

### 1. 정교한 멀티스레드 설계
- **Thread-Safe Shared Resource**: 모든 스레드가 `GameState`라는 공유 객체를 참조하며, `synchronized`와 `volatile`을 통해 데이터 무결성을 유지합니다.
- **Non-blocking Logging**: `ConcurrentLinkedQueue`를 사용하여 로그 기록 스레드와 출력 스레드 간의 병목 현상을 해결했습니다.
- **Asynchronous Logic**: 유저의 공격 대기 중에도 보스의 공격과 힐러의 회복은 독립적으로 진행됩니다.

### 3. 기절(Stun) 및 상태 이상 시스템
- **동적 스레드 생성**: 크리티컬 공격 발생 시 별도의 타이머 스레드를 즉석에서 생성하여 일정 시간 후 상태를 원복시키는 비동기 로직을 구현했습니다.

---

## 📂 프로젝트 구조

| 클래스 | 역할 | 핵심 기술 |
| :--- | :--- | :--- |
| **`Main`** | 스레드 생명주기 관리 | `Thread.start()`, `join()`, `interrupt()` |
| **`GameState`** | 공유 자원 및 동기화 메서드 | `synchronized`, `volatile`, `ConcurrentLinkedQueue` |
| **`InputTask`** | 유저 입력 및 공격 처리 | `Scanner`, `Condition Branching` |
| **`BossTask`** | 보스 AI 루프 및 공격 | `Thread.sleep()`, `Random Probability` |
| **`RenderTask`** | UI/UX 렌더링 엔진 | `ANSI Escape Codes`, `String Formatting` |
| **`RegenTask`** | 주기적인 자동 회복 | `Periodic Task Execution` |

---

## 🎮 게임 가이드

1. **공격**: `Enter` 키를 입력하여 보스를 공격합니다.
2. **크리티컬**: 유저 데미지가 **27 이상**일 경우 보스는 **3초간 기절**합니다.
3. **유저 기절**: 보스의 공격력에 따라 유저가 **2초간 기절**하며, 이때는 `Enter`를 입력해도 공격이 나가지 않습니다.
4. **승리 조건**: 보스의 HP를 0으로 만들면 승리합니다.
5. **패배 조건**: 유저의 HP가 0이 되면 게임이 종료됩니다.
