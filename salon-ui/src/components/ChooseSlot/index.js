import React, { useEffect } from 'react';

import { useParams } from 'react-router-dom';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';

import moment from 'moment';

import ClickableCards from '../ClickableCards';

import './styles.css';

const ChooseSlot = ({
    formattedDate,
    availableSlots,
    handleButtonClick,
    handleDateChange,
    reset
}) => {
  const { serviceId, serviceName } = useParams();

  return (
    <>
      <Form className="ChooseSlot-form">
        <Form.Group bsPrefix="ChooseSlot-date-picker">
          <Form.Label>
            Choose date for <b>{serviceName}</b>
          </Form.Label>
          <Form.Control
            type="date"
            placeholder=""
            onChange={({ target }) => handleDateChange(target.value)}
          />
        </Form.Group>

        <Button
          className="ChooseSlot-button"
          onClick={() => handleButtonClick(serviceId, formattedDate)}
        >
          Show Slots
        </Button>
      </Form>
      {
        availableSlots ? (
          <ClickableCards
            cards={availableSlots
              .map((slot) => ({
                header: serviceName,
                title: slot.stylistName,
                content: `Slot Time ${moment(slot.slotFor).format('h:ss a')}`,
                buttonTitle: 'Book This Slot'
              }))
            }
          />
        ) : null
      }
    </>
  );
};

export default ChooseSlot;
