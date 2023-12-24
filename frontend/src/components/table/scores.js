import React, { useContext } from 'react';
import { useTranslation } from 'react-i18next';
import { MenuContext } from '../../App';
import Table from './table';

function Scores() {

    const { t } = useTranslation();
    const {isMenuOpen, setMenuOpen} = useContext(MenuContext);

    function handleSettings() {
        setMenuOpen(!isMenuOpen);
    }

    return (
        <div className="table-form">
            {t('scores')}
            <br/>
            <br/>
            <Table resource="score"></Table>
            <button className="settings-button" onClick={handleSettings}>
                <img src="../svg/buttons/cog.svg" alt="Settings" ></img>
            </button>
        </div>   
    );
}

export default Scores;