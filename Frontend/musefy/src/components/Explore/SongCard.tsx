import React from 'react';
import Card from '@mui/material/Card';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import PlayCircleFilledIcon from '@mui/icons-material/PlayCircleFilled';
import FavoriteIcon from '@mui/icons-material/Favorite';
import { Song, UserFavouriteSongs } from '../types';
import { Alert, AlertTitle } from '@mui/material';
import { getDecodedToken, getToken } from '../../services/AuthService';
import userService from '../../services/UserService';

interface SongCardProps {
  song: Song;
  onClick: () => void;
}

const SongCard: React.FC<SongCardProps> = ({ song, onClick }) => {
  const handlePlay = () => {
    onClick();
  };

  const showAlert = (severity: any, message : any) => {
    <Alert severity={severity}>
      <AlertTitle>{severity.charAt(0).toUpperCase() + severity.slice(1)}</AlertTitle>
      {message}
    </Alert>
  };

  const handleAddToFavorites = async () => {
    const token = getToken();
    const decodedToken = getDecodedToken() as { userId?: string } | null;
  
    if (decodedToken && decodedToken.userId) {
      const userFavoriteSongDTO: UserFavouriteSongs = {
        userId: decodedToken.userId,
        songId: song.songId,
      };

      console.log(userFavoriteSongDTO);

      const result = await userService.addSongToFavouriteList(userFavoriteSongDTO);

      if (result) {
        // Handle success
        showAlert('success', 'Song added to favorites successfully');
      } else {
        // Handle failure
        showAlert('error', 'Failed to add song to favorites');
      }
    } else {
      // Handle case where user ID is not available
      showAlert('error', 'User ID not found');
    }
  };

  return (
    <Card sx={{margin : "0 5px"}}>
      <CardMedia component="img" height="140" image={song.imageLink} title={song.title} />
      <CardContent>
        <Typography gutterBottom variant="h5" component="h2">
          {song.title}
        </Typography>
        <Typography color="text.secondary">{song.artist}</Typography>
        <PlayCircleFilledIcon onClick={handlePlay} />
        <FavoriteIcon onClick={handleAddToFavorites} />
      </CardContent>
    </Card>
  );
};

export default SongCard;

