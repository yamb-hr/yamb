import React, { Component } from 'react';
import AuthService from '../../api/auth-service';
import { ToastContainer, toast } from 'react-toastify';
import './auth.css';
import { withRouter } from '../../withRouter';


export class Register extends Component {

    constructor(props) {
        super(props)
        this.state = {
            username: AuthService.getCurrentPlayer() ? AuthService.getCurrentPlayer().username : "Player" + Math.round(Math.random() * 10000),
            password: "",
            repeatPassword: ""
        }
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleRepeatPasswordChange = this.handleRepeatPasswordChange.bind(this);
    }

    handleSubmit() {
        AuthService.register({ 
            username: this.state.username,
            password: this.state.password
        })
        .then((player) => {
            console.log(player);
            this.props.navigate('/login')
        })
        .catch((error) => {
            this.handleError(error.message); 
        });
    }

    handleUsernameChange(event) {
        this.setState({username: event.target.value});
    }

    handlePasswordChange(event) {
        this.setState({password: event.target.value});
    }

    handleRepeatPasswordChange(event) {
        this.setState({repeatPassword: event.target.value});
    }

    handleError(message) {
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
    }

    render() {
        let username = this.state.username;
        let password = this.state.password;
        let repeatPassword = this.state.repeatPassword;
        let registerDisabled = (username.length < 5 || username.length > 15) || !password || (password !== repeatPassword);
        return (
            <div>
                <div className="form">
                    <input className="username-input"type="text" value={username} onChange={this.handleUsernameChange} placeholder="Ime..."/>
                    <br/>
                    <input className="password-input" type="password" value={password} onChange={this.handlePasswordChange} placeholder="Lozinka..."/>
                    <br/>
                    <input className="password-input" type="password" value={repeatPassword} onChange={this.handleRepeatPasswordChange} placeholder="Ponovi Lozinku..."/>
                    <br/>
                    <button className="register-button" disabled={registerDisabled} onClick={this.handleSubmit}>Registracija</button>
                    <br/>
                    <span style={{"float":"left"}}><a href="/">Igra</a></span>
                    <span style={{"float":"right"}}><a href="/login">Prijava</a></span>
                    <br/>
                </div>
                <ToastContainer limit={5} style={{ fontSize: "14px"}}/>
            </div>
        );
    }    
}

export default withRouter(Register);