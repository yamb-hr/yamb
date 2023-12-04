import React, { Component } from 'react';
import Column from '../column/column';
import Label from '../label/label';
import './sheet.css';

class Sheet extends Component {

    constructor(props) {
        super(props)
        this.state = {}
        this.handleRollDice = this.handleRollDice.bind(this);
        this.handleBoxClick = this.handleBoxClick.bind(this);
        this.handleRestart = this.handleRestart.bind(this);
        this.handleLogout = this.handleLogout.bind(this);
        this.handleSettings = this.handleSettings.bind(this);
        this.handleInfo = this.handleInfo.bind(this);
    }

    handleSettings() {
        console.log("Settings");
    }

    handleInfo() {
        console.log("Info");
    }

    handleLogout() {
        this.props.onLogout();
    }

    handleRollDice() {
        this.props.onRollDice();
    }

    handleRestart() {
        this.props.onRestart();
    }

    handleBoxClick(columnType, boxType) {
        this.props.onBoxClick(columnType, boxType);
    }

    render() {
        let columns = this.props.columns;
        let topSectionSum = this.props.topSectionSum;
        let middleSectionSum = this.props.middleSectionSum;
        let bottomSectionSum = this.props.bottomSectionSum;
        let totalSum = this.props.totalSum;
        let rollDiceButtonDisabled = this.props.rollDiceButtonDisabled;
        let rollCount = this.props.rollCount;
        let announcement = this.props.announcement;
        let player = this.props.player;
        return (
            <div className="sheet">
                {/* TOP SECTION */}
                <div className="column">
                    <button className="settings-button" onClick={this.handleSettings}>
                        <img src={"./svg/buttons/cog.svg"} alt="Settings"></img>
                    </button>
                    <Label icon="ones" info="Number of ones"></Label>
                    <Label icon="twos" info="Number of twos"></Label>
                    <Label icon="threes" info="Number of threes"></Label>
                    <Label icon="fours" info="Number of fours"></Label>
                    <Label icon="fives" info="Number of fives"></Label>
                    <Label icon="sixes" info="Number of sixes"></Label>
                    <Label variant="sum" value="(1-6)" info="Sum of (1-6) + 30 for 60 or higher"></Label>
                    {/* MID SECTION */}
                    <Label value="Max" info="Sum of all dice"></Label>
                    <Label value="Min" info="Sum of all dice"></Label>
                    <Label variant="sum" value="∆ x 1s"></Label>
                    {/* BOTTOM SECTION */}
                    <Label value="Trips" info="Three of a kind"></Label>
                    <Label value="Straight" info="Five consecutive numbers"></Label>
                    <Label value="Boat" info="Three of a kind and two of a kind"></Label>
                    <Label value="Carriage" info="Four of a kind"></Label>
                    <Label value="Yamb" info="Five of a kind"></Label>
                    <Label variant="sum" value="Σ" info="Sum of (Trips-Yamb)"></Label>
                </div>
                {columns.map((column) => (
                    <div className="column" key={column.type}>
                        <Column 
                            type={column.type} 
                            boxes={column.boxes} 
                            rollCount={rollCount}
                            announcement={announcement}
                            topSectionSum={column.topSectionSum}
                            middleSectionSum={column.middleSectionSum}
                            bottomSectionSum={column.bottomSectionSum}
                            onBoxClick={this.handleBoxClick}>
                        </Column> 
                    </div>
                ))}
                <div className="column">
                <button className="info-button" onClick={this.handleInfo}>
                    <img src={"./svg/buttons/info.svg"} alt="Info"></img>
                </button>
                <div className="empty-space"></div>
                    <button className="roll-button" onClick={this.handleRollDice} disabled={rollDiceButtonDisabled}>
                        <img src={"./svg/buttons/roll-" + (3-rollCount) + ".svg"} alt="Roll"></img>
                    </button>                    
                    <div className="top-section-sum">
                        <Label variant="sum" value={topSectionSum}></Label>
                    </div>
                    <button className="restart-button" onClick={this.handleRestart}>
                        <img src={"./svg/buttons/restart.svg"} alt="Restart"></img>
                    </button>
                    <div className="middle-section-sum">
                        <Label variant="sum" value={middleSectionSum}></Label>
                    </div>
                    <div className="bottom-section-sum">
                        <Label variant="sum" value={bottomSectionSum}></Label>
                    </div>
                </div>
                <div className="last-row">
                    <div className="username">
                        <Label value={player.username}></Label>
                    </div>
                    <button className="logout-button" onClick={this.handleLogout}>Logout</button>
                    <div className="total-sum">
                        <Label variant="sum" value={totalSum}></Label>
                    </div>
                </div>
            </div>
        );
    }    
}

export default Sheet;