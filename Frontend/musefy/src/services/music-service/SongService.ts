import axios from 'axios';
import { Song } from '../../components/types';

const API_URL = "http://localhost:8087/api/songs";

const songService = {
    getSongs: async (): Promise<Song[]> => {
      try {
        const response = await axios.get(API_URL);
        return response.data;
      } catch (error) {
        console.error(error);
        return [];
      }
    },
    getSongById: async (id: string): Promise<Song | null> => {
      try {
        const response = await axios.get(`${API_URL}/${id}`);
        return response.data;
      } catch (error) {
        console.error(error);
        return null;
      }
    },
    createPlaylist: async (song: Song): Promise<Song | null> => {
      try {
        const response = await axios.post(API_URL, song);
        return response.data;
      } catch (error) {
        console.error(error);
        return null;
      }
    },
    updatePlaylist: async (id: number, song: Song): Promise<Song | null> => {
      try {
        const response = await axios.put(`${API_URL}/${id}`, song);
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
  
  export default songService;