import axios, { AxiosResponse } from 'axios';
import { Album } from '../../components/types';

const API_URL = "http://localhost:8085/api/albums";

abstract class AlbumService {
  static async getAlbums(): Promise<Album[]> {
    try {
      const response: AxiosResponse<Album[]> = await axios.get(API_URL);
      return response.data;
    } catch (error) {
      console.error(error);
      return [];
    }
  }

  static async getAlbumById(id: number): Promise<Album | null> {
    try {
      const response: AxiosResponse<Album> = await axios.get(`${API_URL}/${id}`);
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  static async createAlbum(album: Album): Promise<Album | null> {
    try {
      const response: AxiosResponse<Album> = await axios.post(`${API_URL}/create`, album);
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  static async updateAlbum(id: number, album: Album): Promise<Album | null> {
    try {
      const response: AxiosResponse<Album> = await axios.put(`${API_URL}/${id}`, album);
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  static async deleteAlbum(id: number): Promise<boolean> {
    try {
      await axios.delete(`${API_URL}/${id}`);
      return true;
    } catch (error) {
      console.error(error);
      return false;
    }
  }
}

export default AlbumService;
