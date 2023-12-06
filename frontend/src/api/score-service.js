import AuthService from "./auth-service";

const API_URL = "http://localhost:8080/api";

const SCORES_URL = API_URL + "/scores";

const ScoreService = {

    getByInterval: function(interval) {
        return fetch(SCORES_URL + '/interval', {
            body: JSON.stringify({ interval: interval }),
            credentials: 'same-origin',
            headers: {
                'Authorization': 'Bearer ' + AuthService.getCurrentPlayer().token
            },
            method: 'POST',
            mode: 'cors'
        })
        .then(response => {
            if (response.ok) {
                return response.json().then(data => data);
            }
            return response.json().then(error => { throw new Error(error.message) })
        });
    },

}

export default ScoreService;