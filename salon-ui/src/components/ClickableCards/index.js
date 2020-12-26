import Card from 'react-bootstrap/Card';
import CardGroup from 'react-bootstrap/CardGroup'
import Button from 'react-bootstrap/Button';

import './styles.css'

const ClickableCards = ({ cards }) => (
  <CardGroup>
    {
      cards.map(({ header, title, content, altContent, onClickHandler, buttonTitle }, index) => (
        <div key={`clickable-card_${index}`}>
          <Card className="ClickableCards-card">
            <Card.Header>
              <h4>{header}</h4>
            </Card.Header>
            <Card.Body>
            <Card.Title>
              <h2>{title}</h2>
            </Card.Title>
            <Card.Text>
              {content}
              <br />
              {altContent}
            </Card.Text>
            <Button
              className="ClickableCards-button"
              variant="outline-primary"
              onClick={onClickHandler}
            >
              {buttonTitle}
            </Button>
            </Card.Body>
          </Card>
        </div>
      ))
    }
  </CardGroup>
);

export default ClickableCards;
