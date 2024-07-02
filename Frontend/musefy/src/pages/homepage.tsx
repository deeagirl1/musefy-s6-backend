import React from 'react';
import { Link } from 'react-router-dom';
import styled from 'styled-components';

const HomeWrapper = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  position: relative;
`;

const Header = styled.h1`
  font-size: 4rem;
  margin-bottom: 3rem;
  text-align: center;
  color: white;
`;

const ButtonWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 2rem;
`;

const Button = styled(Link)`
  font-size: 1.6rem;
  background-color: #1db954;
  color: #fff;
  padding: 1rem 2rem;
  border-radius: 2rem;
  margin: 0 1rem;
  transition: all 0.3s ease;
  text-decoration: none; /* Add this line to remove the underline */

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  }
`;

const Background = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
`;

const VideoBackground = styled.video`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: -1;
`;

const Home: React.FC = () => {
  return (
    <HomeWrapper>
      <VideoBackground autoPlay muted loop>
      <source src="https://storage.googleapis.com/musefys6-songs-bucket/videos/Club_Scene_Blur.mp4" type="video/mp4" />
        Your browser does not support the video tag.
      </VideoBackground>
      <Header>Discover new music with Musefy</Header>
      <ButtonWrapper>
        <Button to="/browse">Browse</Button>
        <Button to="/login">Login</Button>
      </ButtonWrapper>
    </HomeWrapper>
  );
};

export default Home;



