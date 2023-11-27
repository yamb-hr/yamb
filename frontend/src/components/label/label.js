import React, { Component } from 'react';
import './label.css';

class Label extends Component {

    constructor(props) {
        super(props)
        this.state = {
            value: this.props.value,
            icon: this.props.icon,
            info: this.props.info
        }
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        console.log(this.props.info);
    }

    render() {
        let value = this.state.value;
        let icon = this.state.icon;
        return (
            <button className="label" onClick={this.handleClick}>
                {icon ? <img src={"./svg/labels/" + icon + ".svg"} alt={value}></img> : <strong>{value}</strong>}
            </button>
                
        );
    }    
}

export default Label;