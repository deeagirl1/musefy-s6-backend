import React from 'react';
import { Container, Typography } from '@mui/material';
import PersonalInfoSection from '../components/MyAccount/PersonalInfoSection';
import PreferencesSection from '../components/MyAccount/PreferenceSection';
import SecuritySection from '../components/MyAccount/SecuritySection';

const MyAccountPage: React.FC = () => {
  return (
    <Container maxWidth="md">
      <Typography variant="h1">My Account</Typography>
      <PersonalInfoSection />
      <PreferencesSection />
      <SecuritySection />
    </Container>
  );
};

export default MyAccountPage;
