import React from 'react';
import { Card, CardContent, Typography } from '@mui/material';
import { Album } from '../types';

interface AlbumCardProps {
    album: Album;
  }
  
  const AlbumCard: React.FC<AlbumCardProps> = ({ album }) => {
    const { title, artist } = album;
  
    return (
      <Card>
        <CardContent>
          <Typography variant="h6" component="div">
            {title}
          </Typography>
          <Typography color="text.secondary">{artist}</Typography>
        </CardContent>
      </Card>
    );
  };
  
  
  export default AlbumCard;
