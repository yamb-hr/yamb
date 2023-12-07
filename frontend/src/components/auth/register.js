import React, { useState } from 'react';
import AuthService from '../../api/auth-service';
import { ToastContainer, toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';
import './auth.css';

function Register() {
    const [username, setUsername] = useState(
        AuthService.getCurrentPlayer() ? AuthService.getCurrentPlayer().username : "Player" + Math.round(Math.random() * 10000)
    );
    const [password, setPassword] = useState('');
    const [repeatPassword, setRepeatPassword] = useState('');
    const navigate = useNavigate();

    function handleSubmit() {
        AuthService.register({
            username: username,
            password: password
        })
        .then((player) => {
            console.log(player);
            navigate('/login');
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

    function handleRepeatPasswordChange(event) {
        setRepeatPassword(event.target.value);
    };

    function handleError(error) {
        console.error(error?.message);
        toast.error(error?.message, {
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

    const registerDisabled = username.length < 5 || username.length > 15 || !password || (password !== repeatPassword);

    return (
        <div>
            <div className="form">
                <input
                    className="username-input"
                    type="text"
                    value={username}
                    onChange={handleUsernameChange}
                    placeholder="Ime...">
                </input>
                <br />
                <input
                    className="password-input"
                    type="password"
                    value={password}
                    onChange={handlePasswordChange}
                    placeholder="Lozinka...">
                </input>
                <br />
                <input
                    className="password-input"
                    type="password"
                    value={repeatPassword}
                    onChange={handleRepeatPasswordChange}
                    placeholder="Ponovi Lozinku...">
                </input>
                <br />
                <button className="register-button" disabled={registerDisabled} onClick={handleSubmit} >Registracija</button>
                <br />
                <span style={{ float: "left" }}><a href="/">Igra</a></span>
                <span style={{ float: "right" }}><a href="/login">Prijava</a></span>
                <br />
            </div>
            <ToastContainer limit={5} style={{ fontSize: "14px" }} />
        </div>
    );
};

export default Register;
