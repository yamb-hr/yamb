import React, { Component } from 'react';

class Scores extends Component {

    constructor(props) {
        super(props)
        this.state = {}
    }

    render() {
        return (
            <div className="form">
                Scores
                <br/>
                <br/>
                <a href="/">Home</a>
                <br/>
                <a href="/games">Games</a>
                <br/>
                <a href="/players">Players</a>
            </div>   
        );
    }    
}

export default Scores;