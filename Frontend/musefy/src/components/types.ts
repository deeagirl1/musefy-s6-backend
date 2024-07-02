// Album type
export type Album = {
  albumId: number;
  title: string;
  artist: string;
  songs: Song[];
  // image: string;
};

// Genre type
export type Genre =  {
  id: number;
  name: string;
  image: string;
};

// Playlists type
export type Playlist = {
  id: number;
  title: string;
  description: string;
  created_by: string;
  image: string;
  songs: Album[];
};

// Songs type
export type Song = {
  songId: string;
  title: string;
  artist: string;
  album: Album;
  release_date : String;
  duration: number;
  mp3GCPLink: string
  imageLink: string;
};

export type User = {
  id: string;
  name: string;
  // Add other user properties here
}

export type UserFavouriteSongs = {
  userId: string;
  songId: string;
  // Add other user properties here
}

export type UserRecommendedSongs = {
  userId: string;
  recommendedSongs: Song[];
}

export type UserInteractionCount = {
  userId: string;
  songId: string;
}



