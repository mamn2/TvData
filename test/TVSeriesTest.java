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

    }

    @Test
    public void getGenreTest() throws AssertionError {

        //Tests for first value in genre array
        assertTrue(Arrays.asList(HOMELAND.getGenres()).contains("Drama"));

        //Tests for other values in genre array
        assertTrue(Arrays.asList(GAME_OF_THRONES.getGenres()).contains("Fantasy") );

    }

    @Test
    public void getGenreArrayTest() throws AssertionError {

        assertArrayEquals(new String[] { "Drama", "Thriller", "Espionage" }, HOMELAND.getGenres());

    }

    @Test
    public void getPremiereTest() throws AssertionError {

        assertEquals("2011-10-02", HOMELAND.getPremiereDate());

    }

    @Test
    public void getRatingTest() throws AssertionError {

        //Delta ensures that any loss of precision is accounted for (this is because of how doubles work).
        assertEquals(9.3, GAME_OF_THRONES.getRatings(), .01);

    }

    @Test
    public void getNetworkTest() throws AssertionError {

        assertEquals("HBO", GAME_OF_THRONES.getNetworkName());

    }

    @Test
    public void getSeriesSummaryTest() throws AssertionError {

        String homelandSummary = "<p>The winner of 6 Emmy Awards including Outstanding Drama Series, <b>Homeland</b> is an edge-of-your-seat sensation. Marine Sergeant Nicholas Brody is both a decorated hero and a serious threat. CIA officer Carrie Mathison is tops in her field despite being bipolar. The delicate dance these two complex characters perform, built on lies, suspicion, and desire, is at the heart of this gripping, emotional thriller in which nothing short of the fate of our nation is at stake.</p>";
        assertEquals(homelandSummary, HOMELAND.getSummary());

    }

    @Test
    public void getEpisodeSummaryTest() throws AssertionError {

        //Tests for early cases
        String homelandS1E1Summary = "<p>In the opener of this \"Manchurian Candidate\"-like political thriller, a marine rescued after eight years as a POW in Afghanistan returns home a war hero. But a CIA operative suspects he may actually be an enemy agent with a connection to Al Qaeda and part of a plan to commit a terrorist act on U.S. soil.</p>";
        assertEquals(homelandS1E1Summary, HOMELAND.getEpisode(1, 1).getSummary());

        //Tests for middle cases
        String gotS5E2Summary = "<p>Arya arrives in Braavos; Brienne and Podrick find danger while traveling; Cersei worries about Myrcella in Dorne when Ellaria Sand seeks revenge for Oberyn's death; Jon is tempted by Stannis.</p>";
        assertEquals(gotS5E2Summary, GAME_OF_THRONES.getEpisode(5, 2).getSummary());

        //Test for late case
        String homelandS7E12Summary = "<p>Carrie and Saul's mission doesn't go as planned. Elizabeth Keane fights for her presidency.</p>";
        assertEquals(homelandS7E12Summary, HOMELAND.getEpisode(7, 12).getSummary());

    }

    @Test
    public void getEpisodeTest() throws AssertionError {

        //Tests equality of a full TVEpisode object by creating another instance of "Winter is Coming" GOT S1E1
        TVSeries.TVEpisode gotS1E1 = GAME_OF_THRONES.createNewEpisode();
        gotS1E1.setName("Winter is Coming");
        gotS1E1.setSummary("<p>Lord Eddard Stark, ruler of the North, is summoned to court by his old friend, King Robert Baratheon, to serve as the King's Hand. Eddard reluctantly agrees after learning of a possible threat to the King's life. Eddard's bastard son Jon Snow must make a painful decision about his own future, while in the distant east Viserys Targaryen plots to reclaim his father's throne, usurped by Robert, by selling his sister in marriage.</p>");
        gotS1E1.setAirdate("2011-04-17");
        gotS1E1.setSeason(1);
        gotS1E1.setNumber(1);
        assertEquals(gotS1E1, GAME_OF_THRONES.getEpisode(1, 1));

        //Tests for later episode edge cases
        TVSeries.TVEpisode homelandS7E12 = HOMELAND.createNewEpisode();
        homelandS7E12.setName("Paean to the People");
        homelandS7E12.setSummary("<p>Carrie and Saul's mission doesn't go as planned. Elizabeth Keane fights for her presidency.</p>");
        homelandS7E12.setAirdate("2018-04-29");
        homelandS7E12.setSeason(7);
        homelandS7E12.setNumber(12);
        assertEquals(homelandS7E12, HOMELAND.getEpisode(7, 12));

        //Checks to make sure getEpisode throws an exception for episodes that don't exist
        boolean exceptionThrown = false;
        try {
            assertNull("This episode doesn't exist", GAME_OF_THRONES.getEpisode(13, 5));
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
