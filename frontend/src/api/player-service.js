import AuthService from "./auth-service";

const API_URL = process.env.REACT_APP_API_URL + "/players";

const ScoreService = {

    getPrincipalById: function(playerId) {
        return fetch(API_URL + "/" + playerId + "/principal", {
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
            return response.json().then(error => { throw new Error(error.message) })
        });
    }

}

export default ScoreService;