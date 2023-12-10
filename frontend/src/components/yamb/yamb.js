import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import GameService from '../../api/game-service';
import Game from '../game/game';
import AuthService from '../../api/auth-service';
import './yamb.css';

function Yamb(props) {

    const { t } = useTranslation();
    const { id } = useParams();
    const [game, setGame] = useState(null);
    const [currentUser] = useState(AuthService.getCurrentPlayer());

    useEffect(() => {   
        if (id) {
            GameService.getGameById(id)
            .then((data) => {
                console.log(data);
                setGame(data);
            })
            .catch((error) => {
                props.onError(error)
            });
        } else if (currentUser) {
            GameService.play()
            .then((data) => {
                console.log(data);
                setGame(data);
            })
            .catch((error) => {
                props.onError(error)
            });
        }
    }, [currentUser, id, props]);

    function handleRollDice(diceToRoll) {
        GameService.rollDiceById(game.id, diceToRoll)
        .then((data) => {
            console.log(data);
            setGame(data);
        })
        .catch((error) => {
            props.onError(error)
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
            props.onError(error)
        });
    }

    function handleNewGame() {
        GameService.play()
        .then((data) => {
            console.log(data);
            setGame(data);
        })
        .catch((error) => {
            props.onError(error)
        });
    }

    function handleMakeAnnouncement(type) {
        GameService.makeAnnouncementById(
            game.id, type
        )
        .then((data) => {
            console.log(data);
            setGame(data);
        })
        .catch((error) => {
            props.onError(error)
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
            props.onError(error)
        });
    }

    function handleFinish(totalSum) {
        toast(
			<div>
				<h4>{t('congratulations')}</h4><h2>{totalSum}</h2>
				<button onClick={handleNewGame} className="new-game-button">{t('new-game')}</button>
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
    }

    function handleLogout() {
        AuthService.logout();
        window.location.reload();
    }
   
    return (
        <div className="yamb">
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
        </div>
    );
};

export default Yamb;

