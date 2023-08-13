# NOTE
I created this file to collect my considerations on this project.

## Test the application
I added a `makefile` to make it easier to execute the main commands.

To start the main application
```shell
make
# or
make run
```

To start the dependencies
```shell
make dependencies
```

Examples of GRPC request
```shell
grpcurl -d '{"confirmation_number" : "7Y3G-TC2V"}' \
  -plaintext 127.0.0.1:9000 shackle.reservation.matcher.MatcherService.matchReservation
  # OR
grpcurl -d '{"guest_details": {"last_name": "Smith"}, "arrival_date": {"year": 2023, "month": 8, "day": 15}, "departure_date": {"year": 2023, "month": 8, "day": 16}}' \
  -plaintext 127.0.0.1:9000 shackle.reservation.matcher.MatcherService.matchReservation
```

## Considerations

### Time investment
- I've never used Quarkus, so I had to spend some time exploring and investigating
- I've never set up GRPC from scratch, so again I had to spend some time there (I also had some issues with VSC at the beginning, so I moved to IntelliJ)

### Design and possible improvements
- As data storage I could have used an in-memory database, but I opted for an HashMap
- I'm not sure if the confirmation codes are unique (between online, agent and direct booking), so I opted to use a map of lists
- I created an internal model to store the information and provide an absraction from the model offered by the GRPC
- I've unified the confirmation codes `booking_confirmation_number`, `web_confirmation_code`, `travel_agent_confirmation_code` as one generic `confirmation_code` to make it transparent for a user. This is reflected in the API and in the internal model
- I've created different classes for each type of booking (online, agent and direct). This is probably too much for this use case, but it can help in case we want different matching logic for each of the booking types
- A service is responsible to load the reservation when the application is started. The logic is quite simple and it doesn't handle reconnections or errors
- Matching logic is defined in the abstract class BookingEntry, but probably it could be extracted (composition over inheritance)
- Matching logic is simple and probably it could be improved simplifying the if/else statements (for example using predicates)
  - First of all it uses the confirmation code to find an exact match (if confirmation codes are not unique there is a chance that more than one reservation is returned)
  - If the code doesn't match, it loops against all the entries (this is not optimal, it would be more efficient in a database using indexes for the other values)
  - Then it tries to match the email, phone number or last name
  - If it matches something and arraival and departure dates are provided, it will try to match them with the previous record
  - The outcome of the match can be an unique result, a list of reservations or an empty list
- Response is quite simple, probably I could have ended up with something better
  - With an exact match, it returns the reservation (simplified, all the internal values are removed)
  - With multiple match it ask for more information (it is just a message, but it could be nice to add a custom code for an easier handling)
  - Whit not match, a not found response is returned

### General improvements
- I've written some unit tests to cover most of the happy paths. It would be nice to extends the tests and to introduce integration tests
- The requirements about what information the user can provide to retrieve a reservation are quite open. Giving the user too much freedom of what to pass is not always a goo idea because it can create confusion, it increases the matching logic complexity making it error-prone
- To reduce an automated attacks, it can be nice to have a rate limit logic and suspend queries for time T after X failed attempts