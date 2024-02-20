import './App.css';
import { Alert, Box, CircularProgress, Snackbar } from "@mui/material";
import { BrowserRouter, Routes, Route, Navigate, Router } from "react-router-dom";
import React, { lazy, Suspense, useEffect, useState } from "react";
// import Profile from './pages/Profile';
// import AppLayout from './components/AppLayout';
// import Sidebar from './components/Sidebar';

const Login = lazy(() => import("./pages/Login"));
const Register = lazy(() => import("./pages/Register"));
const Logout = lazy(() => import("./pages/Logout"));
const Course = lazy(() => import("./pages/Course"));
const Profile = lazy(() => import("./pages/Profile"));
const Feedback = lazy(() => import("./pages/Feedback"));
const Lesson = lazy(() => import("./pages/Lesson"));
const Issue = lazy(() => import("./pages/Issue"));
const Announcement = lazy(() => import("./pages/Announcement"));

function App() {

  const [loggedIn, setLoggedIn] = useState('a');

  useEffect(() => {
    let userToken = localStorage.getItem('token');
    if (userToken != null && userToken != '') {
      setLoggedIn(true);
    } else {
      setLoggedIn(false);
    }
  }, []);


  return (

    <>
      {
        loggedIn !== 'a' &&
        <BrowserRouter>
          <Suspense
            fallback={
              <Box className="display-center">
                <CircularProgress sx={{ margin: "auto" }} />
              </Box>
            }>
            <Routes>
              <Route path="/" exact element={loggedIn ? <Navigate to='/course' state={{ isLoggedIn: true }} /> : <Navigate to='/login' state={{ showLoginNecessary: true }} />} />
              <Route path="/login" exact element={loggedIn ? <Navigate to='/course' state={{ isLoggedIn: true }} /> : <Login setLoggedIn={setLoggedIn} />} />
              <Route path="/register" exact element={<Register setLoggedIn={setLoggedIn} />} />
              <Route path="/logout" exact element={<Logout setLoggedIn={setLoggedIn} />} />
              <Route path="/course" exact element={loggedIn ? <Course /> : <Navigate to='/login' state={{ showLoginNecessary: true }} />} />
              <Route path="/profile" exact element={loggedIn ? <Profile /> : <Navigate to='/login' state={{ showLoginNecessary: true }} />} />
              <Route path="/feedback" exact element={loggedIn ? <Feedback /> : <Navigate to='/login' state={{ showLoginNecessary: true }} />} />
              <Route path="/issue" exact element={loggedIn ? <Issue /> : <Navigate to='/login' state={{ showLoginNecessary: true }} />} />
              <Route path="/lesson/:courseId" exact element={loggedIn ? <Lesson /> : <Navigate to='/login' state={{ showLoginNecessary: true }} />} />
              <Route path="/announcement/:courseId" exact element={loggedIn ? <Announcement /> : <Navigate to='/login' state={{ showLoginNecessary: true }} />} />
            </Routes>
          </Suspense>
        </BrowserRouter>
      }

    </>





  );
}

export default App;
