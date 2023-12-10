import React, { useState } from 'react';
import Dice from '../dice/dice';
import Sheet from '../sheet/sheet';
import './game.css';

function Game(props) {

    const [state, setState] = useState({
        diceToRoll: [0, 1, 2, 3, 4],
        rolling: false
    });

    function handleDiceClick(index) {
        let diceToRoll = [...state.diceToRoll];
        if (diceToRoll.includes(index)) {   
            diceToRoll.splice(diceToRoll.indexOf(index), 1);
        } else {
            diceToRoll.push(index);
        }
        setState({ ...state, diceToRoll, rolling: false });
    };

    function handleRollDice() {
        props.onRollDice(state.diceToRoll);
        setState({ ...state, rolling: true });
        setTimeout(() => {
            if (state.rolling) {
                setState({ ...state, rolling: false });
            }
        }, 3000);
    };

    function handleBoxClick(columnType, boxType) {
        if (columnType === "ANNOUNCEMENT" && props.announcement == null) {
            props.onMakeAnnouncement(boxType);
        } else {
            props.onFillBox(columnType, boxType);
            setState({ diceToRoll: [0, 1, 2, 3, 4], rolling: false });
        }
    };

    function handleRestart() {
        setState({ diceToRoll: [0, 1, 2, 3, 4], rolling: false });
        props.onRestart();
    };

    function handleLogout() {
        props.onLogout();
    };

    let rollCount = props.rollCount;
    let sheet = props.sheet;
    let dices = props.dices;
    let announcement = props.announcement;
    let player = props.player;
    let diceDisabled = props.rollCount === 0 || props.rollCount === 3;
    let rollDiceButtonDisabled = props.rollCount === 3 || props.announcementRequired;
    let currentUser = props.currentUser;

    return (
        <div className="game">
            {dices && <div className="dices">
                {dices.map((dice, index) => (
                    <div key={index}>
                        <Dice 
                            value={dice.value} 
                            index={dice.index} 
                            saved={!state.diceToRoll.includes(dice.index)}
                            rolling={state.rolling}
                            diceDisabled={diceDisabled}
                            onDiceClick={handleDiceClick}>
                        </Dice>
                    </div>
                ))}
            </div>}
            <br/>
            {sheet && <Sheet 
                columns={sheet.columns} 
                rollCount={rollCount}
                topSectionSum={sheet.topSectionSum}
                middleSectionSum={sheet.middleSectionSum}
                bottomSectionSum={sheet.bottomSectionSum}
                totalSum={sheet.totalSum}
                announcement={announcement}
                player={player}
                currentUser={currentUser}
                rollDiceButtonDisabled={rollDiceButtonDisabled}
                onRollDice={handleRollDice}
                onRestart={handleRestart}
                onBoxClick={handleBoxClick}
                onLogout={handleLogout}>
            </Sheet>}
        </div>
    );
}

export default Game;
