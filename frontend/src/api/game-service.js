import AuthService from "./auth-service";

const API_URL = "http://localhost:8080/api";

const GAMES_URL = API_URL + "/games";

const GameService = {

    play: function() {
        return fetch(GAMES_URL + '/play', {
            body: null,
            credentials: 'same-origin',
            headers: {
                'Authorization': 'Bearer ' + AuthService.getCurrentPlayer().token
            },
            method: 'POST',
            mode: 'cors'
        })
        .then((response) => {
            if (response.ok) {
              return response.json();
            }
            throw new Error(response.json()?.message);
        })
        .then((data) => {
            return data
        })
        .catch((error) => {
            console.error(error);
        });
    },

    rollDiceById: function(gameId, diceToRoll) {
        return fetch(GAMES_URL + '/' + gameId + '/roll', {
            body: JSON.stringify({ diceToRoll: diceToRoll }),
            credentials: 'same-origin',
            headers: {
                'Authorization': 'Bearer ' + AuthService.getCurrentPlayer().token,
                'content-type': 'application/json'
            },
            method: 'PUT',
            mode: 'cors'
        })
        .then((response) => {
            if (response.ok) {
              return response.json();
            }
            throw new Error(response.json()?.message);
        })
        .then((data) => {
            return data
        });
    },

    fillBoxById: function(gameId, columnType, boxType) {
        return fetch(GAMES_URL + '/' + gameId + '/fill', {
            body: JSON.stringify({ columnType: columnType, boxType: boxType }),
            credentials: 'same-origin',
            headers: {
                'Authorization': 'Bearer ' + AuthService.getCurrentPlayer().token,
                'content-type': 'application/json'
            },
            method: 'PUT',
            mode: 'cors'
        })
        .then((response) => {
            if (response.ok) {
              return response.json();
            }
            throw new Error(response.json()?.message);
        })
        .then((data) => {
            return data
        });
    },

    makeAnnouncementById: function(gameId, boxType) {
        return fetch(GAMES_URL + '/' + gameId + '/announce', {
            body: JSON.stringify({ boxType: boxType }),
            credentials: 'same-origin',
            headers: {
                'Authorization': 'Bearer ' + AuthService.getCurrentPlayer().token,
                'content-type': 'application/json'
            },
            method: 'PUT',
            mode: 'cors'
        })
        .then((response) => {
            if (response.ok) {
              return response.json();
            }
            throw new Error(response.json()?.message);
        })
        .then((data) => {
            return data
        });
    },

    restartById: async function(gameId) {
        return fetch(GAMES_URL + '/' + gameId + '/restart', {
            body: null,
            credentials: 'same-origin',
            headers: {
                'Authorization': 'Bearer ' + AuthService.getCurrentPlayer().token,
            },
            method: 'PUT',
            mode: 'cors'
        })
        .then((response) => {
            if (response.ok) {
              return response.json();
            }
            throw new Error(response.json()?.message);
        })
        .then((data) => {
            return data
        });
    }
}

export default GameService;