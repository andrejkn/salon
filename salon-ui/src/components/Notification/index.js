import { Alert } from 'react-bootstrap';

const Notification = ({ message, isFailure, handleOnClose }) => {
  return (
    <Alert
      variant={ isFailure ? 'danger' : 'success' }
      dismissible
      onClose={handleOnClose}
      transition={false}
    >
      {message}
    </Alert>
  );
};

export default Notification;
