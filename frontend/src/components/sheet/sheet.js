import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { LanguageContext, ThemeContext } from '../../App';
import Column from '../column/column';
import Label from '../label/label';
import './sheet.css';

function Sheet(props) {

    const navigate = useNavigate();
    const {theme, toggleTheme} = useContext(ThemeContext);
    const {language, toggleLanguage} = useContext(LanguageContext);
    const { t } = useTranslation();

    function handleSettings() {
        toggleLanguage();
    }

    function handleInfo() {
        toggleTheme();
    }

    function handleLogout() {
        props.onLogout();
    }

    function handleRollDice() {
        props.onRollDice();
    }

    function handleRestart() {
        props.onRestart();
    }

    function handleBoxClick(columnType, boxType) {
        props.onBoxClick(columnType, boxType);
    }

    function handleAdminClick() {
        navigate('/admin');
    }

    const {
        columns,
        rollCount,
        announcement,
        player,
        currentUser
    } = props;

    function getTotalSum() {
        return getTopSectionSum() + getMiddleSectionSum() + getBottomSectionSum();
    }

    function getTopSectionSum() {
        let sum = 0;
        for (let i in columns) {
            sum += getTopSectionSumByIndex(i);
        }
        return sum;
    }

    function getMiddleSectionSum() {
        let sum = 0;
        for (let i in columns) {
            sum += getMiddleSectionSumByIndex(i);
        }
        return sum;
    }

    function getBottomSectionSum() {
        let sum = 0;
        for (let i in columns) {
            sum += getBottomSectionSumByIndex(i);
        }
        return sum;
    }

    function getTopSectionSumByIndex(columnIndex) {
        let sum = 0;
        for (let i = 0; i < 6; i++) {
            if (columns[columnIndex].boxes[i].value) {
                sum += columns[columnIndex].boxes[i].value;
            }
        }
        if (sum >= 60) {
            sum += 30;
        }
        return sum;
    }

    function getMiddleSectionSumByIndex(columnIndex) {
        let sum = 0;
        if (columns[columnIndex].boxes[0].value && columns[columnIndex].boxes[6].value && columns[columnIndex].boxes[7].value) {
            sum = columns[columnIndex].boxes[0].value * (columns[columnIndex].boxes[6].value - columns[columnIndex].boxes[7].value);
        }
        return sum;
    }

    function getBottomSectionSumByIndex(columnIndex) {
        let sum = 0;
        for (let i = 8; i < 13; i++) {
            if (columns[columnIndex].boxes[i].value) {
                sum += columns[columnIndex].boxes[i].value;
            }
        }
        return sum;
    }

    function isAnnouncementRequired() {
        rollCount === 1 && announcement == null && areAllNonAnnouncementColumnsCompleted();
        return false;
    }

    function areAllNonAnnouncementColumnsCompleted() {
        for (let i in columns) {
            if (columns[i].type !== "ANNOUNCEMENT" && !isColumnCompleted(i)) {
                return false;
            }
        }
        return true;
    }

    function isColumnCompleted(columnIndex) {
        for (let i in columns[columnIndex].boxes) {
            if (columns[columnIndex].boxes[i].value) {
                return false;
            }
        }
        return true;
    }

    let rollDiceButtonDisabled = rollCount === 3 || isAnnouncementRequired();

    return (
        <div className="sheet">
            <div className="column">
                <button className="settings-button" onClick={handleSettings}>
                    <img src="../svg/buttons/language.svg" alt={language} ></img>
                </button>
                <Label icon="ones" info={t('ones')}></Label>
                <Label icon="twos" info={t('twos')}></Label>
                <Label icon="threes" info={t('threes')}></Label>
                <Label icon="fours" info={t('fours')}></Label>
                <Label icon="fives" info={t('fives')}></Label>
                <Label icon="sixes" info={t('sixes')}></Label>
                <Label variant="sum" value="Σ (1, 6)" info={t('top-section-sum')}></Label>
                {/* MID SECTION */}
                <Label value={t('max')} info="Zbroj svih kockica"></Label>
                <Label value={t('min')} info="Zbroj svih kockica"></Label>
                <Label variant="sum" value="∆ x 1" info={t('middle-section-sum')}></Label>
                {/* BOTTOM SECTION */}
                <Label value={t('trips')} info={t('trips-info')}></Label>
                <Label value={t('straight')} info={t('straight-info')}></Label>
                <Label value={t('boat')} info={t('boat-info')}></Label>
                <Label value={t('carriage')} info={t('carriage-info')}></Label>
                <Label value={t('yamb')} info={t('yamb-info')}></Label>
                <Label variant="sum" value="Σ (T, J)" info={t('bottom-section-sum')}></Label>
            </div>
            {columns.map((column, index) => (
                <div className="column" key={column.type}>
                    <Column 
                        type={column.type} 
                        boxes={column.boxes} 
                        rollCount={rollCount}
                        announcement={announcement}
                        topSectionSum={getTopSectionSumByIndex(index)}
                        middleSectionSum={getMiddleSectionSumByIndex(index)}
                        bottomSectionSum={getBottomSectionSumByIndex(index)}
                        onBoxClick={handleBoxClick}>
                    </Column> 
                </div>
            ))}
            <div className="column">
                <button className="info-button" onClick={handleInfo}>
                    <img src={"../svg/buttons/" + (theme === "dark" ? "sun" : "moon") + ".svg"} alt="Language"></img>
                </button>
                {currentUser?.roles?.find(x => x.label=== "ADMIN") && <button className="admin-button" onClick={handleAdminClick}> 
                    <img src={"../svg/buttons/cog.svg"} alt="Admin"></img>
                </button>}
                <button className="roll-button" onClick={handleRollDice} disabled={rollDiceButtonDisabled}>
                    <img src={"../svg/buttons/roll-" + (3-rollCount) + ".svg"} alt="Roll"></img>
                </button>                    
                <div className="top-section-sum">
                    <Label variant="sum" value={getTopSectionSum()}></Label>
                </div>
                <button className="restart-button" onClick={handleRestart}>
                    <img src={"../svg/buttons/restart.svg"} alt="Restart"></img>
                </button>
                <div className="middle-section-sum">
                    <Label variant="sum" value={getMiddleSectionSum()}></Label>
                </div>
                <div className="bottom-section-sum">
                    <Label variant="sum" value={getBottomSectionSum()}></Label>
                </div>
            </div>
            <div className="last-row">
                <button className="username-button">{player.username}</button>
                {currentUser.tempUser ? <a className="register-sheet-button" href="/register">{t('register')}</a> : 
                <button className="logout-button" onClick={handleLogout}>{t('logout')}</button>}
                <Label variant="total-sum" value={getTotalSum()}></Label>
            </div>
        </div>
    );
}

export default Sheet;
