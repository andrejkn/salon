import React, { useState, useEffect } from 'react';

import Navbar from 'react-bootstrap/Navbar';

import { isNil } from 'lodash';

import LoadingIndicator from './components/LoadingIndicator';
import Notification from './components/Notification';
import ChooseService from './components/ChooseService';

import { retrieveServiceDetails } from './services/SalonServiceDetails';

import './App.css';

function App() {
  const [isDataLoaded, setIsDataLoadedState] = useState(false);
  const [availableSlots, setAvailableSlotsState] = useState(null);
  const [notificationMessage, setNotificationMessage] = useState(null);
  const [hasError, setHasErrorState] = useState(false);

  useEffect(() => {
    const subscription = retrieveServiceDetails().subscribe({
      next: (data) => {
        if (data.error) {
          setNotificationMessage(data.message);
          setHasErrorState(true);
        } else {
          setNotificationMessage('Data Loaded Successfully!');
          setAvailableSlotsState(data);
        }
      },
      complete: () => setIsDataLoadedState(true),
    });

    return () => subscription.unsubscribe();
  }, []);

  return (
    <div className="App">
      <>
        <Navbar bg="dark" variant="dark" expand="lg">
          <Navbar.Brand>
            AR Salon and Day Spa Services
          </Navbar.Brand>
        </Navbar>
        <Notification
          isFailure={hasError}
          message={notificationMessage}
        />
        {
          !isDataLoaded ? (
            <LoadingIndicator />
          ) : null
        }
        <div className="App-container">
          {
            !isNil(availableSlots) ? (
              <ChooseService availableSlots={availableSlots} />
            ) : null
          }
        </div>
      </>
    </div>
  );
}

export default App;
