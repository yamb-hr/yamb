import React, { Component } from 'react';
import './dice.css';

export class Dice extends Component {

    constructor(props) {
        super(props)
        this.state = {}
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        this.props.onClick(this.props.index);
    }

    render() {
        let value = this.props.value;
        let saved = this.props.saved;
        let diceClass = "dice " + (saved ? "saved" : "");
        return (
            <button className={diceClass} onClick={this.handleClick}>
                <img src={'./svg/dice/' + value + '.svg'} alt={value}/>
            </button>
        );
    }    
}

export default Dice;