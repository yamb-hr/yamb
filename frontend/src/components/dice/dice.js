import React, { Component } from 'react';
import './dice.css';

export class Dice extends Component {

    constructor(props) {
        super(props)
        this.state = {}
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        this.props.onDiceClick(this.props.index);
    }

    render() {
        let value = this.props.value;
        let diceClass = "dice " + (this.props.saved ? "saved" : "");
        let diceDisabled = this.props.diceDisabled;
        return (
            <button className={diceClass} onClick={this.handleClick} disabled={diceDisabled}>
                <img src={'./svg/dice/' + value + '.svg'} alt={value}/>
            </button>
        );
    }    
}

export default Dice;