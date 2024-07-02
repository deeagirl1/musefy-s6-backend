import React, { useState, useEffect } from 'react';
import { Playlist } from './types';
import { Card, CardActionArea, CardContent, CardMedia, Typography } from '@mui/material';
import PlaylistService from '../services/music-service/PlaylistService';

const FeaturedPlaylistsSection: React.FC = () => {
  const [playlists, setPlaylists] = useState<Playlist[]>([]);

  useEffect(() => {
    PlaylistService.getPlaylists().then((data) => {
      setPlaylists(data);
      console.log(data);
    });
  }, []);

  return (
    <section style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
      {playlists.map((playlist) => (
        <Card style={{ width: '300px', margin: '10px' }} key={playlist.id}>
          <CardActionArea>
            <CardMedia component="img" height="140" image={playlist.image} title={playlist.title} />
            <CardContent>
              <Typography gutterBottom variant="h5" component="h2">
                {playlist.title}
              </Typography>
            </CardContent>
          </CardActionArea>
        </Card>
      ))}
    </section>
  );
};

export default FeaturedPlaylistsSection;
