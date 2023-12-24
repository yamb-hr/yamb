import React, { useContext } from 'react';
import { MenuContext } from '../../App';
import { useTranslation } from 'react-i18next';

function Games() {

    const { t } = useTranslation();
    const {isMenuOpen, setMenuOpen} = useContext(MenuContext);

    function handleSettings() {
        setMenuOpen(!isMenuOpen);
    }

    return (
        <div className="form">
            {t('games')}
            <br/>
            <br/>
            <button className="settings-button" onClick={handleSettings}>
                <img src="../svg/buttons/cog.svg" alt="Settings" ></img>
            </button>
        </div>   
    );
      
}

export default Games;