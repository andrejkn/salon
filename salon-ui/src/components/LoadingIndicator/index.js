import { Spinner } from 'react-bootstrap';

import './styles.css';

const LoadingIndicator = () => {
  return (
    <div>
      <Spinner
        className="Spinner"
        animation="border"
        role="status"
      />
    </div>
  );
};

export default LoadingIndicator;