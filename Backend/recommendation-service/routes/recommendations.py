from fastapi import APIRouter

from services.producer_service import send_recommendations
from services.recommendation_service import RecommenderService

router = APIRouter()
recommendation_service = RecommenderService()

# Load the data into the recommendation service
recommendation_service.load_data("received_data/songs.csv", "received_data/users.csv", "received_data/interactions.csv")


@router.get("/recommend_songs")
async def recommend_songs(n: int = 5):
    user_ids = [user.user_id for user in (recommendation_service.users or [])]

    # Generate recommendations for each user
    for user_id in user_ids:
        recommended_song_ids = recommendation_service.recommend_songs(user_id, n)
        print(user_id)
        # Send the recommendation message for each user
        await send_recommendations(user_id, recommended_song_ids)

        return {"message": f"Recommendations sent for user {user_id}"}

    # Return a success message
    return {"message": "Recommendations sent"}
