import AuthService from "./auth-service";

const API_URL = process.env.REACT_APP_API_URL + "/games";

const GameService = {

    getGameById: function(gameId) {
        return fetch(API_URL + '/' + gameId, {
            body: null,
            credentials: 'same-origin',
            headers: {
                'Authorization': 'Bearer ' + AuthService.getCurrentPlayer()?.token
            },
            method: 'GET',
            mode: 'cors'
        })
        .then(response => {
            if (response.ok) {
                return response.json().then(data => data);
            }
            return response.json().then(error => { 
                console.error(error);
                throw new Error(error.message);
            });
        });
    },

    play: function() {
        return fetch(API_URL + '/play', {
            body: null,
            credentials: 'same-origin',
            headers: {
                'Authorization': 'Bearer ' + AuthService.getCurrentPlayer()?.token
            },
            method: 'POST',
            mode: 'cors'
        })
        .then(response => {
            if (response.ok) {
                return response.json().then(data => data);
            }
            return response.json().then(error => { 
                console.error(error);
                throw new Error(error.message);
            });
        });
    },

    rollDiceById: function(gameId, diceToRoll) {
        return fetch(API_URL + '/' + gameId + '/roll', {
            body: JSON.stringify({ diceToRoll: diceToRoll }),
            credentials: 'same-origin',
            headers: {
                'Authorization': 'Bearer ' + AuthService.getCurrentPlayer()?.token,
                'content-type': 'application/json'
            },
            method: 'PUT',
            mode: 'cors'
        })
        .then(response => {
            if (response.ok) {
                return response.json().then(data => data);
            }
            return response.json().then(error => { 
                console.error(error);
                throw new Error(error.message);
            });
        });
    },

    fillBoxById: function(gameId, columnType, boxType) {
        return fetch(API_URL + '/' + gameId + '/fill', {
            body: JSON.stringify({ columnType: columnType, boxType: boxType }),
            credentials: 'same-origin',
            headers: {
                'Authorization': 'Bearer ' + AuthService.getCurrentPlayer()?.token,
                'content-type': 'application/json'
            },
            method: 'PUT',
            mode: 'cors'
        })
        .then(response => {
            if (response.ok) {
                return response.json().then(data => data);
            }
            return response.json().then(error => { 
                console.error(error);
                throw new Error(error.message);
            });
        });
    },

    makeAnnouncementById: function(gameId, boxType) {
        return fetch(API_URL + '/' + gameId + '/announce', {
            body: JSON.stringify({ boxType: boxType }),
            credentials: 'same-origin',
            headers: {
                'Authorization': 'Bearer ' + AuthService.getCurrentPlayer()?.token,
                'content-type': 'application/json'
            },
            method: 'PUT',
            mode: 'cors'
        })
        .then(response => {
            if (response.ok) {
                return response.json().then(data => data);
            }
            return response.json().then(error => { 
                console.error(error);
                throw new Error(error.message);
            });
        });
    },

    restartById: async function(gameId) {
        return fetch(API_URL + '/' + gameId + '/restart', {
            body: null,
            credentials: 'same-origin',
            headers: {
                'Authorization': 'Bearer ' + AuthService.getCurrentPlayer()?.token,
            },
            method: 'PUT',
            mode: 'cors'
        })
        .then(response => {
            if (response.ok) {
                return response.json().then(data => data);
            }
            return response.json().then(error => { 
                console.error(error);
                throw new Error(error.message);
            });
        });
    }
}

export default GameService;