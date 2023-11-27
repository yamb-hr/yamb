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
        return (
            <button className="box" onClick={this.handleClick}>
                <strong>{value}</strong>
            </button>
        );
    }    
    
}

export default Box;