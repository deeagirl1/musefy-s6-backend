import React from 'react';
import { Box, Typography, Button } from '@mui/material';

const SecuritySection: React.FC = () => {
  const handleDeleteAccount = () => {
    // Logic for deleting the account
  };
  
  return (
    <Box my={4}>
      <Typography variant="h2">Security</Typography>
      <Box mt={3}>
      <Button variant="contained"  sx={{ backgroundColor: '#1db954' }} onClick={handleDeleteAccount}>
          Change Password
        </Button>
      </Box>
      <Box mt={3}>
      <Button variant="contained"  sx={{ backgroundColor: '#1db954' }} onClick={handleDeleteAccount}>
          Download personal data
        </Button>
      </Box>
      <Box mt={3}>
      <Button variant="contained"  sx={{ backgroundColor: '#FF0000' }} onClick={handleDeleteAccount}>
          Delete Account
        </Button>
      </Box>
    </Box>
  );
};

export default SecuritySection;
