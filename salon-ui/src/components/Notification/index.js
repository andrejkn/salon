import React, { useState, useEffect } from 'react';

import { Alert } from 'react-bootstrap';

const Notification = ({ message, isFailure, isVisible, onClose }) => {
  const [isNotificationVisible, setIsNotificationVisible] = useState(true);

  useEffect(() => {
    // Hide Notification component after 5 seconds
    setTimeout(() => setIsNotificationVisible(false), 5000);
  });

  return (
    <Alert
      variant={ isFailure ? 'danger' : 'success' }
      dismissible
      show={isNotificationVisible}
      onClose={() => setIsNotificationVisible(false)}
    >
      {message}
    </Alert>
  );
};

export default Notification;
