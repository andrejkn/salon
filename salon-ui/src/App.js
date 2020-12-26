import React, { useState, useEffect } from 'react';

import {
  BrowserRouter as Router,
  Route
} from 'react-router-dom';

import Navbar from 'react-bootstrap/Navbar';

import { isNil } from 'lodash';

import LoadingIndicator from './components/LoadingIndicator';
import Notification from './components/Notification';
import ChooseService from './components/ChooseService';
import ChooseSlot from './components/ChooseSlot';

import { retrieveServiceDetails } from './services/SalonServiceDetails';
import { retrieveAvailableSlots } from './services/SalonAvailableSlots';

import './App.css';

const App = () => {
  const [isDataLoaded, setIsDataLoadedState] = useState(false);
  const [serviceDetails, setServiceDetailsState] = useState(null);
  const [availableSlots, setAvailableSlotsState] = useState(null);
  const [availableSlotsDate, setAvailableSlotsDate] = useState(null);
  const [notificationMessage, setNotificationMessage] = useState(null);
  const [hasError, setHasErrorState] = useState(false);

  const loadAvailableSlots = (serviceId) => {
    setIsDataLoadedState(false);
    retrieveAvailableSlots(serviceId, availableSlotsDate).subscribe({
      next: (data) => {
        if (data.error) {
          setNotificationMessage(`${data.message}. Failed to load available slots!`);
          setHasErrorState(true);
          setAvailableSlotsState(null);
        } else {
          setAvailableSlotsState(data);
          setNotificationMessage(null)
        }
      },
      complete: () => setIsDataLoadedState(true),
    });
  }

  useEffect(() => {
    const subscription = retrieveServiceDetails().subscribe({
      next: (data) => {
        if (data.error) {
          setNotificationMessage(data.message);
          setHasErrorState(true);
        } else {
          setServiceDetailsState(data);
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
        {
          notificationMessage ? (
            <Notification
              isFailure={hasError}
              message={notificationMessage}
              handleOnClose={() => setNotificationMessage(null)}
            />
          ) : null
        }
        {
          !isDataLoaded ? (
            <LoadingIndicator />
          ) : null
        }
        <div className="App-container">
          <Router>
            <Route
              exact
              path="/"
            >
              {
                !isNil(serviceDetails) ? (
                  <ChooseService serviceDetails={serviceDetails} />
                ) : null
              }
            </Route>
            <Route
              path="/chooseSlot/:serviceId/:serviceName"
            >
              <ChooseSlot
                availableSlots={availableSlots}
                handleButtonClick={loadAvailableSlots}
                handleDateChange={setAvailableSlotsDate}
              />
            </Route>
          </Router>
        </div>
      </>
    </div>
  );
}

export default App;
