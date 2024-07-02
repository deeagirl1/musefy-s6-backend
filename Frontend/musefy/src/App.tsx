import './App.css';
import Navbar from './components/Navigation/navbar';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from './pages/homepage';
import BrowsePage from './pages/BrowsePage';
import LoginPage from './pages/loginPage';
import RegisterPage from './pages/RegisterPage';
import NotFoundPage from './pages/NotFound/NotFoundPage';
import MyAccountPage from './pages/MyAccountPage';
import FavouriteSongsPage from './pages/FavouriteSongsPage';

function App() {
  return (
    <div className="App">
        <Navbar />
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home />}></Route>
                <Route path="/login" element={<LoginPage />}></Route>
                <Route path="/register" element={<RegisterPage />}></Route>
                <Route path="/browse" element={<BrowsePage/>}></Route>
                <Route path="/my-favourite-songs" element={<FavouriteSongsPage/>}></Route>        
                <Route path="/my-account" element={<MyAccountPage/>}></Route>  
                <Route path="*" element={<NotFoundPage/>} />
            </Routes>
        </BrowserRouter>
    </div>
);
}

export default App;
