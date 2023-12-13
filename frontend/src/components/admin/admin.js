import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './admin.css';
import AuthService from '../../api/auth-service';

function Admin() {

    const navigate = useNavigate();   

    const [currentUser] = useState(AuthService.getCurrentPlayer());

    useEffect(() => {
        if (!currentUser?.roles?.find(x => x.label === 'ADMIN')) {
            navigate('/');
        }
    });
    
    return (
        <div className="admin">
            <div className="form">
                Admin
                <br/>
                <br/>
                <a href="/">Home</a>
                <br/>
                <a href="/players">Players</a>
                <br/>
                <a href="/games">Games</a>
                <br/>
                <a href="/scores">Scores</a>
            </div>
        </div>
    );
};

export default Admin;
