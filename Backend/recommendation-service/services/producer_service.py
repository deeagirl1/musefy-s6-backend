import json

from settings.rabbitmq_client import RabbitMQClient


async def send_recommendations(user_id: str, recommended_songs_ids: list[str]):
    exchange_name = "user.recommendations.exchange"
    routing_key = "user.recommendations.routingkey"

    client = RabbitMQClient()
    client.connect()

    recommendation = {
        "userId": user_id,
        "recommendedSongs": recommended_songs_ids
    }
    message_body = json.dumps(recommendation)

    client.publish_message(exchange_name, routing_key, message_body)

    client.disconnect()

    print(f"Recommendations sent for user {user_id}")
    print("Recommendation Message:", message_body)