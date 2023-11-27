import React, { Component } from 'react';
import './button.css';

class RestartButton extends Component {

    constructor(props) {
        super(props)
        this.state = {}
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        this.props.onRestart();
    }

    render() {
        return (
            <button className="restart-button" onClick={this.handleClick}>
                <img src={"./svg/buttons/restart.svg"} alt="Restart"></img>
            </button>
                
        );
    }    
}

export default RestartButton;