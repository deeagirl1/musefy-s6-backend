import unittest

import pandas as pd

from services import RecommenderService


class RecommendationServiceTestCase(unittest.TestCase):
    def setUp(self):
        # Read the CSV file
        data = pd.read_csv("received_data/users.csv")

        # Extract the 'userId' column as a string variable
        user_id = str(data["user_id"].iloc[0])

        self.recommender = RecommenderService()
        self.recommender.load_data("received_data/songs.csv", "received_data/users.csv",
                                   "received_data/interactions.csv")

    def test_recommend_songs(self):

        # Read the CSV file
        data = pd.read_csv("received_data/users.csv")

        # Extract the 'user_id' column as a string variable
        user_id = str(data["user_id"].iloc[0])

        recommended_songs = self.recommender.recommend_songs(user_id, 2)

        self.assertIsInstance(recommended_songs, list)
        self.assertEqual(len(recommended_songs), 2)

    def test_invalid_user_id(self):
        user_id = "4"
        n = 5
        recommended_songs = self.recommender.recommend_songs(user_id, n)

        self.assertEqual(recommended_songs, [])


if __name__ == '__main__':
    unittest.main()
