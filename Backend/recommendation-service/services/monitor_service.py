import os
import asyncio
import threading
import time

from routes.recommendations import recommend_songs
from services.recommendation_service import RecommenderService

recommendation_service = RecommenderService()
loop = asyncio.new_event_loop()


async def check_for_new_data():
    # Check if data files exist
    songs_file = "received_data/songs.csv"
    users_file = "received_data/users.csv"
    interactions_file = "received_data/interactions.csv"

    files = [songs_file, users_file, interactions_file]
    for file in files:
        if os.path.exists(file):
            # Get the modification time of the file
            mod_time = os.path.getmtime(file)

            # Get the current time
            current_time = time.time()

            # Check if the file was modified within the last 24 hours
            if current_time - mod_time <= 24 * 60 * 60:  # 24 hours in seconds
                # Load the updated data into the recommendation service
                recommendation_service.load_data(songs_file, users_file, interactions_file)

                # Trigger recommendation generation
                await recommend_songs()
                break  # Once the recommendation is generated, no need to check other files.


async def monitor_data_folder():
    while True:
        await check_for_new_data()
        await asyncio.sleep(1)


def start_monitoring():
    # Start monitoring the data folder in a separate thread
    monitor_thread = threading.Thread(target=monitor_data_folder)
    monitor_thread.start()

