# Ticket Reservation System

## Architecture

```plantuml
    class Client
    class springbootapp.ControllerLayer
    class springbootapp.ServiceLayer
    class springbootapp.RepositoryLayer
    class Database

    Client --> springbootapp.ControllerLayer : HTTP request
    Client <-- springbootapp.ControllerLayer : HTTP response
    springbootapp.ControllerLayer --> springbootapp.ServiceLayer
    springbootapp.ServiceLayer --> springbootapp.RepositoryLayer
    springbootapp.RepositoryLayer --> Database

```

### HTTP - HyperText Transfer Protocol

HTTP methods:
- GET
- POST
- PUT
- DELETE
- PATCH
- OPTIONS


// Request line
GET www.example.com/index.html?id=1&os=windows& HTTP/1.1
// Headers
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
Accept-Language: en-US,en;q=0.9
Cookie: _ga=GA1.2.123456789.1234567890; _gid=GA1.2.123456789.1234567890
// Body - optional, not available for GET
// empty
{
    "name": "John",
    "age": 30,
    "cars": [
        { "name":"Ford", "models":[ "Fiesta", "Focus", "Mustang" ] },
        { "name":"BMW", "models":[ "320", "X3", "X5" ] },
        { "name":"Fiat", "models":[ "500", "Panda" ] }
    ]
}

### REST API

// Tickets example

www.trs.com/api/tickets - GET - get all tickets
```
    GET /api/tickets HTTP/1.1
    Host: www.trs.com
    ... headers ...
```

www.trs.com/api/tickets - POST - create new ticket
```
    POST /api/tickets HTTP/1.1
    Host: www.trs.com
    ... headers ...

{
    movieId: 141,
    ticketPrice: 22.5,
    rowNo: 5,
    placeNo: 11
}
```

www.trs.com/api/tickets/{ticketId} - GET - get ticket with id = {ticketId}
```
    GET /api/tickets/123 HTTP/1.1
    Host: www.trs.com
    ... headers ...
```

www.trs.com/api/tickets/{ticketId} - PUT - update ticket with id = {ticketId}
```
    PUT /api/tickets/123 HTTP/1.1
    Host: www.trs.com
    ... headers ...

{
    movieId: 141,
    ticketPrice: 22.5,
    rowNo: 5,
    placeNo: 11
}
```

www.trs.com/api/tickets/{ticketId} - DELETE - delete ticket with id = {ticketId}
```
    DELETE /api/tickets/123 HTTP/1.1
    Host: www.trs.com
    ... headers ...
```

### Controller Layer
```java
// TicketController example
    @RestController
    @RequestMapping("/api")
    class TicketController {
	
        @Autowired
        private TicketService ticketService;    
	
        @GetMapping("/tickets")
        public List<Ticket> getAllTickets() {
            return ticketService.getAllTickets();
        }

        @GetMapping("/tickets/{id}")
        public Ticket getTicket(@PathVariable Long id) {
            return ticketService.getTicket(id);
        }

        @PostMapping("/tickets")
        public void addTicket(@RequestBody Ticket ticket) {
            ticketService.addTicket(ticket);
        }

        @PutMapping("/tickets/{id}")
        public void updateTicket(@RequestBody Ticket ticket, @PathVariable Long id) {
            ticketService.updateTicket(id, ticket);
        }

        @DeleteMapping("/tickets/{id}")
        public void deleteTicket(@PathVariable Long id) {
            ticketService.deleteTicket(id);
        }
    }

	class Ticket {
        private Long id; // null for new ticket
        private Long movieId;
        private Double ticketPrice;
        private Integer rowNo;
        private Integer placeNo;
    }
```

```plantuml
    class TicketController {
        - TicketService ticketService
        + getAllTickets()
        // SELECT * FROM tickets
        + getTicketById(Long id)
        // SELECT * FROM tickets WHERE id = id
        + getTicketsBySessionId(Long sessionId)
        // SELECT * FROM tickets WHERE sessionId = sessionId
        + getTicketsByMovieIdAndSessionDateTime(Long movieId, LocalDateTime sessionDateTime)
        // SELECT * FROM tickets WHERE movieId = movieId AND sessionDateTime = sessionDateTime
        + addTicket()
        + updateTicket()
        + deleteTicket()
    }
    
    class TicketService {
        - TicketRepository ticketRepository
        + getAllTickets()
        + getTicket()
        + addTicket()
        + updateTicket()
        + deleteTicket()
    }
    
    interface TicketRepository {
        + getAllTickets()
        + getTicketById()
        + addTicket()
        + updateTicket()
        + deleteTicket()
    }
    
    class Ticket {
        - Long id
        - Long movieId
        - Double ticketPrice
        - Integer rowNo
        - Integer placeNo
    }
    
    Client --> TicketController : HTTP request
    TicketController --> TicketService
    TicketService --> TicketRepository
    TicketRepository --> Database

```


1. List of possible use cases / user actions
  1.1. Admin use-cases
    - Add / edit / remove entity
  1.2. User use-cases
    - When user loads page, show list of movies
    - When user clicks on movie, show movie details and list of sessions
    - When user clicks on session, show list of available places
    - When user clicks on place, show ticket details
    
    - Additionally, when user is logged in, show list of his tickets

2. List of entities (Ticket, Movie, User, etc.)

```plantuml
    class Entity {
        Long id
    }

    class Cinema extends Entity {
        String name      // Planeta Kino
        String address   // 
        String phone     // +375 29 123 45 67
        String email     // planetakino@com.ua
        List<Hall> halls // hall1, hall2, hall3
    }
    
    class Hall extends Entity {
        String name        // hall1
        Cinema cinema      // cinemaId
        HallType type      // 2D, 3D, IMAX
        List<Place> places // 
    }
    
    class Session extends Entity {
        Movie movie      // movieId
        Hall hall        // hallId
        LocalDateTime dateTime
    }

    class Movie extends Entity {
        String title
        String description
        String genre
        String director
        String actors
        String duration
        String country
        String releaseDate
        String posterUrl
    }
    
    class Ticket extends Entity {
        User user         // userId
        Session session   // sessionId
        Place place       // placeId
        BigDecimal price  // calculated depending on place type and hall type
        LocalDateTime purchaseDate
    }
    
    class User extends Entity {
        String name
        String surname
        String email
        String phone
        String password
    }
    
    class Place extends Entity {
        Hall hall        // hallId
        String placeCode // 4-18
        PlaceType type   // VIP, normal, etc.
    }
    
    enum PlaceType {
        VIP (100),
        NORMAL (0),
        CHEAP (-50),
    }
    
    enum HallType {
        IMAX (200),
        3D (100),
        2D (0),
    }
```
CINEMA
 ID, CINEMA_NAME, CINEMA_ADDRESS, CINEMA_PHONE, CINEMA_EMAIL
------------------------------------------------------------
  1, PLANETA KINO, KIEV, +375 29 123 45 67,


HALL
 ID, CINEMA_ID, HALL_NAME
------------------------------------
  1,         1,     hall1
  2,         1,     hall2
  3,         1,     hall3


PLACE
 ID, HALL_ID, PLACE_CODE, PLACE_TYPE
------------------------------------
  1,       1,        1-1,        VIP
  2,       1,        1-2,        VIP
  3,       1,        1-3,        VIP


TICKET
 ID, USER_ID, SESSION_ID, PLACE_ID, TICKET_PRICE, TICKET_PURCHASE_DATE
----------------------------------------------------------------------
  1,       1,          1,        1,         22.5,  2019-01-01 12:00:00
  2,       2,          1,        2,         22.5,  2019-01-01 12:00:00
  3,       3,          1,        3,         22.5,  2019-01-01 12:00:00
