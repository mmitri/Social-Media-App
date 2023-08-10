import React from 'react';
import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Route, Routes, useParams} from "react-router-dom";
import { useEffect, useState, useRef } from 'react';
import HomePage from './pages/HomePage/HomePage';
import LoginPage from './pages/LoginPage/LoginPage';
import NewPostPage from './pages/NewPostPage/NewPostPage';
import ProfilePage from './pages/ProfilePage/ProfilePage';
import OtherProfile from './pages/OtherProfile/OtherProfile';
import Comment from './pages/Comment/Comment';
import AddFile from './pages/AddFile/NewFilePage';
import jwt_decode from "jwt-decode";
import { getModeForUsageLocation } from 'typescript';
import Navbar from './components/Navbar';
import NewFilePage from './pages/AddFile/NewFilePage';



function App() {
  /**
   * list of routes to different pages
   */
  return (
    <>
    <div className = "App">
      <Router>
        <Routes>
            <Route path="/login" element={<LoginPage/>} />
            <Route path = "/" element={<HomePage/>} />
            <Route path = "/add-post" element={<NewPostPage/>} />
            <Route path = "/edit-profile" element={<ProfilePage/>} />
            <Route path = "/edit-user-profile" element={<OtherProfile/>} />
            <Route path = "/comment" element={<Comment post_id={8} message={''}/>} />
            <Route path = "/add-file" element={<NewFilePage/>} />
        </Routes>
      </Router>
    </div>


<div className="container mx-auto bg-gray-200 rounded-l shadow border p-6 m-6">
<p className="text-xl text-gray-700 font-bold mb-5">
  The Buzz ©
</p>
</div>

</>
  );
}

export default App;