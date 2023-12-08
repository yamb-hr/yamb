import React from 'react';
import './dice.css';

function Dice(props) {
    function handleClick() {
        props.onDiceClick(props.index);
    };

    function getDiceClass() {
        let diceClass = "dice " + (props.saved ? "saved " : " ");
        if (props.rolling && !props.saved) {
            diceClass += "rolling ";
            diceClass += Math.random() > 0.5 ? "clockwise" : "counter-clockwise";
        }
        return diceClass;
    };

    function getDiceStyle() {
        if (props.rolling && !props.saved) {
            let time = Math.round(800 + Math.random() * 1000);
            return {
                animationDuration: time + "ms"
            }
        }
    };

    let value = props.value;
    let diceClass = getDiceClass();
    let diceStyle = getDiceStyle();
    let diceDisabled = props.diceDisabled;

    return (
        <button className={diceClass} style={diceStyle} onClick={handleClick} disabled={diceDisabled}>
            <img src={'./svg/dice/' + value + '.svg'} alt={value}/>
        </button>
    );
}

export default Dice;