import React from 'react';
import { Card, CardContent, CardMedia, Typography } from '@mui/material';
import { Playlist } from '../types';

interface PlaylistCardProps {
    playlist: Playlist;
  }
  
  const PlaylistCard: React.FC<PlaylistCardProps> = ({ playlist }) => {

    return (
      <Card style={{ width: '300px', margin: '10px' }}>
        <CardMedia component="img" height="140" image={playlist.image} title={playlist.title} />
        <CardContent>
          <Typography gutterBottom variant="h5" component="h2">
            {playlist.title}
          </Typography>
          <Typography color="text.secondary">{playlist.created_by}</Typography>
        </CardContent>
    </Card>
    );
  };


  export default PlaylistCard;