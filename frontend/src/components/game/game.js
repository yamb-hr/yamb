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
        this.handleRestart = this.handleRestart.bind(this);
        this.handleLogout = this.handleLogout.bind(this);
    }

    handleDiceClick(index) {
        let diceToRoll = this.state.diceToRoll;
        if (diceToRoll.includes(index)) {   
            diceToRoll.splice(diceToRoll.indexOf(index), 1);
        } else {
            diceToRoll.push(index);
        }
        this.setState({ diceToRoll });
    }

    handleRollDice() {
        this.props.onRollDice(this.state.diceToRoll);
    }

    handleBoxClick(columnType, boxType) {
        if (columnType === "ANNOUNCEMENT" && this.props.announcement == null) {
            this.props.onMakeAnnouncement(boxType);
        } else {
            this.props.onFillBox(columnType, boxType);
            let diceToRoll = [0, 1, 2, 3, 4];
            this.setState({ diceToRoll });
        }
    }

    handleRestart() {
        this.setState({ diceToRoll: [0, 1, 2, 3, 4] });
        this.props.onRestart();
    }

    handleLogout() {
        this.props.onLogout();
    }

    render() {
        let rollCount = this.props.rollCount;
        let sheet = this.props.sheet;
        let dices = this.props.dices;
        let announcement = this.props.announcement;
        let player = this.props.player;
        let diceToRoll = this.state.diceToRoll;
        let diceDisabled = this.props.rollCount === 0 || this.props.rollCount === 3;
        let rollDiceButtonDisabled = this.props.rollCount === 3 || this.props.announcementRequired;
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
                                    diceDisabled={diceDisabled}
                                    onDiceClick={this.handleDiceClick}>
                                </Dice>
                            </div>
                        ))}
                    </div>}
                <br/>
                {sheet && 
                    <Sheet 
                        columns={sheet.columns} 
                        rollCount={rollCount}
                        topSectionSum={sheet.topSectionSum}
                        middleSectionSum={sheet.middleSectionSum}
                        bottomSectionSum={sheet.bottomSectionSum}
                        totalSum={sheet.totalSum}
                        announcement={announcement}
                        player={player}
                        rollDiceButtonDisabled={rollDiceButtonDisabled}
                        onRollDice={this.handleRollDice}
                        onRestart={this.handleRestart}
                        onBoxClick={this.handleBoxClick}
                        onLogout={this.handleLogout}>
                    </Sheet>}
            </div>
        );
    }    
}

export default Game;