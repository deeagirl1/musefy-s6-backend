import React from 'react';
import { Genre } from './types';

type GenreSectionProps = {
  genres: Genre[];
};

const GenreSection: React.FC<GenreSectionProps> = ({ genres }) => {
  return (
    <section>
      <h2>Genres</h2>
        {genres.map((genre) => (
          <li key={genre.id}>
            <img src={genre.image} alt={genre.name} />
            <span>{genre.name}</span>
          </li>
        ))}
    </section>
  );
};

export default GenreSection;
