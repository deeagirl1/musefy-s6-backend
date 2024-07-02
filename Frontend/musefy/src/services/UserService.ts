import axios from 'axios';
import { User, UserFavouriteSongs, UserInteractionCount } from '../components/types';
import { getToken } from './AuthService';

const API_URL = "http://localhost:8085/api/users";

const userService = {
  getUsers: async (): Promise<User[]> => {
    try {
      const response = await axios.get(API_URL);
      return response.data;
    } catch (error) {
      console.error(error);
      return [];
    }
  },
  getUserById: async (id: number): Promise<User | null> => {
    try {
      const response = await axios.get(`${API_URL}/${id}`);
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  },

  deleteUser: async (id: number): Promise<boolean> => {
    try {
      const token = getToken();
  
      const config = {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      };
  
      await axios.delete(`${API_URL}/${id}`, config);
      return true;
    } catch (error) {
      console.error(error);
      return false;
    }
  },

  addSongToFavouriteList: async (userFavoriteSongDTO: UserFavouriteSongs): Promise<User | null> => {
    try {
      const token = getToken();
  
      const config = {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      };
  
      const response = await axios.post(`${API_URL}/add-song-to-favourite-list`, userFavoriteSongDTO, config);
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  },

  getUserFavoriteSongs: async (userId: string): Promise<string[] | null> => {
    try {
      const token = getToken();
  
      const config = {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      };
  
      const response = await axios.get(`${API_URL}/${userId}/favoritesongs`, config);
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  },
  
  handleSongListenedEvent: async (userInteractionDTO: UserInteractionCount): Promise<string | null> => {
    try {
      const token = getToken();
  
      const config = {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      };
  
      const response = await axios.post(`${API_URL}/song-listened`, userInteractionDTO, config);
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  }
};

export default userService;
