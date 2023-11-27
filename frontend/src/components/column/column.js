import React, { Component } from 'react';
import Box from '../box/box';
import Label from '../label/label';
import './column.css';

class Column extends Component {

    constructor(props) {
        super(props)
        this.state = {}
        this.handleBoxClick = this.handleBoxClick.bind(this);
        this.isBoxDisabled = this.isBoxDisabled.bind(this);
    }

    handleBoxClick(boxType) {
        this.props.onBoxClick(this.props.type, boxType);
    }

    isBoxDisabled(box) {
        if (this.props.rollCount === 0) {
            return true;
        }
        if (box.value != null) {
            return true;
        }   
        if (this.props.type === "FREE") {
            return true;
        } else if (this.props.type === "DOWNWARDS") {
            return box.type === "ONES"; // || sheet.getColumns().get(columnType.ordinal()).getBoxes().get(boxType.ordinal() - 1).getValue() != null;
        } else if (this.props.type === "UPWARDS") {
            return box.type === "YAMB"; // || sheet.getColumns().get(columnType.ordinal()).getBoxes().get(boxType.ordinal() + 1).getValue() != null;
        } else if (this.props.type === "ANNOUNCEMENT") {
            return box.type === this.props.announcement;
        }
        return false;

    }

    render() {
        let type = this.props.type;
        let boxes = this.props.boxes;
        let rollCount = this.props.rollCount;
        let announcement = this.props.announcement;
        let topSectionSum = this.props.topSectionSum;
        let middleSectionSum = this.props.middleSectionSum;
        let bottomSectionSum = this.props.bottomSectionSum;
        return (
            <div className="column">    
                <Label 
                    icon={type} 
                    value={type}
                    info={type}>
                </Label>
                {boxes.map((box) => (
                    <Box 
                        key={type+box.type}
                        type={box.type}
                        value={box.value}
                        columnType={type}
                        announcement={announcement}
                        boxDisabled={this.isBoxDisabled(box)}
                        onClick={this.handleBoxClick}>
                    </Box>
                ))}
                <div className="column-top-section-sum">
                    <Label value={topSectionSum}></Label>
                </div>
                <div className="column-middle-section-sum">
                    <Label value={middleSectionSum}></Label>
                </div>
                <div className="column-bottom-section-sum">
                    <Label value={bottomSectionSum}></Label>
                </div>
            </div>
        );
    }    
}

export default Column;