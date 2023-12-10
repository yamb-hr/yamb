import React, { useEffect, useState} from 'react';
import TempPlayer from '../auth/temp-player';
import Yamb from '../yamb/yamb';
import AuthService from '../../api/auth-service';
import './home.css';

function Home(props) {

    const [currentUser, setCurrentUser] = useState(AuthService.getCurrentPlayer());

    useEffect(() => {
        console.log(process.env.REACT_APP_API_URL);
    });    

    function handleCurrentUserChange(player) {
        setCurrentUser(player);
    }

    function handleError(error) {
        props.onError(error);
    }

    return (
        <div className="home">
            {!currentUser ? <TempPlayer onCurrentUserChange={handleCurrentUserChange}></TempPlayer> : <Yamb onError={handleError}></Yamb>}
        </div>
    );
};

export default Home;
