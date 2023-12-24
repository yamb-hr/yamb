import React, { useContext } from 'react';
import { useTranslation } from 'react-i18next';
import { MenuContext } from '../../App';
import Table from './table';

function Players() {

    const { t } = useTranslation();
    const { isMenuOpen, setMenuOpen } = useContext(MenuContext);

    function handleSettings() {
        setMenuOpen(!isMenuOpen);
    }

    return (
        <div className="table-form">
            {t('players')}
            <br/>
            <br/>
            <Table resource="player"></Table>
            <button className="settings-button" onClick={handleSettings}>
                <img src="../svg/buttons/cog.svg" alt="Settings" ></img>
            </button>
        </div>   
    );
}

export default Players;