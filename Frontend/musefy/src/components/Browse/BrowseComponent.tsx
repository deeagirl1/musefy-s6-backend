import { Typography, Box } from '@mui/material';
import Carousel from 'react-material-ui-carousel';
import { Album, Playlist, Song } from '../types';
import { useState, useEffect } from 'react';
import AlbumCard from '../Explore/AlbumCard';
import PlaylistCard from '../Explore/PlaylistCard';
import SongCard from '../Explore/SongCard';
import AlbumService from '../../services/music-service/AlbumService';
import PlaylistService from '../../services/music-service/PlaylistService';
import songService from '../../services/music-service/SongService';
import LoadingPage from '../../pages/Loading/LoadingPage';
import Playbar from '../PlayBar/Playbar';

const BrowseComponent: React.FC = () => {
  const [songsData, setSongsData] = useState<Song[]>([]);
  const [playlistsData, setPlaylistsData] = useState<Playlist[]>([]);
  const [albumsData, setAlbumsData] = useState<Album[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [selectedSong, setSelectedSong] = useState<Song | null>(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const [volume, setVolume] = useState(50);
  const [currentSongIndex, setCurrentSongIndex] = useState(0);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const songs = await songService.getSongs();
        const playlists = await PlaylistService.getPlaylists();
        const albums = await AlbumService.getAlbums();

        setSongsData(songs);
        setPlaylistsData(playlists);
        setAlbumsData(albums);
        setIsLoading(false);
      } catch (error) {
        console.error(error);
        setIsLoading(false);
      }
    };

    fetchData();
  }, []);

  if (isLoading) {
    return <LoadingPage />;
  }

  const handleSongClick = (song: Song) => {
    setSelectedSong(song);
    setIsPlaying(true); // Play the selected song when clicked
    const songIndex = songsData.findIndex((s) => s.songId === song.songId);
    setCurrentSongIndex(songIndex);
  };
  const handlePlayPause = () => {
    setIsPlaying(isPlaying); // Toggle the play/pause state
  };

  const handleVolumeChange = (
    event: React.SyntheticEvent | Event,
    newValue: number | number[]
  ) => {
    if (typeof newValue === 'number') {
      setVolume(newValue); // Update the volume value
    }
  };

  const handleSkipPrevious = () => {
    if (selectedSong && songsData.length > 1) {
      const currentIndex = songsData.findIndex(song => song.songId === selectedSong.songId);
      const previousIndex = (currentIndex - 1 + songsData.length) % songsData.length;
      const previousSong = songsData[previousIndex];
      setSelectedSong(previousSong);
      setCurrentSongIndex(previousIndex);
      setIsPlaying(true);
    }
  };
  
  const handleSkipNext = () => {
    if (selectedSong && songsData.length > 1) {
      const currentIndex = songsData.findIndex(song => song.songId === selectedSong.songId);
      const nextIndex = (currentIndex + 1) % songsData.length;
      const nextSong = songsData[nextIndex];
      setSelectedSong(nextSong);
      setCurrentSongIndex(nextIndex);
      setIsPlaying(true);
    }
  };
  


  const chunkSize = 4; // Number of cards per slide
  const songChunks = [];
  for (let i = 0; i < songsData.length; i += chunkSize) {
    songChunks.push(songsData.slice(i, i + chunkSize));
  }

  return (
    <div>
      <Typography variant="h4" gutterBottom>
        Songs
      </Typography>
      <Carousel animation="slide" navButtonsAlwaysVisible>
        {songChunks.map((songGroup, index) => (
          <div
            key={index}
            style={{
              display: 'flex',
              justifyContent: 'space-between',
            }}
          >
            {songGroup.map((song) => (
              <SongCard
                key={song.songId}
                song={song}
                onClick={() => handleSongClick(song)}
              />
            ))}
          </div>
        ))}
      </Carousel>

      <Typography variant="h4" gutterBottom>
        Playlists
      </Typography>
      <Carousel animation="slide" navButtonsAlwaysVisible>
        {playlistsData.map((playlist) => (
          <PlaylistCard key={playlist.id} playlist={playlist} />
        ))}
      </Carousel>

      <Typography variant="h4" gutterBottom>
        Albums
      </Typography>
      <Carousel animation="slide" navButtonsAlwaysVisible>
        {albumsData.map((album) => (
          <AlbumCard key={album.albumId} album={album} />
        ))}
      </Carousel>

      <Box sx={{ paddingBottom: "10rem" }}>
        {selectedSong && (
          <Playbar
            song={selectedSong}
            isPlaying={isPlaying}
            volume={volume}
            onPlayPause={handlePlayPause}
            onVolumeChange={handleVolumeChange}
            onSkipNext={handleSkipNext}
            onSkipPrevious={handleSkipPrevious}
          />
        )}
      </Box>

    </div>
  );
};

export default BrowseComponent;
