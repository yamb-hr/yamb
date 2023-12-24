import React, { useContext } from 'react';
import { useTranslation } from 'react-i18next';
import { MenuContext } from '../../App';

function Players() {

    const { t } = useTranslation();
    const {isMenuOpen, setMenuOpen} = useContext(MenuContext);

    function handleSettings() {
        setMenuOpen(!isMenuOpen);
    }

    return (
        <div className="form">
            {t('players')}
            <br/>
            <br/>
            <button className="settings-button" onClick={handleSettings}>
                <img src="../svg/buttons/cog.svg" alt="Settings" ></img>
            </button>
        </div>   
    );
}

export default Players;