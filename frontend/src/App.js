import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ToastContainer, toast, Slide } from 'react-toastify';
import { createContext, useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import SockJsClient from "react-stomp";
import i18n from './i18n';
import Home from './components/home/home';
import Login from './components/auth/login';
import Register from './components/auth/register';
import Players from './components/lists/players';
import Scores from './components/lists/scores';
import Games from './components/lists/games';
import Admin from './components/admin/admin';
import Yamb from './components/yamb/yamb';
import Chat from './components/chat/chat';
import Dashboard from './components/dashboard/dashboard';
import AuthService from './api/auth-service';
import PlayerService from "./api/player-service";
import 'react-toastify/dist/ReactToastify.css';
import './App.css';

export const ThemeContext = createContext(null);
export const LanguageContext = createContext(null);
export const CurrentUserContext = createContext(null);

var socket = null

function App() {
	
	const { t } = useTranslation();
	const [ theme, setTheme ] = useState(getCurrentTheme());
	const [ language, setLanguage ] = useState(getCurrentLanguage());
	const [ currentUser, setCurrentUser ] = useState(AuthService.getCurrentPlayer());

    const [ principal, setPrincipal ] = useState(null);
    const [ connected, setConnected ] = useState(false);
    const [ topics, setTopics ] = useState(["/chat/public"]);

	useEffect(() => {
        if (principal) {
            let newTopics = ["/chat/public"];
            setTopics([...newTopics, "/player/" + principal + "/private"]);
        }
    }, [principal]);

	function toggleTheme() {
		let newTheme = theme === "dark" ? "light" : "dark";
		setTheme(newTheme);
		toast.info(t('theme-changed') + newTheme, {
            position: "top-center",
				autoClose: 1000,
				transition: Slide,
				hideProgressBar: true,
				closeOnClick: true,
				pauseOnHover: false,
				pauseOnFocusLoss: false,
				draggable: true,
				progress: undefined,
				theme: theme
        });
		document.documentElement.setAttribute("theme", newTheme);
		localStorage.setItem("nextTheme", newTheme === "dark" ? "light" : "dark");
	}

	function getCurrentTheme() {
		let theme = "dark";
		if (localStorage.getItem("nextTheme")) {
			if (localStorage.getItem("nextTheme") === "dark") {
				theme = "light";
			} else {
				theme = "dark";
			}
		} else {
			if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
				theme = "dark";
			} else {
				theme = "light"
			}
		}
		document.documentElement.setAttribute("theme", theme);
		return theme;
	}

	function getCurrentLanguage() {
        if (localStorage.getItem("i18nextLng")) {
            if (localStorage.getItem("i18nextLng") === "en-US") {
                return "hr";
            } else {
                return "en-US";
            }
        } else {
            return navigator.language || navigator.userLanguage;
        }
    }

	function toggleLanguage() {
        if (language === "en-US") {
            setLanguage("hr");
        } else {
            setLanguage("en-US");
        }
        toast.info(t('language-changed') + language, {
            position: "top-center",
				autoClose: 1000,
				transition: Slide,
				hideProgressBar: true,
				closeOnClick: true,
				pauseOnHover: false,
				pauseOnFocusLoss: false,
				draggable: true,
				progress: undefined,
				theme: theme
        });
        i18n.changeLanguage(language);
    }

	function handleError(error) {
		console.error(error?.message);
		toast.error(error?.message, {
			position: "top-center",
			autoClose: 2000,
			transition: Slide,
			hideProgressBar: true,
			closeOnClick: true,
			pauseOnHover: true,
			pauseOnFocusLoss: true,
			draggable: true,
			progress: undefined,
			theme: theme
		});
	}

	function handleConnected() {
        setConnected(true);
        PlayerService.getPrincipalById(
            currentUser.id
        )
        .then(data => {
            setPrincipal(data.principal);
        }
        ).catch(error => {
            handleError(error);
        });
    }

	function handleMessage(message) {
		console.log(message);
	}

	function handleDisconnected() {
		setPrincipal(null);
		console.log("Disconnected");
	}

	function sendMessage(message, channel) {
		if (socket) {
			socket.sendMessage("/app" + channel, JSON.stringify(message));
		}
	}

	return (
		<div className="App">
			<header className="App-header">
				<CurrentUserContext.Provider value={{ currentUser, setCurrentUser}}>
					<ThemeContext.Provider value={{ theme, toggleTheme}}>
						<LanguageContext.Provider value={{ language, toggleLanguage}}>
								<Router>
									<Routes>
										<Route path="/" element={<Home onError={handleError}/>} />
										<Route path="/login" element={<Login onError={handleError}/>} />
										<Route path="/register" element={<Register onError={handleError}/>} />
										<Route path="/players" element={<Players onError={handleError}/>} />
										<Route path="/scores" element={<Scores onError={handleError}/>} />
										<Route path="/games" element={<Games onError={handleError}/>} />
										<Route path="/games/:id" element={<Yamb onError={handleError}/>} />
										<Route path="/admin" element={<Admin onError={handleError}/>} />
										<Route path="/chat" element={<Chat onError={handleError}/>} />
										<Route path="/dashboard" element={<Dashboard onError={handleError}/>} />
									</Routes>
								</Router>
						</LanguageContext.Provider>
					</ThemeContext.Provider>
				</CurrentUserContext.Provider>
				<ToastContainer limit={5} style={{fontSize:"medium"}}/>
				{currentUser && <SockJsClient url={process.env.REACT_APP_API_URL + "/ws?token=" + currentUser.token}
					topics={topics}
					onMessage={(message) => {
                        handleMessage(message);
					}}
					onConnect={() => {
                        handleConnected();
					}}
					onDisconnect={() => {
						handleDisconnected();
					}}
					ref={(client) => {
						socket = client;
					}} />}
			</header>
		</div>
	);
}

export default App;
