import React, { Component } from 'react';

class Players extends Component {

    constructor(props) {
        super(props)
        this.state = {}
    }

    render() {
        return (
            <div className="form">
                Players
                <br/>
                <br/>
                <a href="/">Home</a>
                <br/>
                <a href="/games">Games</a>
                <br/>
                <a href="/scores">Scores</a>
            </div>   
        );
    }    
}

export default Players;