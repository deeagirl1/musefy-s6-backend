import React, { useState, useEffect, useRef } from "react";
import { Box, IconButton, Slider, Typography, Stack } from "@mui/material";
import {
  SkipPrevious,
  PlayArrow,
  Pause,
  SkipNext,
  VolumeDown,
  VolumeUp,
} from "@mui/icons-material";
import { Song, UserInteractionCount } from "../types";
import userService from "../../services/UserService";
import { getToken, getDecodedToken } from "../../services/AuthService";

interface PlaybarProps {
  song: Song;
  isPlaying: boolean;
  volume: number;
  onPlayPause: () => void;
  onVolumeChange: (event: React.SyntheticEvent | Event, newValue: number | number[]) => void;
  onSkipPrevious: () => void;
  onSkipNext: () => void;
}

const Playbar: React.FC<PlaybarProps> = ({
  song,
  isPlaying,
  volume,
  onPlayPause,
  onVolumeChange,
  onSkipPrevious,
  onSkipNext,
}) => {
  const audioRef = useRef<HTMLAudioElement>(null);
  const [currentTime, setCurrentTime] = useState(0);
  const [duration, setDuration] = useState(0);

  useEffect(() => {
    const decodedToken = getDecodedToken() as { userId: string };
  
    if (audioRef.current) {
      if (isPlaying) {
        audioRef.current.play();
  
        if (decodedToken) {
          const userId = decodedToken.userId;
          const userInteractionDTO: UserInteractionCount = {
            userId,
            songId: song.songId,
          };
          userService.handleSongListenedEvent(userInteractionDTO);
        }
      } else {
        audioRef.current.pause();
      }
    }
  }, [isPlaying]);
  useEffect(() => {
    if (audioRef.current) {
      audioRef.current.volume = volume / 100;
    }
  }, [volume]);

  const handleTimeUpdate = () => {
    if (audioRef.current) {
      setCurrentTime(audioRef.current.currentTime);
    }
  };

  const handleDurationChange = () => {
    if (audioRef.current) {
      setDuration(audioRef.current.duration);
    }
  };

  const handleProgressChange = (
    event: React.SyntheticEvent | Event,
    newValue: number | number[]
  ) => {
    if (typeof newValue === "number") {
      if (audioRef.current) {
        audioRef.current.currentTime = newValue;
        setCurrentTime(newValue);
      }
    }
  };

  const formatTime = (time: number) => {
    const minutes = Math.floor(time / 60);
    const seconds = Math.floor(time % 60);

    return `${minutes.toString().padStart(2, "0")}:${seconds
      .toString()
      .padStart(2, "0")}`;
  };

  return (
    <Box
      sx={{
        position: "fixed",
        bottom: 0,
        left: 0,
        width: "100%",
        backgroundColor: "#2B2929",
        zIndex: 1000,
        paddingBottom: "env(safe-area-inset-bottom)", // Handle safe area for iOS devices
        overflowX: "auto",
        overflowY: "hidden",
        whiteSpace: "nowrap",
        WebkitOverflowScrolling: "touch",
      }}
    >
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          padding: "1rem",
        }}
      >
        {song && (
          <audio
            src={song.mp3GCPLink}
            ref={audioRef}
            onEnded={onPlayPause}
            onTimeUpdate={handleTimeUpdate}
            onLoadedMetadata={handleDurationChange}
          ></audio>
        )}
        <Stack
          direction="row"
          alignItems="center"
          spacing={1}
          sx={{ color: "#929292" }}
        >
          <IconButton onClick={onSkipPrevious} sx={{ color: "inherit" }}>
            <SkipPrevious />
          </IconButton>
          <IconButton onClick={onPlayPause} sx={{ color: "inherit" }}>
            {isPlaying ? <Pause /> : <PlayArrow />}
          </IconButton>
          <IconButton onClick={onSkipNext} sx={{ color: "inherit" }}>
            <SkipNext />
          </IconButton>
          <Typography sx={{ mr: "1rem" }}>{formatTime(currentTime)}</Typography>
          <Slider
            value={currentTime}
            min={0}
            max={duration}
            step={0.01}
            onChange={handleProgressChange}
            onChangeCommitted={handleProgressChange}
            aria-labelledby="playback-slider"
            sx={{ ml: "10px", mr: "10px", color: "inherit", width: 500 }}
          />
          <Typography sx={{ ml: "1rem" }}>
            {formatTime(duration - currentTime)}
          </Typography>
          <IconButton onClick={() => {}} sx={{ color: "inherit" }}>
            {/* Add icon for favorite */}
          </IconButton>
          <IconButton onClick={() => {}} sx={{ color: "inherit" }}>
            {/* Add icon for shuffle */}
          </IconButton>
          <IconButton onClick={() => {}} sx={{ color: "inherit" }}>
            {/* Add icon for repeat */}
          </IconButton>
          <IconButton sx={{ color: "inherit" }}>
            {/* Add icon for queue */}
          </IconButton>
          <Box sx={{ width: 200, mt: "100px" }}>
            <Stack spacing={2} direction="row" alignItems="center">
              <VolumeDown />
              <Slider
                aria-label="Volume slider"
                value={volume}
                min={0}
                max={100}
                step={1}
                onChange={onVolumeChange}
                sx={{ color: "#4745A0" }}
                aria-labelledby="volume-slider"
              />
              <VolumeUp />
            </Stack>
          </Box>
          <Stack direction="row" alignItems="center" spacing={1}>
            {/* Add song image */}
            <Box>
              {/* Add artist name and song title */}
            </Box>
          </Stack>
        </Stack>
      </Box>
    </Box>
  );
};

export default Playbar;
