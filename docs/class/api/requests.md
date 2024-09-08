# [ 5. Class Documentation ](../documentation)

## 5.3 API

### 5.3.2 Request DTOs

#### AuthRequest
Used for authentication and login purposes.

| Field      | Type   | Description              |
|------------|--------|--------------------------|
| `username` | String | The player's username     |
| `password` | String | The player's password     |
---

#### ActionRequest
Used to perform specific actions within a game, such as rolling dice.

| Field         | Type     | Description                                |
|---------------|----------|--------------------------------------------|
| `diceToRoll`  | int[]    | The indices of the dice to be rolled       |
| `columnType`  | [ColumnType](../domain/enums#columntype)     | The column type related to the action      |
| `boxType`     | BoxType     | The box type related to the action         |
---

#### GameRequest
Used to create or retrieve a game based on a player’s ID.

| Field      | Type   | Description               |
|------------|--------|---------------------------|
| playerId | Long   | The unique ID of the player |
---

#### PlayerPreferencesRequest
Used to set or update player preferences like language and theme.

| Field     | Type   | Description                   |
|-----------|--------|-------------------------------|
| `language`| String | The preferred language of the player |
| `theme`   | String | The preferred theme (e.g., dark or light mode) |
---