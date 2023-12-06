import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/auth/login';
import Register from './components/auth/register';
import Games from './components/games/games';
import Players from './components/players/players';
import Scores from './components/scores/scores';
import Yamb from './components/yamb';
import './App.css';

function App() {

	return (
		<div className="App">
			<header className="App-header">
			<Router>
				<title>Jamb</title>
				<Routes>
					<Route path="/" element={<Yamb />} />
					<Route path="/login" element={<Login />} />
					<Route path="/register" element={<Register />} />
					<Route path="/players" element={<Players />} />
					<Route path="/scores" element={<Scores />} />
					<Route path="/games" element={<Games />} />
				</Routes>
			</Router>
			</header>
		</div>
	);
}

export default App;
