import React, { Component } from 'react';
import Game from './game/game';
import AuthService from '../api/auth-service';
import GameService from '../api/game-service';

class Yamb extends Component {

    constructor(props) {
		super(props);
		this.state = {
			currentUser: AuthService.getCurrentPlayer(),
        }
        this.onSubmit = this.onSubmit.bind(this);
        this.handleRollDice = this.handleRollDice.bind(this);
        this.handleRestart = this.handleRestart.bind(this);
        this.handleFillBox = this.handleFillBox.bind(this);
        this.handleMakeAnnouncement = this.handleMakeAnnouncement.bind(this);
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
            console.log(data);
            this.setState({ game: data });
        })
        .catch((error) => {
            AuthService.logout();
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

    handleFillBox(columnType, boxType) {
        GameService.fillBoxById(this.state.game.id, columnType, boxType)
        .then((data) => {
            this.setState({ game: data });
        })
        .catch((error) => {
            console.error(error);
        });
    }

    handleMakeAnnouncement(type) {
        GameService.makeAnnouncementById(this.state.game.id, type)
            .then((data) => {
                this.setState({ game: data });
            })
            .catch((error) => {
                console.error(error);
            });

    }

    handleRestart() {
        GameService.restartById(this.state.game.id)
        .then((data) => {
            this.setState({ game: data });
        })
        .catch((error) => {
            console.error(error);
        });
    }

    render() {
        let currentUser = this.state.currentUser;
        let game = this.state.game;
        return (
            <div className="Home">
                {!currentUser && 
                    <form onSubmit={this.onSubmit}>
                        <input type="text" name="username" placeholder="Name..."/>
                        <button type="submit">Play</button>
                    </form>}
                {game && 
                <Game 
                    sheet={game.sheet}
                    dices={game.dices}
                    rollCount={game.rollCount}
                    announcement={game.announcement}
                    topSectionSum={game.topSectionSum}
                    middleSectionSum={game.middleSectionSum}
                    bottomSectionSum={game.bottomSectionSum}
                    totalSum={game.totalSum}
                    player={game.player}
                    onRollDice={this.handleRollDice}
                    onFillBox={this.handleFillBox}
                    onMakeAnnouncement={this.handleMakeAnnouncement}
                    onRestart={this.handleRestart}>
                </Game>}
                {currentUser && <button onClick={AuthService.logout}>Logout</button>}                             

            </div>
        );
    }
    
}

export default Yamb;