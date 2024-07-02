import pandas as pd
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from models.song import Song
from models.user import User


class RecommenderService:
    def __init__(self):
        self.song_similarity_matrix = None
        self.songs = None
        self.users = None
        self.interactions = None

    def load_data(self, songs_file: str, users_file: str, interactions_file: str):
        # Load song data
        songs_df = pd.read_csv(songs_file)
        songs_df['duration'] = songs_df['duration'].astype(int)
        songs_df['tempo'] = songs_df['tempo'].astype(int)
        self.songs = [Song(**song) for song in songs_df.to_dict(orient="records")]

        # Load user data
        users_df = pd.read_csv(users_file)
        self.users = [User(**user) for user in users_df.to_dict(orient="records")]

        # Load interaction data
        self.interactions = pd.read_csv(interactions_file)

        # Calculate song similarity matrix based on song features
        song_features = pd.concat([
            songs_df[['duration', 'tempo']],
            pd.get_dummies(songs_df['genre'], prefix='genre'),
            pd.get_dummies(songs_df['key'], prefix='key')
        ], axis=1)
        self.song_similarity_matrix = cosine_similarity(song_features)

    def is_data_loaded(self):
        return self.song_similarity_matrix is not None and self.songs is not None

    def get_user_ids(self):
        if self.users is None:
            raise Exception("Data has not been loaded. Call load_data() first.")
        return [user.user_id for user in self.users]

    def recommend_songs(self, user_id: str, n: int = 5) -> list[str]:
        if not self.is_data_loaded():
            raise Exception("Data has not been loaded. Call load_data() first.")

        user_interactions = self.interactions[self.interactions['user_id'] == user_id]

        if user_interactions.empty:
            return []

        known_song_ids = user_interactions['song_id'].values
        known_song_ids = [str(song_id) for song_id in known_song_ids]

        known_song_idx = [idx for idx, song in enumerate(self.songs) if str(song.song_id) in known_song_ids]

        scores = np.sum(self.song_similarity_matrix[known_song_idx], axis=0)
        top_song_ids = np.argsort(-scores)[:n]
        recommended_song_ids = [str(self.songs[idx].song_id) for idx in top_song_ids]
        return recommended_song_ids
