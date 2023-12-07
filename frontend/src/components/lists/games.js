import React, { Component } from 'react';

class Games extends Component {

    constructor(props) {
        super(props)
        this.state = {}
    }

    render() {
        return (
            <div className="form">
                Games
                <br/>
                <br/>
                <a href="/">Home</a>
                <br/>
                <a href="/players">Players</a>
                <br/>
                <a href="/scores">Scores</a>
            </div>   
        );
    }    
}

export default Games;