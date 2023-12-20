import React, { useContext, useState } from 'react';
import { CurrentUserContext } from '../../App';
import AuthService from '../../api/auth-service';

function TempPlayer(props) {
    
    const [ username, setUsername ] = useState("Player" + Math.round(Math.random() * 10000));
    const { setCurrentUser } = useContext(CurrentUserContext);

    function handleSubmit() {
        AuthService.createTempPlayer({
            username: username
        })
        .then((player) => {
            console.log(player);
            localStorage.setItem("player", JSON.stringify(player));
            setCurrentUser(player);
        })
        .catch((error) => {
            props.onError(error);
        });
    };

    function handleUsernameChange(event) {
        setUsername(event.target.value);
    };

    let loginDisabled = username.length < 5 || username.length > 15;

    return (
        <div className="login">
            <div className="form">
                <input
                    className="username-input"
                    type="text"
                    value={username}
                    onChange={handleUsernameChange}
                    placeholder="Ime..."
                />
                <br />
                <button
                    className="login-button"
                    disabled={loginDisabled}
                    onClick={handleSubmit}
                >
                    Igraj
                </button>
                <br />
                <span style={{ float: "left" }}>
                    <a href="/login">Prijava</a>
                </span>
                <span style={{ float: "right" }}>
                    <a href="/register">Registracija</a>
                </span>
                <br />
            </div>
        </div>
    );
};

export default TempPlayer;
