import logging

import pika
from pika import ConnectionParameters


class RabbitMQClient:
    def __init__(self):
        self.connection = None
        self.channel = None

    def connect(self):
        # rabbitmq_host = "localhost"
        # rabbitmq_port = 5672
        # rabbitmq_username = "guest"
        # rabbitmq_password = "guest"
        rabbitmq_host = "rabbitmq-service.default.svc.cluster.local"
        rabbitmq_port = 5672
        rabbitmq_username = "musefy_rabbitmq"
        rabbitmq_password = "musefy_rabbitmq_12345_6789"

        credentials = pika.PlainCredentials(rabbitmq_username, rabbitmq_password)
        connection_params = pika.ConnectionParameters(host=rabbitmq_host, port=rabbitmq_port, credentials=credentials)
        self.connection = pika.BlockingConnection(connection_params)
        self.channel = self.connection.channel()

    def disconnect(self):
        if self.connection and not self.connection.is_closed:
            self.connection.close()

    def declare_exchange(self, exchange_name, exchange_type, durable=True):
        self.channel.exchange_declare(
            exchange=exchange_name,
            exchange_type=exchange_type,
            durable=durable,
        )

    def declare_queue(self, queue_name):
        self.channel.queue_declare(queue=queue_name, durable=True)

    def bind_queue(self, queue_name, exchange_name, routing_key):
        self.channel.queue_bind(
            queue=queue_name,
            exchange=exchange_name,
            routing_key=routing_key
        )

    def publish_message(self, exchange_name, routing_key, message):
        self.channel.basic_publish(
            exchange=exchange_name,
            routing_key=routing_key,
            body=message,
            properties=pika.BasicProperties(content_type="application/json")
        )


async def create_queue():
    exchange_name = "user.recommendations.exchange"
    exchange_type = "topic"
    queue_name = "user.recommendations.queue"
    routing_key = "user.recommendations.routingkey"

    client = RabbitMQClient()
    client.connect()

    client.declare_exchange(exchange_name, exchange_type, durable=True)
    client.declare_queue(queue_name)
    client.bind_queue(queue_name, exchange_name, routing_key)

    client.disconnect()

    logging.info("Queue created successfully")