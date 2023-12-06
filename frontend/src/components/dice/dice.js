import React, { Component } from 'react';
import './dice.css';

export class Dice extends Component {

    constructor(props) {
        super(props)
        this.state = {
            diceClass: "dice " + (this.props.saved ? "saved " : " "),
            diceStyle: {}
        }
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        this.props.onDiceClick(this.props.index);
    }

    getDiceClass() {
        let diceClass = "dice " + (this.props.saved ? "saved " : " ");
        if (this.props.rolling && !this.props.saved) {
            diceClass += "rolling ";
            diceClass += Math.random() > 0.5 ? "clockwise" : "counter-clockwise";
        }
        return diceClass;
    }

    getDiceStyle() {
        if (this.props.rolling && !this.props.saved) {
            let time = Math.round(800 + Math.random() * 1000);
            return {
                animationDuration: time + "ms"
            }
        }
    }

    render() {
        let value = this.props.value;
        let diceClass = this.getDiceClass();
        let diceStyle = this.getDiceStyle();
        let diceDisabled = this.props.diceDisabled;
        return (
            <button className={diceClass} style={diceStyle} onClick={this.handleClick} disabled={diceDisabled}>
                <img src={'./svg/dice/' + value + '.svg'} alt={value}/>
            </button>
        );
    }    
}

export default Dice;