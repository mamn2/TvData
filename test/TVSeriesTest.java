import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TVSeriesTest {

    //Loads JSON data about the TV Series into form of a TVSeries object. Info retrieved from "Data" folder.
    private static final TVSeries GAME_OF_THRONES = deserializeData("GameOfThrones");
    private static final TVSeries HOMELAND = deserializeData("Homeland");

    /**
     * This class will deserialize the information located in a JSON object to fit the fields of the TVSeries class.
     * @param tvShow is the name of the TVShow.
     * @return a TVSeries object with all fields initialized.
     */
    private static TVSeries deserializeData(String tvShow) {

        //Stores all the data from JSON file in the form of a Java JsonObject
        JsonObject tvShowData = new JsonParser().parse(Data.getFileContentsAsString(tvShow)).getAsJsonObject();

        //This will initialize summary, name, and language fields for a TVSeries
        TVSeries tvSeries = new Gson().fromJson(tvShowData, TVSeries.class);

        //Ratings are set manually since they are located within an object
        tvSeries.setRatings(tvShowData.get("rating").getAsJsonObject().get("average").getAsDouble());

        //Network name is set manually since it is located within an object
        tvSeries.setNetworkName(tvShowData.get("network").getAsJsonObject().get("name").getAsString());

        //Premiere date is set manually because the field "premiered" is not specific enough
        tvSeries.setPremiereDate(tvShowData.get("premiered").getAsString());

        //Since the genres are stored in a JsonArray, we loop through it to convert to a java array
        String[] genres = new String[tvShowData.get("genres").getAsJsonArray().size()];
        for (int i = 0; i < genres.length; i++) {
            genres[i] = tvShowData.get("genres").getAsJsonArray().get(i).getAsString();
        }
        tvSeries.setGenres(genres);

        //Since the episodes are stored in a JsonArray, we loop through it to convert it to a java array
        TVSeries.TVEpisode[] episodes = new TVSeries.TVEpisode[tvShowData.get("_embedded").getAsJsonObject()
                                                                         .get("episodes").getAsJsonArray().size()];
        for (int i = 0; i < episodes.length; i++) {
            //gets each episodes and stores it in a TVEpisode object
            episodes[i] = new Gson().fromJson(tvShowData.get("_embedded").getAsJsonObject()
                                                        .get("episodes").getAsJsonArray().get(i),
                                                        TVSeries.TVEpisode.class);
        }
        tvSeries.setEpisodes(episodes);

        return tvSeries;

    }


    @Test
    public void getSeriesNameTest() throws AssertionError {

        assertEquals("Game of Thrones", GAME_OF_THRONES.getName());
        assertEquals("Homeland", HOMELAND.getName());

    }

    @Test
    public void getGenreTest() throws AssertionError {

        //Ensures that one of the contents of the genres array is "Drama"
        assertTrue(Arrays.asList(GAME_OF_THRONES.getGenres()).contains("Drama"));
        assertTrue(Arrays.asList(HOMELAND.getGenres()).contains("Drama") );

    }

    @Test
    public void getGenreArrayTest() throws AssertionError {

        assertArrayEquals(new String[] { "Drama", "Thriller", "Espionage" }, HOMELAND.getGenres());

    }

    @Test
    public void getPremiereTest() throws AssertionError {

        assertEquals("2011-04-17", GAME_OF_THRONES.getPremiereDate());
        assertEquals("2011-10-02", HOMELAND.getPremiereDate());

    }

    @Test
    public void getRatingTest() throws AssertionError {

        //Delta ensures that any loss of precision is accounted for (this is because of how doubles work).
        assertEquals(9.3, GAME_OF_THRONES.getRatings(), .01);
        assertEquals(8.3, HOMELAND.getRatings(), .01);

    }

    @Test
    public void getNetworkTest() throws AssertionError {

        assertEquals("HBO", GAME_OF_THRONES.getNetworkName());
        assertEquals("Showtime", HOMELAND.getNetworkName());

    }

    @Test
    public void getEpisodeTest() throws AssertionError {

        //Checks to make sure getEpisode throws an exception for episodes that don't exist
        boolean exceptionThrown = false;
        try {
            assertNull(GAME_OF_THRONES.getEpisode(13, 5));
        } catch (ArrayIndexOutOfBoundsException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void getEpisodeNameTest() throws AssertionError {

        //Test for early season/episode
        assertEquals("Pilot", HOMELAND.getEpisode(1, 1).getEpisodeName());

        //Test for middle values
        assertEquals("Blackwater", GAME_OF_THRONES.getEpisode(2, 9).getEpisodeName());

        //Test for last episode
        assertEquals("TBA", GAME_OF_THRONES.getEpisode(8, 1).getEpisodeName());

    }

    @Test
    public void getEpisodeAirDate() throws AssertionError {

        //Test for early episode
        assertEquals("2011-10-09", HOMELAND.getEpisode(1, 2).getAirdate());

        //Test for middle values
        assertEquals("2011-12-18", HOMELAND.getEpisode(1, 12).getAirdate());

        //Test for last episode
        assertEquals("2018-04-29", HOMELAND.getEpisode(7, 12).getAirdate());

    }

    @Test
    public void getEpisodeSeason() throws AssertionError {

        //Test for early episode
        assertEquals(1, HOMELAND.getEpisode(1, 1).getSeason());

        //Test for middle values
        assertEquals(3, GAME_OF_THRONES.getEpisode(3, 2).getSeason());

        //Test for last values
        assertEquals(8, GAME_OF_THRONES.getEpisode(8, 1).getSeason());

    }

    @Test
    public void getEpisodeNumber() throws AssertionError {

        //Test for early values
        assertEquals(1, HOMELAND.getEpisode(1, 1).getNumber());

        //Test for middle values
        assertEquals(4, GAME_OF_THRONES.getEpisode(4, 4).getNumber());

        //Test for last values
        assertEquals(7, GAME_OF_THRONES.getEpisode(7, 7).getNumber());

    }

}
