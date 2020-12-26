import { generateFromFetchObservable } from './Utils';

const retrieveServiceDetails = () =>
  generateFromFetchObservable('/api/services/salonServiceDetails');

export {
  retrieveServiceDetails,
};
