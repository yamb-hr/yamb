import React, { Component } from 'react';
import Box from '../box/box';
import Label from '../label/label';
import './column.css';

class Column extends Component {

    constructor(props) {
        super(props)
        this.state = {}
        this.handleBoxClick = this.handleBoxClick.bind(this);
    }

    handleBoxClick(boxType) {
        this.props.onBoxClick(this.props.type, boxType);
    }

    render() {
        let type = this.props.type;
        let boxes = this.props.boxes;
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