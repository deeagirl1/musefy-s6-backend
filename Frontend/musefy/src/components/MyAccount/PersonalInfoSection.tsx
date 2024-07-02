import React from 'react';
import { Box, Typography, TextField, Grid, Button } from '@mui/material';

const PersonalInfoSection: React.FC = () => {
  return (
    <Box my={4}>
      <Typography variant="h2">Personal Information</Typography>
      <Box mt={3}>
        <Grid container spacing={2}>
          <Grid item xs={6}>
            <TextField label="First Name" fullWidth />
          </Grid>
          <Grid item xs={6}>
            <TextField label="Last Name" fullWidth />
          </Grid>
        </Grid>
      </Box>
      <Box mt={3}>
        <TextField label="Email" fullWidth />
      </Box>
      <Box mt={3}>
        <Button  variant="contained"  sx={{ backgroundColor: '#1db954' }}>
          Save Changes
        </Button>
      </Box>
    </Box>
  );
};

export default PersonalInfoSection;
