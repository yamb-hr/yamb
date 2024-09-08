# [ 5. Class Documentation ](../documentation.md)

## 5.2 Business

### 5.2.1 Interfaces

#### GameService

##### GameService Class Diagram

```mermaid
classDiagram

    GameController --> GameService
    GameService <|-- GameServiceImpl : implements

    class GameService {
        <<Interface>>
        +GameResponse getById(String)
        +List~GameResponse~ getAll()
        +GameResponse getOrCreate(Long)
        +GameResponse rollById(String, int[])
        +GameResponse announceById(String, BoxType)
        +GameResponse fillById(String, ColumnType, BoxType)
        +GameResponse restartById(String)
        +GameResponse finishById(String)
        +GameResponse completeById(String)
    }

```

---

#### PlayerService

##### PlayerService Class Diagram

```mermaid
classDiagram

    PlayerController --> PlayerService
    PlayerService <|-- PlayerServiceImpl : imlements

    class PlayerService {
        <<Interface>>
        +PlayerResponse getById(Long)
        +List~PlayerResponse~ getAll()
        +GlobalPlayerStatsResponse getGlobalStats()
        +List~ShortScoreResponse~ getScoresByPlayerId(Long)
        +PlayerStatsResponse getPlayerStats(Long)
        +PlayerPreferencesResponse getPreferencesByPlayerId(Long)
        +PlayerPreferencesResponse setPreferencesByPlayerId(Long, PlayerPreferences)
        +PlayerResponse changeUsername(Long, String)
        +void deleteInactivePlayers()
    }

```

---

#### ScoreService

##### ScoreService Class Diagram

```mermaid
classDiagram

    ScoreController --> ScoreService
    ScoreService <|-- ScoreServiceImpl : implements

    class ScoreService {
        <<Interface>>
        +ScoreResponse getById(Long)
        +List~ScoreResponse~ getAll()
        +GlobalScoreStatsResponse getGlobalStats()
    }

```
---

#### LogService

##### LogService Class Diagram

```mermaid
classDiagram

    LogController --> LogService
    LogService <|-- LogServiceImpl : implements

    class LogService {
        <<Interface>>
        +LogResponse getById(Long)
        +List~LogResponse~ getAll()
        +void deleteById(Long)
        +void deleteAll()
    }

```

---

#### AuthService

##### AuthService Class Diagram

```mermaid
classDiagram

    AuthController --> AuthService
    AuthService <|-- AuthServiceImpl : implements

    class AuthService {
        <<Interface>>
        +AuthResponse login(String, String)
        +RegisteredPlayer register(String, String)
        +AuthResponse createAnonymousPlayer(String)
        +void changePassword(String, String)
    }

```

---

#### RecaptchaService

##### RecaptchaService Class Diagram

```mermaid
classDiagram

    AuthController --> RecaptchaService
    RecaptchaService <|-- RecaptchaServiceImpl : implements

    class RecaptchaService {
        <<Interface>>
        +boolean verifyRecaptcha(String)
    }

```