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
        } else if (box.value != null) {
            return true;
        } else if (this.props.announcement != null) {
            return this.props.type !== "ANNOUNCEMENT" || box.type !== this.props.announcement;
        } else if (this.props.type === "FREE") {
            return false;
        } else if (this.props.type === "DOWNWARDS") {
            return box.type !== "ONES" && this.props.boxes[this.props.boxes.findIndex(x => x.type === box.type) - 1].value == null;
        } else if (this.props.type === "UPWARDS") {
            return box.type !== "YAMB" && this.props.boxes[this.props.boxes.findIndex(x => x.type === box.type) + 1].value == null;
        } else if (this.props.type === "ANNOUNCEMENT") {
            return this.props.rollCount !== 1 && box.type !== this.props.announcement;
        }
        return false;
    }

    getInfo() {
        switch (this.props.type) {
            case "FREE":
                return "Stupac se popunjava u bilo kojem redoslijedu";
            case "DOWNWARDS":
                return "Stupac se popunjava od vrha prema dnu";
            case "UPWARDS":
                return "Stupac se popunjava od dna prema vrhu";
            case "ANNOUNCEMENT":
                return "Stupac se mora prethodno najaviti";
            default:
                return null;
        }
    }

    render() {
        let type = this.props.type;
        let boxes = this.props.boxes;
        let announcement = this.props.announcement;
        let topSectionSum = this.props.topSectionSum;
        let middleSectionSum = this.props.middleSectionSum;
        let bottomSectionSum = this.props.bottomSectionSum;
        let info = this.getInfo();
        return (
            <div className="column">    
                <Label 
                    icon={type} 
                    value={type}
                    info={info}
                    variant="column-symbol">
                </Label>
                {boxes.map((box) => (
                    <Box 
                        key={type+box.type}
                        type={box.type}
                        value={box.value}
                        columnType={type}
                        announcement={announcement}
                        disabled={this.isBoxDisabled(box)}
                        onClick={this.handleBoxClick}>
                    </Box>
                ))}
                <div className="sum column-top-section-sum">
                    <Label variant="sum" value={topSectionSum}></Label>
                </div>
                <div className="sum column-middle-section-sum">
                    <Label variant="sum" value={middleSectionSum}></Label>
                </div>
                <div className="sum column-bottom-section-sum">
                    <Label variant="sum" value={bottomSectionSum}></Label>
                </div>
            </div>
        );
    }    
}

export default Column;