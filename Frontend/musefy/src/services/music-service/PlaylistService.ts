import axios from 'axios';
import { Playlist } from '../../components/types';

const API_URL = "http://localhost:8085/api/playlists";

const playlistService = {
  getPlaylists: async (): Promise<Playlist[]> => {
    try {
      const response = await axios.get(API_URL);
      return response.data;
    } catch (error) {
      console.error(error);
      return [];
    }
  },
  getPlaylistById: async (id: number): Promise<Playlist | null> => {
    try {
      const response = await axios.get(`${API_URL}/${id}`);
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  },
  createPlaylist: async (playlist: Playlist): Promise<Playlist | null> => {
    try {
      const response = await axios.post(API_URL, playlist);
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  },
  updatePlaylist: async (id: number, playlist: Playlist): Promise<Playlist | null> => {
    try {
      const response = await axios.put(`${API_URL}/${id}`, playlist);
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  },
  deletePlaylist: async (id: number): Promise<boolean> => {
    try {
      await axios.delete(`${API_URL}/${id}`);
      return true;
    } catch (error) {
      console.error(error);
      return false;
    }
  }
};

export default playlistService;
