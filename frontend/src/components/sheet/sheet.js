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
        let currentUser = this.props.currentUser;
        return (
            <div className="sheet">
                {/* TOP SECTION */}
                <div className="column">
                    <button className="settings-button" onClick={this.handleSettings}>
                        <img src={"./svg/buttons/cog.svg"} alt="Settings"></img>
                    </button>
                    <Label icon="ones" info="Broj jedinica"></Label>
                    <Label icon="twos" info="Broj dvica"></Label>
                    <Label icon="threes" info="Broj trica"></Label>
                    <Label icon="fours" info="Broj četvorki"></Label>
                    <Label icon="fives" info="Broj petica"></Label>
                    <Label icon="sixes" info="Broj šestica"></Label>
                    <Label variant="sum" value="Σ (1, 6)" info="Zbroj od jedinica do šestica + 30 za 60 ili više"></Label>
                    {/* MID SECTION */}
                    <Label value="Max" info="Zbroj svih kockica"></Label>
                    <Label value="Min" info="Zbroj svih kockica"></Label>
                    <Label variant="sum" value="∆ x 1-ice" info="(Max - Min) x broj jedinica"></Label>
                    {/* BOTTOM SECTION */}
                    <Label value="Tris" info="Tri iste kockice"></Label>
                    <Label value="Skala" info="Pet uzastopnih kockica"></Label>
                    <Label value="Ful" info="Tris i par"></Label>
                    <Label value="Poker" info="Četiri iste kockice"></Label>
                    <Label value="Jamb" info="Pet istih kockica"></Label>
                    <Label variant="sum" value="Σ (T, J)" info="Zbroj od Trisa do Jamba"></Label>
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
                    <button className="username-button">{player.username}</button>
                    {currentUser.tempUser ? <a className="register-sheet-button" href="/register">Registracija</a> : 
                    <button className="logout-button" onClick={this.handleLogout}>Odjava</button>}
                    <Label variant="total-sum" value={totalSum}></Label>
                </div>
            </div>
        );
    }    
}

export default Sheet;