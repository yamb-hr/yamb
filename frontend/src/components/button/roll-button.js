import React, { Component } from 'react';
import './button.css';

class RollButton extends Component {

    constructor(props) {
        super(props)
        this.state = {}
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        this.props.onClick();
    }

    render() {
        let rollCount = this.props.rollCount;
        return (
            <button className="roll-button" onClick={this.handleClick}>
                <img src={"./svg/buttons/roll-" + (3-rollCount) + ".svg"} alt="Roll!"></img>
            </button>
                
        );
    }    
}

export default RollButton;