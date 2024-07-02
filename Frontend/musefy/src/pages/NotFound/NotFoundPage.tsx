import React from 'react';
import { TransitionGroup, CSSTransition } from 'react-transition-group';
import './NotFound.css';


const NotFoundPage: React.FC = () => {
  return (
    <TransitionGroup>
      <CSSTransition classNames="notFound" timeout={500}>
        <div className="notFound-container">
          <h1>Page Not Found</h1>
          <p>The requested page does not exist.</p>
        </div>
      </CSSTransition>
    </TransitionGroup>
  );
};

export default NotFoundPage;
