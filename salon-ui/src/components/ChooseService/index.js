import { useHistory } from 'react-router-dom';

import ClickableCards from '../ClickableCards';

const ChooseService = ({ serviceDetails }) => {
  const history = useHistory();

  return (
    <ClickableCards
      cards={
        serviceDetails?.map((service) => ({
          header: service.name,
          title: `$${service.price}`,
          content: service.description,
          altContent: `${service.timeInMinutes} Minutes`,
          onClickHandler: () => history.push(`/chooseSlot/${service.id}/${service.name}`),
          buttonTitle: 'Book Now',
        }))
      }
    />
  );
};

export default ChooseService;
