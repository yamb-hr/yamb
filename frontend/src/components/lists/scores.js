import React, { useContext } from 'react';
import { useTranslation } from 'react-i18next';
import { MenuContext } from '../../App';

function Scores() {

    const { t } = useTranslation();
    const {isMenuOpen, setMenuOpen} = useContext(MenuContext);

    function handleSettings() {
        setMenuOpen(!isMenuOpen);
    }

    return (
        <div className="form">
            {t('scores')}
            <br/>
            <br/>
            <button className="settings-button" onClick={handleSettings}>
                <img src="../svg/buttons/cog.svg" alt="Settings" ></img>
            </button>
        </div>   
    );
}

export default Scores;