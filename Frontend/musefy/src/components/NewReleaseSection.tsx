import React, { useState, useEffect } from 'react';
import { Album } from './types';
import { Card, CardActionArea, CardContent, CardMedia, Typography } from '@mui/material';
import AlbumService from '../services/music-service/AlbumService';

const NewReleaseSection: React.FC= () => {
  const [albums, setAlbums] = useState<Album[]>([]);

  useEffect(() => {
    AlbumService.getAlbums().then((data) => {
      setAlbums(data);
      console.log(data);
    });
  }, []);

  return (
    <section style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
      {albums.map((album) => (
        <Card style={{ width: '300px', margin: '10px' }} key={album.albumId}>
          <CardActionArea>
            {/* <CardMedia component="img" height="140" image={album.image} title={album.title} /> */}
            <CardContent>
              <Typography gutterBottom variant="h5" component="h2">
                {album.title}
              </Typography>
            </CardContent>
          </CardActionArea>
        </Card>
      ))}
    </section>
  );
};

export default NewReleaseSection;
