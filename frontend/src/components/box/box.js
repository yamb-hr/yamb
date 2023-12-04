import React, { Component } from 'react';
import './box.css';

class Box extends Component {

    constructor(props) {
        super(props)
        this.state = {}
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        this.props.onClick(this.props.type);
    }
    
    render() {
        let value = this.props.value;
        let boxClass = "box " + (this.props.columnType === "ANNOUNCEMENT" && this.props.type === this.props.announcement ? "announcement" : "");
        let disabled = this.props.disabled;
        return (
            <button className={boxClass} onClick={this.handleClick} disabled={disabled}>
                <strong>{value}</strong>
            </button>
        );
    }    
    
}

export default Box;