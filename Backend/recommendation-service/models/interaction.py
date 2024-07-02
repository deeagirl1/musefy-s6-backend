from pydantic import BaseModel
class Interaction(BaseModel):
    user_id: str
    song_id: str
    listen_count: int
