import React, { useState } from 'react';
import AuthService from '../../api/auth-service';

function TempPlayer(props) {
    
    const [username, setUsername] = useState("Player" + Math.round(Math.random() * 10000));

    function handleSubmit() {
        AuthService.createTempPlayer({
            username: username
        })
        .then((player) => {
            console.log(player);
            localStorage.setItem("player", JSON.stringify(player));
            props.onCurrentUserChange(player);
        });
    };

    function handleUsernameChange(event) {
        setUsername(event.target.value);
    };

    const loginDisabled = username.length < 5 || username.length > 15;

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
