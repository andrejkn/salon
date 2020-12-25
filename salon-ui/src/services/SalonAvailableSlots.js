import { generateFromFetchObservable } from './Utils';

const retrieveAvailableSlots = (salonServiceId, formattedDate) =>
  generateFromFetchObservable(`/api/services/availableSlots/${salonServiceId}/${formattedDate}`);

export {
  retrieveAvailableSlots,
};
