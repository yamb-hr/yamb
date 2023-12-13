import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ToastContainer, toast, Slide } from 'react-toastify';
import { createContext, useState } from 'react';
import i18n from './i18n';
import Home from './components/home/home';
import Login from './components/auth/login';
import Register from './components/auth/register';
import Players from './components/lists/players';
import Scores from './components/lists/scores';
import Games from './components/lists/games';
import Yamb from './components/yamb/yamb';
import 'react-toastify/dist/ReactToastify.css';
import './App.css';
import { useTranslation } from 'react-i18next';
import Admin from './components/admin/admin';

export const ThemeContext = createContext(null);
export const LanguageContext = createContext(null);

function App() {

	const { t } = useTranslation();

	const [theme, setTheme] = useState(getCurrentTheme());
	const [language, setLanguage] = useState(getCurrentLanguage());

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

	return (
		<div className="App">
			<header className="App-header">
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
								</Routes>
							</Router>
					</LanguageContext.Provider>
				</ThemeContext.Provider>
				<ToastContainer limit={5} style={{fontSize:"medium"}}/>
			</header>
		</div>
	);
}

export default App;
