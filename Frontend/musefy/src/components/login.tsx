import React, { useEffect, useState } from 'react';
import { Container, Typography, TextField, Button } from '@mui/material';
import { ThunkDispatch, AnyAction } from '@reduxjs/toolkit';
import { useNavigate, Link } from 'react-router-dom';
import { authenticateUser, authenticationSuccess, authenticationFailure } from '../services/AuthService';
import { useDispatch } from 'react-redux';

export const Login: React.FC = (props: any) => {
  
  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");

  const navigate = useNavigate();
  const dispatch: ThunkDispatch<{}, {}, AnyAction> = useDispatch();

  const changeUsername = (event: React.ChangeEvent<HTMLInputElement>) => {
    setUsername(event.target.value);
  };

  const changePassword = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
  };

  const handleSubmit = async (event: React.SyntheticEvent) => {
    event.preventDefault();
    if (username && password) {
      try {
        const response = await dispatch(authenticateUser(username, password));
        if (response.success) {
          navigate('/browse');
          window.location.reload(); 
        } else {
          dispatch(authenticationFailure('Invalid username or password'));
        }
      } catch (error: any) {
        dispatch(authenticationFailure(error.message));
      }
    }
  };

  useEffect(() => {
  }, []);

  return (
    <Container maxWidth="xs" sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '50vh' }}>
      <Typography variant="h4" align="center" gutterBottom>
        Sign in 
      </Typography>
      <TextField label="Email or username" fullWidth margin="normal" onChange={changeUsername} />
      <TextField label="Password" type="password" fullWidth margin="normal"  onChange={changePassword}/>
      <Button variant="contained" fullWidth sx={{ backgroundColor: '#1db954', marginTop: '16px' }} onClick={handleSubmit}>
        Log in
      </Button>
      <Typography variant="body2" align="center" sx={{ marginTop: '16px' }}>
        Don't have an account? <Link to="/register">Register</Link>
      </Typography>
    </Container>
  );
};
