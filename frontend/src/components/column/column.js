import React from 'react';
import Box from '../box/box';
import Label from '../label/label';
import './column.css';

function Column(props) {
    const handleBoxClick = (boxType) => {
        props.onBoxClick(props.type, boxType);
    };

    function isBoxDisabled(box) {
        if (props.rollCount === 0) {
            return true;
        } else if (box.value != null) {
            return true;
        } else if (props.announcement != null) {
            return props.type !== "ANNOUNCEMENT" || box.type !== props.announcement;
        } else if (props.type === "FREE") {
            return false;
        } else if (props.type === "DOWNWARDS") {
            return box.type !== "ONES" && props.boxes[props.boxes.findIndex(x => x.type === box.type) - 1].value == null;
        } else if (props.type === "UPWARDS") {
            return box.type !== "YAMB" && props.boxes[props.boxes.findIndex(x => x.type === box.type) + 1].value == null;
        } else if (props.type === "ANNOUNCEMENT") {
            return props.rollCount !== 1 && box.type !== props.announcement;
        }
        return false;
    };

    function getInfo() {
        switch (props.type) {
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
    };

    const info = getInfo();

    return (
        <div className="column">    
            <Label 
                icon={props.type} 
                value={props.type}
                info={info}
                variant="column-symbol">
            </Label>
            {props.boxes.map((box) => (
                <Box 
                    key={props.type + box.type}
                    type={box.type}
                    value={box.value}
                    columnType={props.type}
                    announcement={props.announcement}
                    disabled={isBoxDisabled(box)}
                    onClick={handleBoxClick}>
                </Box>
            ))}
            <div className="sum column-top-section-sum">
                <Label variant="sum" value={props.topSectionSum}></Label>
            </div>
            <div className="sum column-middle-section-sum">
                <Label variant="sum" value={props.middleSectionSum}></Label>
            </div>
            <div className="sum column-bottom-section-sum">
                <Label variant="sum" value={props.bottomSectionSum}></Label>
            </div>
        </div>
    );
}

export default Column;
