import React, { useContext, useEffect } from 'react';
import { CurrentUserContext } from '../../App';
import TempPlayer from '../auth/temp-player';
import Yamb from '../yamb/yamb';
import './home.css';

function Home(props) {

    const { currentUser } = useContext(CurrentUserContext);

    useEffect(() => {
        console.log(process.env.REACT_APP_API_URL);
    });    

    function handleError(error) {
        props.onError(error);
    }

    return (
        <div className="home">
            {currentUser ? <Yamb onError={handleError}></Yamb> : <TempPlayer onError={handleError}></TempPlayer> }
        </div>
    );
};

export default Home;
