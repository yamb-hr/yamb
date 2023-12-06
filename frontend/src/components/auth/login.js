import React, { Component } from 'react';
import AuthService from '../../api/auth-service';
import { withRouter } from '../../withRouter';
import { ToastContainer, toast } from 'react-toastify';

export class Login extends Component {

    constructor(props) {
        super(props)
        this.state = {
            username: "",
            password: ""
        }
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
    }

    handleSubmit() {
        AuthService.login({ 
            username: this.state.username,
            password: this.state.password
        })
        .then((player) => {
            console.log(player);
            localStorage.setItem("player", JSON.stringify(player)); 
            this.props.navigate("/");
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
        let loginDisabled = (username.length < 5 || username.length > 15) || !password;
        return (
            <div className="login">
                <div className="form">
                    <input className="username-input"type="text" value={username} onChange={this.handleUsernameChange} placeholder="Ime..."/>
                    <br/>
                    <input className="password-input" type="password" value={password} onChange={this.handlePasswordChange} placeholder="Lozinka..."/>
                    <br/>
                    <button className="login-button" disabled={loginDisabled} onClick={this.handleSubmit}>Prijava</button>
                    <br/>
                    <span style={{"float":"left"}}><a href="/" >Igra</a></span>
                    <span style={{"float":"right"}}><a href="/register" >Registracija</a></span>
                    <br/>
                </div>
                <ToastContainer limit={5} style={{ fontSize: "14px"}}/>
            </div>
        );
    }    
}

export default withRouter(Login);