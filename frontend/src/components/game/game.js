import React, { Component } from 'react';
import Dice from '../dice/dice';
import Sheet from '../sheet/sheet';
import './game.css';

class Game extends Component {

    constructor(props){
        super(props)
        this.state = {
            diceToRoll: [0, 1, 2, 3, 4]
        }
        this.handleRollDice = this.handleRollDice.bind(this);
        this.handleDiceClick = this.handleDiceClick.bind(this);
        this.handleBoxClick = this.handleBoxClick.bind(this);
    }

    handleDiceClick(index) {
        this.setState(prevState => {
            let diceToRoll = [...prevState.diceToRoll];
            if (diceToRoll.includes(index)) {
                diceToRoll.splice(diceToRoll.indexOf(index), 1);
            } else {
                diceToRoll.push(index);
            }
            return { diceToRoll };
        });
    }

    handleRollDice() {
        this.props.onRollDice(this.state.diceToRoll);
    }

    handleBoxClick(columnType, boxType) {
        this.props.onBoxClick(columnType, boxType);
    }

    render() {
        let rollCount = this.props.rollCount;
        let sheet = this.props.sheet;
        let dices = this.props.dices;
        let diceToRoll = this.state.diceToRoll;
        return (
            <div className="game">
                {dices && 
                    <div className="dices">
                        {dices.map((dice, index) => (
                            <div key={index}>
                                <Dice 
                                    value={dice.value} 
                                    index={dice.index} 
                                    saved={!diceToRoll.includes(dice.index)}
                                    onClick={this.handleDiceClick}>
                                </Dice>
                            </div>
                        ))}
                    </div>}
                    <br/>
                {sheet && 
                    <Sheet 
                        columns={sheet.columns} 
                        rollCount={rollCount}
                        onRollDice={this.handleRollDice}
                        onBoxClick={this.handleBoxClick}>
                    </Sheet>}
            </div>
        );
    }    
}

export default Game;