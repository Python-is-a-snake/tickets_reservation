package db.migration;

import com.trs.tickets.model.HallType;
import com.trs.tickets.model.PlaceType;
import com.trs.tickets.model.dto.*;
import db.MySqlParameterSource;
import lombok.SneakyThrows;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class V2__data extends BaseJavaMigration {

    private static final String INSERT_CINEMA = "INSERT INTO cinema (name, address, phone, email) VALUES (:name, :address, :phone, :email)";
    private static final String INSERT_HALL = "INSERT INTO hall (name, type, cinema_id) VALUES (:name, :type, :cinemaId)";
    private static final String INSERT_PLACE = "INSERT INTO place (number, place_type, row, session_id) VALUES (:number, :placeType, :row, :sessionId)";
    private static final String INSERT_MOVIE = "INSERT INTO movie (actors, country, description, director, duration, genre, poster_url, release_date, title, trailer_url) " +
                                               "values (:actors, :country, :description, :director, :duration, :genre, :posterUrl, :releaseDate, :title, :trailerUrl)";
    private static final String INSERT_SESSION = "INSERT INTO session (movie_id, hall_id, session_date_time) VALUES (:movieId, :hallId, :sessionDateTime)";

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    @SneakyThrows
    public void migrate(Context context) {
        jdbcTemplate = new NamedParameterJdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));

        long cinemaId = addCinema(new CinemaDto("Kyiv Cinema Hall", "Kyiv, Avenue, 12", "(098) 765-43-21", "hall1@kyivcinema.com", List.of()));

        long hallId = addHall(new HallDto(cinemaId, "2D Hall", HallType.HALL_2D));
        addHall(new HallDto(cinemaId, "3D Hall", HallType.HALL_3D));
        addHall(new HallDto(cinemaId, "IMAX Hall", HallType.HALL_IMAX));
        addMovies();
        addSession(new SessionDto(1L, hallId, LocalDateTime.now().plusDays(10), List.of()));
    }

    private long addCinema(CinemaDto cinemaDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_CINEMA, MySqlParameterSource.of(cinemaDto), keyHolder);

        return Objects.requireNonNull(keyHolder.getKeyAs(BigInteger.class)).longValue();
    }

    private long addHall(HallDto hallDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_HALL, MySqlParameterSource.of(hallDto), keyHolder);

        return Objects.requireNonNull(keyHolder.getKeyAs(BigInteger.class)).longValue(); //hallId

//        List<PlaceDto> places = getPlaces(hallId);
//        SqlParameterSource[] batchPlaces = MySqlParameterSource.createBatch(places);
//        jdbcTemplate.batchUpdate(INSERT_PLACE, batchPlaces);
    }

    private void addSession(SessionDto sessionDto){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_SESSION, MySqlParameterSource.of(sessionDto), keyHolder);

        long sessionId = Objects.requireNonNull(keyHolder.getKeyAs(BigInteger.class)).longValue();

        List<PlaceDto> places = getPlaces(sessionId);
        SqlParameterSource[] batchPlaces = MySqlParameterSource.createBatch(places);
        jdbcTemplate.batchUpdate(INSERT_PLACE, batchPlaces);
    }

    private List<PlaceDto> getPlaces(long sessionId) {
        List<PlaceDto> places = new ArrayList<>();

        for (int row = 1; row <= 8; row++) {
            for (int num = 1; num <= 12; num++) {
                PlaceType placeType = PlaceType.NORMAL;

                if (row <= 2 && (num <= 2 || num >= 7)) {
                    placeType = PlaceType.CHEAP;
                } else if (row >= 7 && (num >= 5 || num <= 8)) {
                    placeType = PlaceType.VIP;
                }

                places.add(new PlaceDto(sessionId, row, num, placeType));
            }
        }

        return places;
    }

    private void addMovies() {
        List<MovieDto> movies = getMovies();
        SqlParameterSource[] batchMovies = SqlParameterSourceUtils.createBatch(movies);
        jdbcTemplate.batchUpdate(INSERT_MOVIE, batchMovies);
    }

    private List<MovieDto> getMovies() {
        List<MovieDto> movies = new ArrayList<>();

        movies.add(MovieDto.builder()
                .title("Gladiator")
                .description("A former Roman General sets out to exact vengeance against the corrupt emperor who murdered his family and sent him into slavery.")
                .genre("Action")
                .director("Ridley Scott")
                .actors("Russell Crowe, Joaquin Phoenix, Connie Nielsen")
                .duration(155)
                .country("USA")
                .releaseDate(LocalDate.of(2000, 5, 5))
                .posterUrl("https://www.imdb.com/title/tt0172495/mediaviewer/rm638309888")
                .trailerUrl("https://www.youtube.com/watch?v=AxQajgTyLcM")
                .build());

        movies.add(MovieDto.builder()
                .title("The Departed")
                .description("An undercover cop and a mole in the police attempt to identify each other while infiltrating an Irish gang in South Boston.")
                .genre("Crime")
                .director("Martin Scorsese")
                .actors("Leonardo DiCaprio, Matt Damon, Jack Nicholson")
                .duration(151)
                .country("USA")
                .releaseDate(LocalDate.of(2006, 10, 6))
                .posterUrl("https://www.imdb.com/title/tt0407887/mediaviewer/rm218243968")
                .trailerUrl("https://www.youtube.com/watch?v=auYbpnEwBBg")
                .build());

        movies.add(MovieDto.builder()
                .title("Goodfellas")
                .description("The story of Henry Hill and his life in the mob, covering his relationship with his wife Karen Hill and his mob partners Jimmy Conway and Tommy DeVito in the Italian-American crime syndicate.")
                .genre("Crime")
                .director("Martin Scorsese")
                .actors("Robert De Niro, Ray Liotta, Joe Pesci")
                .duration(146)
                .country("USA")
                .releaseDate(LocalDate.of(1990, 9, 21))
                .posterUrl("https://www.imdb.com/title/tt0099685/mediaviewer/rm3652080128")
                .trailerUrl("https://www.youtube.com/watch?v=qo5jJpHtI1Y")
                .build());

        movies.add(MovieDto.builder()
                .title("Schindler's List")
                .description("In German-occupied Poland during World War II, industrialist Oskar Schindler gradually becomes concerned for his Jewish workforce after witnessing their persecution by the Nazis.")
                .genre("Drama")
                .director("Steven Spielberg")
                .actors("Liam Neeson, Ralph Fiennes, Ben Kingsley")
                .duration(195)
                .country("USA")
                .releaseDate(LocalDate.of(1994, 2, 4))
                .posterUrl("https://www.imdb.com/title/tt0108052/mediaviewer/rm4112966144")
                .trailerUrl("https://www.youtube.com/watch?v=JdRGC-w9syA")
                .build());

        movies.add(MovieDto.builder()
                .title("The Prestige")
                .description("After a tragic accident, two stage magicians engage in a battle to create the ultimate illusion while sacrificing everything they have to outwit each other.")
                .genre("Drama")
                .director("Christopher Nolan")
                .actors("Christian Bale, Hugh Jackman, Scarlett Johansson")
                .duration(130)
                .country("USA")
                .releaseDate(LocalDate.of(2006, 10, 20))
                .posterUrl("https://www.imdb.com/title/tt0482571/mediaviewer/rm3032699392")
                .trailerUrl("https://www.youtube.com/watch?v=o4gHCmTQDVI")
                .build());

        movies.add(MovieDto.builder()
                .title("Interstellar")
                .description("A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.")
                .genre("Adventure, Drama, Sci-Fi")
                .director("Christopher Nolan")
                .actors("Matthew McConaughey, Anne Hathaway, Jessica Chastain")
                .duration(169)
                .country("USA, UK, Canada")
                .releaseDate(LocalDate.of(2014, 11, 7))
                .posterUrl("https://www.imdb.com/title/tt0816692/mediaviewer/rm3493722368")
                .trailerUrl("https://www.youtube.com/watch?v=zSWdZVtXT7E")
                .build());

        movies.add(MovieDto.builder()
                .title("Parasite")
                .description("Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.")
                .genre("Comedy, Drama, Thriller")
                .director("Bong Joon Ho")
                .actors("Kang-ho Song, Sun-kyun Lee, Yeo-jeong Jo")
                .duration(132)
                .country("South Korea")
                .releaseDate(LocalDate.of(2019, 11, 8))
                .posterUrl("https://www.imdb.com/title/tt6751668/mediaviewer/rm2433157632")
                .trailerUrl("https://www.youtube.com/watch?v=5xH0HfJHsaY")
                .build());

        movies.add(MovieDto.builder()
                .title("Joker")
                .description("In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker.")
                .genre("Crime, Drama, Thriller")
                .director("Todd Phillips")
                .actors("Joaquin Phoenix, Robert De Niro, Zazie Beetz")
                .duration(122)
                .country("USA, Canada")
                .releaseDate(LocalDate.of(2019, 10, 4))
                .posterUrl("https://www.imdb.com/title/tt7286456/mediaviewer/rm1756471296")
                .trailerUrl("https://www.youtube.com/watch?v=t433PEQGErc")
                .build());

        movies.add(MovieDto.builder()
                .title("The Shape of Water")
                .description("At a top-secret research facility in the 1960s, a lonely janitor forms a unique relationship with an amphibious creature that is being held in captivity.")
                .genre("Adventure, Drama, Fantasy")
                .director("Guillermo del Toro")
                .actors("Sally Hawkins, Octavia Spencer, Michael Shannon")
                .duration(123)
                .country("USA, Canada")
                .releaseDate(LocalDate.of(2017, 12, 22))
                .posterUrl("https://www.imdb.com/title/tt5580390/mediaviewer/rm4813667840")
                .trailerUrl("https://www.youtube.com/watch?v=XFYWazblaUA")
                .build());

        movies.add(MovieDto.builder()
                .title("La La Land")
                .description("While navigating their careers in Los Angeles, a pianist and an actress fall in love while attempting to reconcile their aspirations for the future.")
                .genre("Comedy, Drama, Music")
                .director("Damien Chazelle")
                .actors("Ryan Gosling, Emma Stone, Rosemarie DeWitt")
                .duration(128)
                .country("USA")
                .releaseDate(LocalDate.of(2016, 12, 25))
                .posterUrl("https://www.imdb.com/title/tt3783958/mediaviewer/rm3223629056")
                .trailerUrl("https://www.youtube.com/watch?v=0pdqf4P9MB8")
                .build());

        movies.add(MovieDto.builder()
                .title("1917")
                .description("April 6th, 1917. As a regiment assembles to wage war deep in enemy territory, two soldiers are assigned to race against time and deliver a message that will stop 1,600 men from walking straight into a deadly trap.")
                .genre("Drama, War")
                .director("Sam Mendes")
                .actors("Dean-Charles Chapman, George MacKay, Daniel Mays")
                .duration(119)
                .country("USA, UK, India, Spain, Canada")
                .releaseDate(LocalDate.of(2019, 12, 25))
                .posterUrl("https://www.imdb.com/title/tt8579674/mediaviewer/rm4287605504")
                .trailerUrl("https://www.youtube.com/watch?v=YqNYrYUiMfg")
                .build());

        movies.add(MovieDto.builder()
                .title("The Irishman")
                .description("An old man recalls his time painting houses for his friend, Jimmy Hoffa, through the 1950-70s.")
                .genre("Biography, Crime, Drama")
                .director("Martin Scorsese")
                .actors("Robert De Niro, Al Pacino, Joe Pesci")
                .duration(209)
                .country("USA")
                .releaseDate(LocalDate.of(2019, 11, 27))
                .posterUrl("https://www.imdb.com/title/tt1302006/mediaviewer/rm3916927744")
                .trailerUrl("https://www.youtube.com/watch?v=WHXxVmeGQUc")
                .build());

        movies.add(MovieDto.builder()
                .title("Dunkirk")
                .description("Allied soldiers from Belgium, the British Empire, and France are surrounded by the German Army and evacuated during a fierce battle in World War II.")
                .genre("Action, Drama, History")
                .director("Christopher Nolan")
                .actors("Fionn Whitehead, Barry Keoghan, Mark Rylance")
                .duration(106)
                .country("USA, UK, Netherlands, France")
                .releaseDate(LocalDate.of(2017, 7, 21))
                .posterUrl("https://www.imdb.com/title/tt5013056/mediaviewer/rm1031177984")
                .trailerUrl("https://www.youtube.com/watch?v=F-eMt3SrfFU")
                .build());

        movies.add(MovieDto.builder()
                .title("Mad Max: Fury Road")
                .description("In a post-apocalyptic wasteland, a woman rebels against a tyrannical ruler in search of her homeland with the aid of a group of female prisoners, a psychotic worshiper, and a drifter named Max.")
                .genre("Action, Adventure, Sci-Fi")
                .director("George Miller")
                .actors("Tom Hardy, Charlize Theron, Nicholas Hoult")
                .duration(120)
                .country("Australia, USA")
                .releaseDate(LocalDate.of(2015, 5, 15))
                .posterUrl("https://www.imdb.com/title/tt1392190/mediaviewer/rm1478712320")
                .trailerUrl("https://www.youtube.com/watch?v=hEJnMQG9ev8")
                .build());

        movies.add(MovieDto.builder()
                .title("The Revenant")
                .description("A frontiersman on a fur trading expedition in the 1820s fights for survival after being mauled by a bear and left for dead by members of his own hunting team.")
                .genre("Action, Adventure, Drama")
                .director("Alejandro G. Iñárritu")
                .actors("Leonardo DiCaprio, Tom Hardy, Will Poulter")
                .duration(156)
                .country("USA, Hong Kong, Taiwan")
                .releaseDate(LocalDate.of(2015, 12, 25))
                .posterUrl("https://www.imdb.com/title/tt1663202/mediaviewer/rm1779893248")
                .trailerUrl("https://www.youtube.com/watch?v=LoebZZ8K5N0")
                .build());

        movies.add(MovieDto.builder()
                .title("Whiplash")
                .description("A promising young drummer enrolls at a cut-throat music conservatory where his dreams of greatness are mentored by an instructor who will stop at nothing to realize a student's potential.")
                .genre("Drama, Music")
                .director("Damien Chazelle")
                .actors("Miles Teller, J.K. Simmons, Melissa Benoist")
                .duration(106)
                .country("USA")
                .releaseDate(LocalDate.of(2014, 10, 15))
                .posterUrl("https://www.imdb.com/title/tt2582802/mediaviewer/rm2522676992")
                .trailerUrl("https://www.youtube.com/watch?v=7d_jQycdQGo")
                .build());

        movies.add(MovieDto.builder()
                .title("Birdman or (The Unexpected Virtue of Ignorance)")
                .description("A washed-up superhero actor attempts to revive his fading career by writing, directing, and starring in a Broadway production.")
                .genre("Comedy, Drama")
                .director("Alejandro G. Iñárritu")
                .actors("Michael Keaton, Zach Galifianakis, Edward Norton")
                .duration(119)
                .country("USA")
                .releaseDate(LocalDate.of(2014, 10, 17))
                .posterUrl("https://www.imdb.com/title/tt2562232/mediaviewer/rm3739243008")
                .trailerUrl("https://www.youtube.com/watch?v=uJfLoE6hanc")
                .build());

        movies.add(MovieDto.builder()
                .title("Her")
                .description("In a near future, a lonely writer develops an unlikely relationship with an operating system designed to meet his every need.")
                .genre("Drama, Romance, Sci-Fi")
                .director("Spike Jonze")
                .actors("Joaquin Phoenix, Amy Adams, Scarlett Johansson")
                .duration(126)
                .country("USA")
                .releaseDate(LocalDate.of(2013, 12, 18))
                .posterUrl("https://m.media-amazon.com/images/M/MV5BMjA1Nzk0OTM2OF5BMl5BanBnXkFtZTgwNjU2NjEwMDE@._V1_.jpg")
                .trailerUrl("https://www.youtube.com/watch?v=WzV6mXIOVl4")
                .build());

        movies.add(MovieDto.builder()
                .title("Gravity")
                .description("Two astronauts work together to survive after an accident leaves them stranded in space.")
                .genre("Drama, Sci-Fi, Thriller")
                .director("Alfonso Cuarón")
                .actors("Sandra Bullock, George Clooney, Ed Harris")
                .duration(91)
                .country("UK, USA")
                .releaseDate(LocalDate.of(2013, 10, 4))
                .posterUrl("https://m.media-amazon.com/images/M/MV5BMTY1NTc0NTA0OV5BMl5BanBnXkFtZTgwOTE4MjgwMTE@._V1_.jpg")
                .trailerUrl("https://www.youtube.com/watch?v=OiTiKOy59o4")
                .build());

        movies.add(MovieDto.builder()
                .title("The Wolf of Wall Street")
                .description("Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption, and the federal government.")
                .genre("Biography, Crime, Drama")
                .director("Martin Scorsese")
                .actors("Leonardo DiCaprio, Jonah Hill, Margot Robbie")
                .duration(180)
                .country("USA")
                .releaseDate(LocalDate.of(2013, 12, 25))
                .posterUrl("https://m.media-amazon.com/images/M/MV5BMjIxMjgxNTk0MF5BMl5BanBnXkFtZTgwNjIyOTg2MDE@._V1_.jpg")
                .trailerUrl("https://www.youtube.com/watch?v=iszwuX1AK6A")
                .build());

        movies.add(MovieDto.builder()
                .title("The Social Network")
                .description("As Harvard student Mark Zuckerberg creates the social networking site that would become known as Facebook, he is sued by the twins who claimed he stole their idea, and by the co-founder who was later squeezed out of the business.")
                .genre("Biography, Drama")
                .director("David Fincher")
                .actors("Jesse Eisenberg, Andrew Garfield, Justin Timberlake")
                .duration(120)
                .country("USA")
                .releaseDate(LocalDate.of(2010, 10, 1))
                .posterUrl("https://m.media-amazon.com/images/M/MV5BYTBkNDMwNzgtZjJkNi00ZTVhLWE5MzAtMTYzN2U4OTNjOWRjXkEyXkFqcGdeQXVyOTAzODkzMjI@._V1_.jpg")
                .trailerUrl("https://www.youtube.com/watch?v=lB95KLmpLR4")
                .build());

        movies.add(MovieDto.builder()
                .title("Black Swan")
                .description("A committed dancer struggles to maintain her sanity after winning the lead role in a production of Tchaikovsky's \"Swan Lake\".")
                .genre("Drama, Thriller")
                .director("Darren Aronofsky")
                .actors("Natalie Portman, Mila Kunis, Vincent Cassel")
                .duration(108)
                .country("USA")
                .releaseDate(LocalDate.of(2010, 12, 17))
                .posterUrl("https://m.media-amazon.com/images/M/MV5BMjA0ODU4MzYxMV5BMl5BanBnXkFtZTcwMjQ2NTcxNA@@._V1_.jpg")
                .trailerUrl("https://www.youtube.com/watch?v=5jaI1XOB-bs")
                .build());

        movies.add(MovieDto.builder()
                .title("The King's Speech")
                .description("The story of King George VI, his impromptu ascension to the throne of the British Empire in 1936, and the speech therapist who helped the unsure monarch overcome his stammer.")
                .genre("Biography, Drama, History")
                .director("Tom Hooper")
                .actors("Colin Firth, Geoffrey Rush, Helena Bonham Carter")
                .duration(118)
                .country("UK, USA, Australia")
                .releaseDate(LocalDate.of(2010, 12, 25))
                .posterUrl("https://m.media-amazon.com/images/M/MV5BOWM5MDUwZWMtYzU1YS00NmRiLTkxN2YtMGViNGMzOTRiOWM5XkEyXkFqcGdeQXVyMTQyMTMwOTk0._V1_.jpg")
                .trailerUrl("https://www.youtube.com/watch?v=kl8F-8tR8to")
                .build());

        movies.add(MovieDto.builder()
                .title("Inception")
                .description("A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.")
                .genre("Action, Adventure, Sci-Fi")
                .director("Christopher Nolan")
                .actors("Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page")
                .duration(148)
                .country("USA, UK")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .posterUrl("https://m.media-amazon.com/images/M/MV5BMjExMjkwNTQ0Nl5BMl5BanBnXkFtZTcwNTY0OTk1Mw@@._V1_.jpg")
                .trailerUrl("https://www.youtube.com/watch?v=YoHD9XEInc0")
                .build());

        return movies;

    }
}
