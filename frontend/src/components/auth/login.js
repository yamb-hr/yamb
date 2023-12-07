import React, { useState } from 'react';
import AuthService from '../../api/auth-service';
import { ToastContainer, toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';


function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    function handleSubmit() {
        AuthService.login({
            username: username,
            password: password
        })
        .then((player) => {
            console.log(player);
            localStorage.setItem("player", JSON.stringify(player)); 
            navigate("/");
        })
        .catch((error) => {
            handleError(error.message); 
        });
    };

    function handleUsernameChange(event) {
        setUsername(event.target.value);
    };

    function handlePasswordChange(event) {
        setPassword(event.target.value);
    };

    function handleError(message) {
        console.error(message);
        toast.error(message, {
            position: "top-center",
            autoClose: 2000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            pauseOnFocusLoss: true,
            draggable: true,
            progress: undefined,
            theme: "dark"
        });
    };

    const loginDisabled = username.length < 5 || username.length > 15 || !password;

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
                <input
                    className="password-input"
                    type="password"
                    value={password}
                    onChange={handlePasswordChange}
                    placeholder="Lozinka..."
                />
                <br />
                <button
                    className="login-button"
                    disabled={loginDisabled}
                    onClick={handleSubmit}
                >
                    Prijava
                </button>
                <br />
                <span style={{ float: "left" }}>
                    <a href="/">Igra</a>
                </span>
                <span style={{ float: "right" }}>
                    <a href="/register">Registracija</a>
                </span>
                <br />
            </div>
            <ToastContainer limit={5} style={{ fontSize: "14px"}} />
        </div>
    );
};

export default Login;
