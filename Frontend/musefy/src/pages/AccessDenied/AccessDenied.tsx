import React from 'react';
import { TransitionGroup, CSSTransition } from 'react-transition-group';
import './accessDenied.css'; // Add your custom CSS for the animation

const AccessDeniedPage: React.FC = () => {
  return (
    <TransitionGroup>
      <CSSTransition classNames="accessDenied" timeout={500}>
        <div className="accessDenied-container">
          <h1>Access Denied</h1>
          <p>Sorry, you don't have permission to access this page.</p>
        </div>
      </CSSTransition>
    </TransitionGroup>
  );
};

export default AccessDeniedPage;
