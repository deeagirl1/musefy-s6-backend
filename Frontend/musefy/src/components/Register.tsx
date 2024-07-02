import React, { useState } from 'react';
import { Container, Typography, TextField, Button, FormControlLabel, Checkbox } from '@mui/material';
import { registerUser } from '../services/AuthService';
import { useNavigate } from 'react-router-dom';

export const Register: React.FC = () => {
  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [firstName, setFirstName] = useState<string>("");
  const [lastName, setLastName] = useState<string>("");
  const [termsAccepted, setTermsAccepted] = useState<boolean>(false);

  const navigate = useNavigate();

  const changeUsername = (event: React.ChangeEvent<HTMLInputElement>) => {
    setUsername(event.target.value);
  };

  const changeFirstName = (event: React.ChangeEvent<HTMLInputElement>) => {
    setFirstName(event.target.value);
  };

  const changeLastName = (event: React.ChangeEvent<HTMLInputElement>) => {
    setLastName(event.target.value);
  };

  const changeEmail = (event: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(event.target.value);
  };

  const changePassword = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
  };

  const handleTermsAcceptedChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setTermsAccepted(event.target.checked);
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>): void => {
    event.preventDefault();
    if (username && password && email && firstName && lastName && termsAccepted) {
      registerUser(username, password, email, firstName, lastName)
        .then((response) => {
          navigate('/');
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };

  return (
    <Container maxWidth="xs" sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '70vh' }}>
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', width: '100%' }}>
        <Typography variant="h4" align="center" gutterBottom>
          Create an account
        </Typography>
        <TextField label="Username" fullWidth margin="normal" value={username} onChange={changeUsername} />
        <TextField label="First Name" fullWidth margin="normal" value={firstName} onChange={changeFirstName} />
        <TextField label="Last Name" fullWidth margin="normal" value={lastName} onChange={changeLastName} />
        <TextField label="Email" type="email" fullWidth margin="normal" value={email} onChange={changeEmail} />
        <TextField label="Password" type="password" fullWidth margin="normal" value={password} onChange={changePassword} />
        <FormControlLabel
          control={<Checkbox checked={termsAccepted} onChange={handleTermsAcceptedChange} />}
          label="I accept the Terms & Conditions"
        />
        <Button type="submit" variant="contained" fullWidth sx={{ mt: 2, bgcolor: '#1db954' }}>
          Register
        </Button>
      </form>
    </Container>
  );
};
