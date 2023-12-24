import { useContext, useEffect, useState } from "react";
import { ErrorContext, LanguageContext } from "../../App";
import PlayerService from "../../api/player-service";
import GameService from "../../api/game-service";
import ScoreService from "../../api/score-service";
import "./table.css";

function Table(props) {

    const { handleError } = useContext(ErrorContext);
    const { language } = useContext(LanguageContext);
    const [ data, setData ] = useState(null);
    const [ size, setSize ] = useState(10);
    const [ page, setPage ] = useState(0);
    const [ order, setOrder ] = useState('id');
    const [ direction, setDirection ] = useState('asc');

    const localeStringFormat = {
        year: 'numeric', month: 'long', day: 'numeric'
    }
    
    useEffect(() => {
        switch (props.resource) {
            case 'player':
                PlayerService.getPlayers(size, page, order, direction)
                .then(data => {
                    console.log(data);
                    setData(data);  
                })
                .catch(error => {
                    handleError(error);
                });
                break;
            case 'game':
                GameService.getGames(size, page, order, direction)
                .then(data => {
                    console.log(data);
                    setData(data);  
                })
                .catch(error => {
                    handleError(error);
                });
                break;
            case 'score': 
                ScoreService.getScores(size, page, order, direction)
                .then(data => {
                    console.log(data);
                    setData(data);  
                })
                .catch(error => {
                    handleError(error);
                });
                break;
            default:
                break;
        }
    }, [size, page, order, direction, setData, props.resource]);

    function toggleDirection() {
        if (direction === 'asc') {
            setDirection('desc');
        } else {
            setDirection('asc');
        }
    }

    function handleSizeChange(event) {
        setSize(event.target.value);
        setPage(0);
    }

    return(
        <div>
            {data && <table className="table">
                <thead>
                    <tr> 
                        {data[0] && [...Object.keys(data[0])]?.map((field, index) => {
                            if (!Array.isArray(data[0][field]) && typeof(data[0][field]) !== 'object') {
                                return <th key={index} onClick={() => {setOrder(field); toggleDirection();}}>{field}</th>
                            }
                            return null;
                        })} 
                    </tr>
                </thead>
                <tbody>
                    {data.map((element, index) => (
                        <tr key={index}>
                            {[...Object.keys(element)]?.map((field, index) => {
                                if (!Array.isArray(element[field]) && typeof(element[field]) !== 'object') {
                                    if (field === 'date') {
                                        return <td key={index}><time dateTime={element[field]}>{new Date(element[field]).toLocaleString(language, localeStringFormat)}</time></td>
                                    } else {
                                        return <td key={index}>{element[field]}</td>
                                    }
                                }
                                return null;
                            })} 
                        </tr>
                    ))} 
                </tbody>
            </table>}
            <br/>
            <div>
                <button onClick={() => {setPage(page - 1)}} disabled={page === 0} className="page-control">{"<"}</button>
                &nbsp;&nbsp;{page}&nbsp;&nbsp;
                <button onClick={() => {setPage(page + 1)}} disabled={data && data.length < size} className="page-control">{">"}</button>
            </div>  
            <br/>
            <label>Rows per page: </label>
            <select value={size} onChange={handleSizeChange}>
                <option value="10">10</option>
                <option value="20">20</option>
                <option value="50">50</option>
                <option value="100">100</option>
            </select>
        </div>
    )
}

export default Table;