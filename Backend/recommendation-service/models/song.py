from pydantic import BaseModel


class Song(BaseModel):
    song_id: str
    title: str
    genre: str
    duration: float
    tempo: int
    key: str
