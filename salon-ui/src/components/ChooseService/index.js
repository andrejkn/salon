import Card from 'react-bootstrap/Card';
import CardColumns from 'react-bootstrap/CardColumns'
import Button from 'react-bootstrap/Button';

import './styles.css'

const ChooseService = ({ availableSlots }) => {
  return (
    <CardColumns>
      {
        availableSlots?.map((slot) => (
          <div key={`slot_${slot.id}`}>
            <Card className="ChooseService-card">
              <Card.Header>
                <h4>{slot.name}</h4>
              </Card.Header>
              <Card.Body>
                <Card.Title>
                  <h2>${slot.price}</h2>
                </Card.Title>
                <Card.Text>
                  {slot.description}
                  <br />
                  {slot.timeInMinutes} Minutes
                </Card.Text>
                <Button
                  className="ChooseService-book-now-button"
                  variant="outline-primary"
                >
                  Book Now
                </Button>
              </Card.Body>
            </Card>
          </div>
        ))
      }
    </CardColumns>
  );
};

export default ChooseService;
