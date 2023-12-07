import React, { useState, useEffect } from 'react';
import Game from '../game/game';
import AuthService from '../../api/auth-service';
import GameService from '../../api/game-service';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './yamb.css';

function Yamb() {
    const [username, setUsername] = useState("Player" + Math.round(Math.random() * 10000));
    const [currentUser, setCurrentUser] = useState(AuthService.getCurrentPlayer());
    const [game, setGame] = useState(null);

    useEffect(() => {
        if (currentUser) {
            GameService.play()
            .then((data) => {
                console.log(data);
                setGame(data);
            })
            .catch((error) => {
                console.error(error);
                AuthService.logout();
            });
        }
        console.log(process.env.REACT_APP_API_URL);
    }, [currentUser]);

    function handleSubmit() {
        AuthService.createTempPlayer({ 
            username: username
        })
        .then((player) => {
            localStorage.setItem("player", JSON.stringify(player)); 
            setCurrentUser(player);
        })
        .catch((error) => {
            handleError(error.message); 
        });
    };    

    function handleRollDice(diceToRoll) {
        GameService.rollDiceById(game.id, diceToRoll)
        .then((data) => {
            console.log(data);
            setGame(data);
        })
        .catch((error) => {
            handleError(error);
        });
    };

    function handleFillBox(columnType, boxType) {
        GameService.fillBoxById(
            game.id, columnType, boxType
        )
        .then((data) => {
            console.log(data);
            setGame(data);
            if (data.status === "FINISHED") {
                handleFinish();
            }
        })
        .catch((error) => {
            handleError(error);
        });
    }

    function handleNewGame() {
        GameService.play()
        .then((data) => {
            console.log(data);
            setGame(data);
        })
        .catch((error) => {
            console.error(error);
            AuthService.logout();
        });
    }

    function handleFinish() {
		toast(
			<div>
				<h3>Čestitamo!</h3><h4>Vaš konačan rezultat je:</h4><h2>{game?.totalSum}</h2>
				<button onClick={handleNewGame} className="new-game-button">Nova Igra</button>
			</div>, {
				position: "top-center",
				autoClose: false,
				hideProgressBar: false,
				closeOnClick: true,
				pauseOnHover: true,
				pauseOnFocusLoss: true,
				draggable: true,
				progress: undefined,
				theme: "dark"
			}
		);
	};

    function handleMakeAnnouncement(type) {
        GameService.makeAnnouncementById(
            game.id, type
        )
        .then((data) => {
            console.log(data);
            setGame(data);
        })
        .catch((error) => {
            handleError(error);
        });

    }

    function handleRestart() {
        GameService.restartById(
            game.id
        )
        .then((data) => {
            console.log(data);
            setGame(data);
        })
        .catch((error) => {
            handleError(error);
        });
    }

    function handleLogout() {
        AuthService.logout();
        window.location.reload();
    }

    function handleUsernameChange(event) {
        setUsername(event.target.value);
    }

    function handleError(error) {
		console.error(error?.message);
		toast.error(error?.message, {
			position: "top-center",
			autoClose: 2000,
			hideProgressBar: true,
			closeOnClick: true,
			pauseOnHover: true,
			pauseOnFocusLoss: true,
			draggable: true,
			progress: undefined,
			theme: "dark"
		});
	}

    let playDisabled = username.length < 5 || username.length > 15;

    return (
        <div className="yamb">
            {!currentUser && <div className="form">
                <input className="username-input" type="text" value={username} onChange={handleUsernameChange} placeholder="Ime..."/>
                <br/>
                <button className="play-button" disabled={playDisabled} onClick={handleSubmit}>Igraj</button>
                <br/>
                <span style={{"float":"left"}}><a href="/login" >Prijava</a></span>
                <span style={{"float":"right"}}><a href="/register" >Registracija</a></span>
                <br/>
            </div>}
            {game && <Game 
                sheet={game.sheet}
                dices={game.dices}
                rollCount={game.rollCount}
                announcement={game.announcement}
                announcementRequired={game.announcementRequired}
                topSectionSum={game.topSectionSum}
                middleSectionSum={game.middleSectionSum}
                bottomSectionSum={game.bottomSectionSum}
                totalSum={game.totalSum}
                player={game.player}
                currentUser={currentUser}
                onRollDice={handleRollDice}
                onFillBox={handleFillBox}
                onMakeAnnouncement={handleMakeAnnouncement}
                onRestart={handleRestart}
                onLogout={handleLogout}>
            </Game>}
            <ToastContainer />
        </div>
    );
};

export default Yamb;
