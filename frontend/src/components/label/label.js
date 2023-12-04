import React, { Component } from 'react';
import './label.css';

class Label extends Component {

    constructor(props) {
        super(props)
        this.state = {}
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        if (this.props.info) console.log(this.props.info);
    }

    render() {
        let value = this.props.value;
        let icon = this.props.icon;
        let labelClass = "label " + (this.props.variant ? this.props.variant : "");
        return (
            <button className={labelClass} onClick={this.handleClick}>
                {icon ? <img src={"./svg/labels/" + icon + ".svg"} alt={value}></img> : <strong>{value}</strong>}
            </button>                
        );
    }    
}

export default Label;