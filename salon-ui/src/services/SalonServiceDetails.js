import { of } from 'rxjs';
import { fromFetch } from 'rxjs/fetch';
import { switchMap, catchError } from 'rxjs/operators';

const retrieveServiceDetails = () => {
  return fromFetch('http://localhost:8080/salon/salonServiceDetails').pipe(
    switchMap((response) => {
      if (response.ok) {
        return response.json();
      } else {
        return of({
          error: true,
          message: `Error: ${response.status}`,
        });
      }
    }),
    catchError((error) => of({
      error: true,
      message: error.message,
    }))
  );
};

export {
  retrieveServiceDetails,
};