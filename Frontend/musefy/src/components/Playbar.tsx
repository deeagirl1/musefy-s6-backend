import React, { useState, useEffect } from 'react';
import { Grid, IconButton, Slider } from '@mui/material';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import PauseIcon from '@mui/icons-material/Pause';
import { Song } from './types';

type PlaybarProps = {
  song: Song;
  isPlaying: boolean;
  onPlayPause: () => void;
  onPrevSong: () => void;
  onNextSong: () => void;
};

const Playbar: React.FC<PlaybarProps> = ({ song, onPlayPause, onPrevSong, onNextSong }) => {
  const [audio, setAudio] = useState<HTMLAudioElement | null>(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const [duration, setDuration] = useState(0);
  const [currentTime, setCurrentTime] = useState(0);

  useEffect(() => setAudio(new Audio(song.mp3GCPLink as string)), [song]);

  useEffect(() => {
    if (audio) {
      audio.addEventListener('canplaythrough', () => {
        setDuration(audio.duration);
      });
      audio.addEventListener('timeupdate', () => {
        setCurrentTime(audio.currentTime);
      });
      audio.addEventListener('ended', () => {
        setIsPlaying(false);
      });
    }
  }, [audio]);

  const togglePlay = () => {
    if (!audio) return;

    if (isPlaying) {
      audio.pause();
    } else {
      audio.play();
    }

    setIsPlaying(!isPlaying);
  };

  const handleSeek = (_event: Event, value: number | number[]) => {
    if (!audio) return;

    if (Array.isArray(value)) {
      return;
    }

    audio.currentTime = value;
  };

  const formatTime = (seconds: number) => {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = Math.floor(seconds % 60);

    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
  };

  return (
    <Grid container alignItems="center">
      <Grid item>
        <IconButton onClick={togglePlay}>
          {isPlaying ? <PauseIcon /> : <PlayArrowIcon />}
        </IconButton>
      </Grid>
      <Grid item xs>
        <Slider
          value={currentTime}
          max={duration}
          onChange={handleSeek}
          aria-label="time slider"
        />
      </Grid>
      <Grid item>
        {duration > 0 && <div>{`${formatTime(currentTime)} / ${formatTime(duration)}`}</div>}
      </Grid>
    </Grid>
  );
};

export default Playbar;
