const API_URL = "http://localhost:8080/api";

const AUTH_URL = API_URL + "/auth";

const AuthService = {

    getCurrentPlayer: function() {
        try {
            return JSON.parse(localStorage.getItem("player"));
        } catch (error) {
            console.error(error);
            this.logout();
        }
        return null;
    },

    logout: function() {
        localStorage.clear();
        window.location.reload();
    },

    login: function(authRequest) {
        return fetch(AUTH_URL + '/login', {
                body: JSON.stringify(authRequest),
                credentials: 'same-origin',
                headers: {
                    'content-type': 'application/json'
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
            });
        },

    register: function(authRequest) {
        return fetch(AUTH_URL + '/register', {
            body: JSON.stringify(authRequest),
            credentials: 'same-origin',
            headers: {
                'Authorization': 'Bearer ' + AuthService.getCurrentPlayer().token, // in case a user was already logged in as a temp player
                'content-type': 'application/json'
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
        });
    },

    createTempPlayer: function(tempPlayerRequest) {
        return fetch(AUTH_URL + '/temp-player', {
            body: JSON.stringify(tempPlayerRequest),
            credentials: 'same-origin',
            headers: {
                'content-type': 'application/json'
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
        });
    }
}

export default AuthService;
