import React from 'react';
import { TransitionGroup, CSSTransition } from 'react-transition-group';
import './LoadingPage.css';

const LoadingPage: React.FC = () => {
  return (
    <TransitionGroup>
      <CSSTransition classNames="loading" timeout={500}>
        <div className="loading-container">
          <h1>Loading</h1>
          <p>Please wait while the page is loading...</p>
        </div>
      </CSSTransition>
    </TransitionGroup>
  );
};

export default LoadingPage;
