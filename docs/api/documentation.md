# API Documentation
- [Authorization](#authorization)
  - [User Login](#user-login)
  - [User Registration](#user-registration)
  - [Anonymous Player Creation](#anonymous-player-creation)
- [Games](#games)
  - [Get Game by ID](#get-game-by-id)
  - [Get All Games](#get-all-games)
  - [Create or Get Game for Player](#create-or-get-game-for-player)
  - [Roll Dice in a Game](#roll-dice-in-a-game)
  - [Announce Game Box Type](#announce-game-box-type)
  - [Fill Game Box with Roll](#fill-game-box-with-roll)
  - [Restart Game](#restart-game)
  - [Finish Game](#finish-game)
  - [Complete Game as Admin](#complete-game-as-admin)
- [Players](#players)
  - [Get Player by ID](#get-player-by-id)
  - [Get All Players](#get-all-players)
  - [Get Global Player Stats](#get-global-player-stats)
  - [Get Player Scores](#get-player-scores)
  - [Get Player Stats by ID](#get-player-stats-by-id)
  - [Get Player Preferences by ID](#get-player-preferences-by-id)
  - [Update Player Preferences](#update-player-preferences)
  - [Delete Inactive Players](#delete-inactive-players)
- [Scores](#scores)
  - [Get Score by ID](#get-score-by-id)
  - [Get All Scores](#get-all-scores)
  - [Get Global Score Stats](#get-global-score-stats)
- [Logs](#logs)
  - [Get Log by ID](#get-log-by-id)
  - [Get All Logs](#get-all-logs)
  - [Delete Log by ID](#delete-log-by-id)
  - [Delete All Logs](#delete-all-logs)

## Authorization

### User Login
Logs in a user.

- **Endpoint**: POST `/api/auth/login`
- **Request Body**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Responses**:
  | Status Code    | Return Type           | Description                   |
  |----------------|-----------------------|-------------------------------|
  | 200 OK         | AuthResponse          | Successful login              |
  | 400 Bad Request| ErrorResponse         | Invalid username or password  |
  
### User Registration
Registers a new user.

- **Endpoint**: POST `/api/auth/register`
- **Request Body**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Responses**:
  | Status Code    | Return Type           | Description                   |
  |----------------|-----------------------|-------------------------------|
  | 201 Created    | Player                | User successfully registered  |
  | 400 Bad Request| ErrorResponse         | Registration Data invalid     |
  
### Anonymous Player Creation
Creates an anonymous player.

- **Endpoint**: POST `/api/auth/anonymous`
- **Request Body**:
  ```json
  {
    "username": "string"
  }
  ```
- **Responses**:
  | Status Code    | Return Type           | Description                   |
  |----------------|-----------------------|-------------------------------|
  | 201 Created    | AuthResponse          | Anonymous player created      |
  | 400 Bad Request| ErrorResponse         | Username invalid or taken     |

---

### Games

#### Get Game by ID
Fetches a game by its ID.

- **Endpoint**: GET `/api/games/{id}`
- **Responses**:
  | Status Code | Return Type | Description |
  | --- | --- | --- |
  | 200 OK | Game | Game successfully retrieved |
  | 404 Not Found | ErrorResponse | Game not found with the provided ID |

#### Get All Games
Fetches a list of all games.

- **Endpoint**: GET `/api/games`
- **Responses**:
  | Status Code | Return Type | Description |
  | --- | --- | --- |
  | 200 OK | List of Games | All games successfully retrieved |

#### Create or Get Game for Player
Creates a new game for the player or retrieves an existing one.

- **Endpoint**: PUT `/api/games`
- **Request Body**:
  ```json
  {
    "playerId": "string"
  }
  ```
- **Responses**:
  | Status Code | Return Type | Description |
  | --- | --- | --- |
  | 200 OK | Game | Existing Game retrieved |
  | 201 CREATED | Game | New Game created |
  | 400 Bad Request | ErrorResponse | Invalid Request Body |
  | 401 Unauthorized | ErrorResponse | Invalid Authorization Header |
  | 403 Forbidden | ErrorResponse | Provided playerId does not match current user |
  | 404 Not Found | ErrorResponse | Game not found with the provided ID |

#### Roll Dice in a Game
Rolls the dice for a specific game.

- **Endpoint**: PUT `/api/games/{id}/roll`
- **Request Body**:
  ```json
  {
    "diceToRoll": [1, 2, 3, 4, 5]
  }
  ```
- **Responses**:
  | Status Code | Return Type | Description |
  | --- | --- | --- |
  | 200 OK | Game | Dice successfully rolled, game updated |
  | 400 Bad Request | ErrorResponse | Invalid dice selection or roll parameters |
  | 401 Unauthorized | ErrorResponse | Invalid Authorization Header |
  | 403 Forbidden | ErrorResponse | Game does not belong to the current user |
  | 404 Not Found | ErrorResponse | Game not found with the provided ID |
  | 423 Locked | ErrorResponse | Game is completed or finished |

#### Announce Game Box Type
Announces a box type for the player's move in the game.

- **Endpoint**: PUT `/api/games/{id}/announce`
- **Request Body**:
  ```json
  {
    "boxType": "string"
  }
  ```
- **Responses**:
  | Status Code | Return Type | Description |
  | --- | --- | --- |
  | 200 OK | Game | Box type successfully announced |
  | 400 Bad Request | ErrorResponse | Invalid box type or announcement parameters |
  | 401 Unauthorized | ErrorResponse | Invalid Authorization Header |
  | 403 Forbidden | ErrorResponse | Game does not belong to the current user |
  | 404 Not Found | ErrorResponse | Game not found with the provided ID |
  | 409 Conflict | ErrorResponse | Announcement already made or invalid timing |
  | 423 Locked | ErrorResponse | Game is completed or finished |

#### Fill Game Box with Roll
Fills a box with a rolled result in the game.

- **Endpoint**: PUT `/api/games/{id}/fill`
- **Request Body**:
  ```json
  {
    "columnType": "string",
    "boxType": "string"
  }
  ```
- **Responses**:
  | Status Code | Return Type | Description |
  | --- | --- | --- |
  | 200 OK | Game | Box filled with roll, game updated |
  | 400 Bad Request | ErrorResponse | Invalid column or box type provided |
  | 401 Unauthorized | ErrorResponse | Invalid Authorization Header |
  | 403 Forbidden | ErrorResponse | Game does not belong to the current user |
  | 404 Not Found | ErrorResponse | Game not found with the provided ID |
  | 409 Conflict | ErrorResponse | Box already filled or not available |
  | 423 Locked | ErrorResponse | Game is completed or finished |

#### Restart Game
Restarts the game.

- **Endpoint**: PUT `/api/games/{id}/restart`
- **Responses**:
  | Status Code | Return Type | Description |
  | --- | --- | --- |
  | 200 OK | Game | Game successfully restarted |
  | 403 Forbidden | ErrorResponse | Game does not belong to the current user |
  | 404 Not Found | ErrorResponse | Game not found with the provided ID |
  | 423 Locked | ErrorResponse | Game is completed or finished |

#### Finish Game
Marks the game as finished.

- **Endpoint**: PUT `/api/games/{id}/finish`
- **Responses**:
  | Status Code | Return Type | Description |
  | --- | --- | --- |
  | 200 OK | Game | Game successfully finished |
  | 400 Bad Request | ErrorResponse | Game not in a valid state for finishing |
  | 403 Forbidden | ErrorResponse | Game does not belong to the current user |
  | 404 Not Found | ErrorResponse | Game not found with the provided ID |
  | 409 Conflict | ErrorResponse | Game is not completed |
  | 423 Locked | ErrorResponse | Game is completed or finished |

---

### Players

#### Get Player by ID
Fetches a player by their ID.

- **Endpoint**: GET `/api/players/{id}`
- **Response**:
  - 200 OK: Returns player information.

#### Get All Players
Fetches a list of all players.

- **Endpoint**: GET `/api/players`
- **Response**:
  - 200 OK: Returns a list of players.

#### Get Global Player Stats
Fetches global player statistics.

- **Endpoint**: GET `/api/players/stats`
- **Response**:
  - 200 OK: Returns global statistics across all players.

#### Get Player Scores
Fetches a list of a player's scores.

- **Endpoint**: GET `/api/players/{id}/scores`
- **Response**:
  - 200 OK: Returns a list of the player's scores.

#### Get Player Stats by ID
Fetches statistics for a specific player by their ID.

- **Endpoint**: GET `/api/players/{id}/stats`
- **Response**:
  - 200 OK: Returns player statistics.

#### Get Player Preferences by ID
Fetches a player's preferences by their ID.

- **Endpoint**: GET `/api/players/{id}/preferences`
- **Response**:
  - 200 OK: Returns player preferences.

#### Update Player Preferences
Updates the preferences for a player.

- **Endpoint**: PUT `/api/players/{id}/preferences`
- **Request Body**:
  ```json
  {
    "preferenceKey": "preferenceValue"
  }
  ```
- **Response**:
  - 200 OK: Player preferences updated.

#### Change Player Username
Changes the username for a specific player.

- **Endpoint**: PUT `/api/players/{id}/username`
- **Request Body**:
  ```json
  {
    "username": "newUsername"
  }
  ```
- **Response**:
  - 200 OK: Username updated.

#### Delete Inactive Players
Deletes inactive player accounts.

- **Endpoint**: DELETE `/api/players/inactive`
- **Response**:
  - 204 No Content: Inactive players deleted.

---

### Scores

#### Get Score by ID
Fetches a specific score by its ID.

- **Endpoint**: GET `/api/scores/{id}`
- **Response**:
  - 200 OK: Returns the score information.

#### Get All Scores
Fetches all scores across players.

- **Endpoint**: GET `/api/scores`
- **Response**:
  - 200 OK: Returns a list of scores.

#### Get Global Score Stats
Fetches global score statistics.

- **Endpoint**: GET `/api/scores/stats`
- **Response**:
  - 200 OK: Returns global statistics across all scores.

---

### Logs

#### Get Log by ID
Fetches a log entry by its ID (admin-only).

- **Endpoint**: GET `/api/logs/{id}`
- **Response**:
  - 200 OK: Returns the log entry.

#### Get All Logs
Fetches all log entries (admin-only).

- **Endpoint**: GET `/api/logs`
- **Response**:
  - 200 OK: Returns a list of logs.

#### Delete Log by ID
Deletes a specific log entry by its ID (admin-only).

- **Endpoint**: DELETE `/api/logs/{id}`
- **Response**:
  - 204 No Content: Log entry deleted.

#### Delete All Logs
Deletes all log entries (admin-only).

- **Endpoint**: DELETE `/api/logs`
- **Response**:
  - 204 No Content: All log entries deleted.