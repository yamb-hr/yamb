import React, { Component } from 'react';
import Column from '../column/column';
import Label from '../label/label';
import './sheet.css';
import RollButton from '../button/roll-button';
import RestartButton from '../button/restart-button';

class Sheet extends Component {

    constructor(props) {
        super(props)
        this.state = {}
        this.handleRollDice = this.handleRollDice.bind(this);
        this.handleBoxClick = this.handleBoxClick.bind(this);
        this.handleRestart = this.handleRestart.bind(this);
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
        let rollButtonDisabled = this.props.rollButtonDisabled;
        let rollCount = this.props.rollCount;
        let announcement = this.props.announcement;
        let player = this.props.player;
        return (
            <div className="sheet">
                <div className="first-column">
                    {/* TOP SECTION */}
                    <Label icon="ones" info="Number of ones"></Label>
                    <Label icon="twos" info="Number of twos"></Label>
                    <Label icon="threes" info="Number of threes"></Label>
                    <Label icon="fours" info="Number of fours"></Label>
                    <Label icon="fives" info="Number of fives"></Label>
                    <Label icon="sixes" info="Number of sixes"></Label>
                    <Label value="(1-6)" info="Sum of (1-6) + 30 for 60 or higher"></Label>
                    {/* MID SECTION */}
                    <Label value="Max" info="Sum of all dice"></Label>
                    <Label value="Min" info="Sum of all dice"></Label>
                    <Label value="∆ x 1s"></Label>
                    {/* BOTTOM SECTION */}
                    <Label value="Trips" info="Three of a kind"></Label>
                    <Label value="Straight" info="Five consecutive numbers"></Label>
                    <Label value="Boat" info="Three of a kind and two of a kind"></Label>
                    <Label value="Carriage" info="Four of a kind"></Label>
                    <Label value="Yamb" info="Five of a kind"></Label>
                    <Label value="Σ" info="Sum of (Trips-Yamb)"></Label>
                </div>
                {columns.map((column, index) => (
                    <div className="column" key={index}>
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
                <div className="last-column">
                    <RollButton 
                        disabled={rollButtonDisabled} 
                        onRollDice={this.handleRollDice}
                        rollCount={rollCount}>
                    </RollButton>
                    <div className="top-section-sum">
                        <Label value={topSectionSum}></Label>
                    </div>
                    <RestartButton 
                        onRestart={this.handleRestart}>
                    </RestartButton>
                    <div className="middle-section-sum">
                        <Label value={middleSectionSum}></Label>
                    </div>
                    <div className="bottom-section-sum">
                        <Label value={bottomSectionSum}></Label>
                    </div>
                </div>
                <div className="total-sum">
                    <Label value={totalSum}></Label>
                </div>
                <div className="username">
                    <Label value={player.username}></Label>
                </div>
            </div>
        );
    }    
}

export default Sheet;