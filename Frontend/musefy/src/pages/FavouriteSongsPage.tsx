import React, { useState, useEffect } from 'react';
import SongCard from '../components/Explore/SongCard';
import { Song } from '../components/types';
import userService from '../services/UserService';
import { getDecodedToken, getToken } from '../services/AuthService';
import songService from '../services/music-service/SongService';

const FavouriteSongsPage: React.FC = () => {
  const [favoriteSongs, setFavoriteSongs] = useState<Song[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const decodedToken = getDecodedToken() as { userId: string };
  
        if (decodedToken && decodedToken.userId) {
          const userId = decodedToken.userId;
          const songIds = await userService.getUserFavoriteSongs(userId);
  
          if (songIds) {
            const fetchSongsPromises = songIds.map(async (songId) => {
              const song = await songService.getSongById(songId);
              return song;
            });
  
            const songs = await Promise.all(fetchSongsPromises);
            const filteredSongs = songs.filter((song) => song !== null) as Song[];
  
            setFavoriteSongs(filteredSongs);
          }
        }
      } catch (error) {
        console.error(error);
      }
    };
  
    fetchData();
  }, []);
  
  
  return (
    <div>
      <h2>Favourite Songs</h2>
      <div className="song-list">
        {favoriteSongs.map((song) => (
          <SongCard key={song.songId} song={song} onClick={() => {}} />
        ))}
      </div>
    </div>
  );
};

export default FavouriteSongsPage;
