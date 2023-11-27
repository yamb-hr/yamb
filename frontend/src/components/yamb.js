import React, { Component } from 'react';
import Game from './game/game';
import AuthService from '../api/auth-service';
import GameService from '../api/game-service';

class Yamb extends Component {

    constructor(props) {
		super(props);
		this.state = {
            game: undefined,
			currentUser: AuthService.getCurrentPlayer(),
        }
        this.onSubmit = this.onSubmit.bind(this);
        this.handleRollDice = this.handleRollDice.bind(this);
        this.handleBoxClick = this.handleBoxClick.bind(this);
    }

    componentDidMount() {
        if (this.state.currentUser) {   
            this.play();
        }
	}

    onSubmit(e) {
        e.preventDefault();
        AuthService.createTempPlayer({ username: e.target.username.value })
        .then((player) => {
            localStorage.setItem("player", JSON.stringify(player)); 
            GameService.play()
            .then((data) => {
                this.setState({ game: data, currentUser: player });
            })
            .catch((error) => {
                console.error(error);
            });
        })
        .catch((error) => {
            console.error(error);
        });
    }    

    play() {
        GameService.play()
        .then((data) => {
            this.setState({ game: data });
        })
        .catch((error) => {
            console.error(error);
        });
    }

    handleRollDice(diceToRoll) {
        GameService.rollDiceById(this.state.game.id, diceToRoll)
        .then((data) => {
            this.setState({ game: data });
        })
        .catch((error) => {
            console.error(error);
        });
    }

    handleBoxClick(columnType, boxType) {
        if (this.state.game.announcement != null && columnType === "ANNOUNCEMENT")
            GameService.makeAnnouncementById(this.state.game.id, boxType)
            .then((data) => {
                this.setState({ game: data });
            })
            .catch((error) => {
                console.error(error);
            });
        else {
            GameService.fillBoxById(this.state.game.id, columnType, boxType)
            .then((data) => {
                this.setState({ game: data });
            })
            .catch((error) => {
                console.error(error);
            });
        }
    }

    render() {
        let currentUser = this.state.currentUser;
        let game = this.state.game;
        return (
            <div className="Home">
                {currentUser && 
                    <div>
                        {currentUser.username}
                        <br/>
                        <button onClick={AuthService.logout}>Logout</button>
                    </div>}                             
                {!currentUser && 
                    <form onSubmit={this.onSubmit}>
                        <input type="text" name="username" placeholder="Name..."/>
                        <button type="submit">Play</button>
                    </form>
                }
                {game && 
                <Game 
                    sheet={game.sheet}
                    dices={game.dices}
                    rollCount={game.rollCount}
                    topSectionSum={game.topSectionSum}
                    middleSectionSum={game.middleSectionSum}
                    bottomSectionSum={game.bottomSectionSum}
                    totalSum={game.totalSum}
                    onRollDice={this.handleRollDice}
                    onBoxClick={this.handleBoxClick}>
                </Game>}
                
            </div>
        );
    }
    
}

export default Yamb;