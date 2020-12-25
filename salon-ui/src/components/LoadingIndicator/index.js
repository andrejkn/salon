import { Spinner } from 'react-bootstrap';

import './styles.css';

const LoadingIndicator = () => {
  return (
    <>
      <Spinner
        className="LoadingIndicator-spinner"
        animation="border"
        role="status"
      />
    </>
  );
};

export default LoadingIndicator;
