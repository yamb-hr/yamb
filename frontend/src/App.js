import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/auth/login';
import Register from './components/auth/register';
import Games from './components/lists/games';
import Players from './components/lists/players';
import Scores from './components/lists/scores';
import Yamb from './components/yamb/yamb';
import './App.css';

function App() {

	return (
		<div className="App">
			<header className="App-header">
			<Router>
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
