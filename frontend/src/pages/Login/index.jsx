import React, { useEffect, useState } from 'react';
import { Button, TextField, Link, Box, Typography, Container } from '@mui/material';

import MuiAlert from '@mui/material/Alert';
import { useLocation, useNavigate } from 'react-router-dom';
import Snackbar from '@mui/material/Snackbar';
import backendCall from '../../utils/network';

const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const salt = process.env.REACT_APP_SALT;

export default function Login({ setLoggedIn }) {

  const [username, setUserName] = useState('');
  const [password, setPassword] = useState('');

  const [message, setMessage] = useState('');
  const [snackType, setSnackType] = useState('success');
  const [isSnackbarOpen, setIsSnackbarOpen] = useState(false);

  const location = useLocation();
  const navigate = useNavigate();

  const onUsernameChange = (event) => {
    setUserName(event.target.value);
  }

  const onPasswordChange = (event) => {
    setPassword(event.target.value);
  }

  const login = async (event) => {
    event.preventDefault();
    // const hashedPassword = bcrypt.hashSync(password, salt);

    await backendCall.post('/api/v1/login', {
      username: username,
      password: password,
    }).then((res) => {
      // window.localStorage.setItem('token', res.data.token);
      window.localStorage.setItem('token', res.data.token);
      window.localStorage.setItem('role', res.data.role);
      setLoggedIn(true);
      navigate('/course', {
        state: {
          isLoginSuccessful: true
        }
      });
      // window.location = '/outing';
    }).catch((err) => {
      console.log('login error : ', err);
      if (err.response && err.response.data && err.response.data.error) {
        setMessage(err.response.data.error);
        setSnackType('error');
        setIsSnackbarOpen(true);
      }
    });
    // let response = sampleData['login'].response;

    // if (response.status == 1) {
    //   window.localStorage.setItem('token', response.token);
    //   window.location = '/home';
    // } else if (response.status == 2) {
    //   setSnackType('error');
    //   setMessage(response.errorMsg);
    //   setIsSnackbarOpen(true);
    // }


  }

  const hanldeSnackbarClose = () => {
    setIsSnackbarOpen(false)
  }

  const handleLogoutMessage = () => {
    setSnackType('success');
    setMessage('You logged out successfully.');
    setIsSnackbarOpen(true);
  }

  const handleLoginNecessaryMessage = () => {
    setSnackType('warning');
    setMessage('You must login to access the application.');
    setIsSnackbarOpen(true);
  }

  const showRegistrationSuccessMessage = () => {
    setSnackType('success');
    setMessage('User is registered successfully.');
    setIsSnackbarOpen(true);
  }

  useEffect(() => {
    let userToken = localStorage.getItem('token');
    let state = location.state;
    if (state !== null && state.isLogOut) {
      window.localStorage.removeItem('token');
      setLoggedIn(false);
      handleLogoutMessage();
    } else if (state !== null && state.showLoginNecessary) {
      handleLoginNecessaryMessage();
    } else if (state !== null && state.isRegisterSuccess) {
      showRegistrationSuccessMessage();
    } else if (userToken != null && userToken != '') {
      window.location = '/home';
    }
    navigate(location.pathname, {});
  }, [])

  return (

    <>
      <Box sx={{
        position: "absolute",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)"
      }}>
        <Typography variant="h3" sx={{ fontWeight: 'bold', marginBottom: 2 }}>
          Algolab
        </Typography>
        <Typography variant="h4" sx={{ color: '#555' }}>
          Interview buddy partner
        </Typography>
        <Typography variant="h5" sx={{
          color: "#555",
          margin: "20px"
        }}>Login</Typography>

        <div>
          <TextField
            id="standard-basic"
            type="text"
            autoComplete="off"
            name="username"
            value={username}
            sx={{ width: "250px" }}
            onChange={onUsernameChange}
            placeholder="User Name"
            required
          />
          <br /><br />
          <TextField
            id="standard-basic"
            type="password"
            autoComplete="off"
            name="password"
            value={password}
            onChange={onPasswordChange}
            placeholder="Password"
            sx={{ width: "250px" }}
            required
          />
          <br /><br />
          <Button
            className="button_style"
            variant="contained"
            color="primary"
            size="small"
            disabled={username === '' || password === ''}
            onClick={login}
            sx={{ padding: "10px 20px" }}
          >
            Login
          </Button>
          <div>
            <p>
              Don't have an account?{' '}
              <Link to="/register">
                <Button onClick={()=>{window.location='/register'}}>Register</Button>
              </Link>
            </p>
          </div>
        </div>

      </Box>
      <Snackbar open={isSnackbarOpen} autoHideDuration={4000} onClose={hanldeSnackbarClose}>
        <Alert severity={snackType} onClose={hanldeSnackbarClose}>{message}</Alert>
      </Snackbar>

    </>

  );
}
