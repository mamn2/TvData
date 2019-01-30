import com.google.gson.JsonParser;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class TVSeriesTest {

    private static final TVSeries GAME_OF_THRONES = new TVSeries(
            new JsonParser().parse(Data.getFileContentsAsString("GameOfThrones")).getAsJsonObject());
    private static final TVSeries HOMELAND = new TVSeries(
            new JsonParser().parse(Data.getFileContentsAsString("Homeland")).getAsJsonObject());
    // This is an empty TVSeries used as a constant for testing purposes
    private static final TVSeries EMPTY_SERIES = new TVSeries("An empty series");

    @Test
    public void getSeriesNameTest() throws AssertionError {

        assertEquals("Game of Thrones", GAME_OF_THRONES.getSeriesName());

    }

    @Test
    public void getNullSeriesNameTest() throws AssertionError {

        EMPTY_SERIES.setSeriesName(null);
        assertNull(EMPTY_SERIES.getSeriesName());

        //Revert the value so other tests are not affected
        EMPTY_SERIES.setSeriesName("An empty series");

    }

    @Test
    public void getGenresTest() throws AssertionError {

        assertArrayEquals(new String[] { "Drama", "Thriller", "Espionage" }, HOMELAND.getGenres());

    }

    @Test
    public void getNullGenresTest() throws AssertionError {

        assertNull(EMPTY_SERIES.getGenres());

    }

    @Test
    public void getLanguageTest() throws AssertionError {

        assertEquals("English", GAME_OF_THRONES.getLanguage());

    }

    @Test
    public void getNullLanguageTest() throws AssertionError {

        assertNull(EMPTY_SERIES.getLanguage());

    }

    @Test
    public void getPremiereTest() throws AssertionError {

        assertEquals("2011-10-02", HOMELAND.getPremiereDate());

    }

    @Test
    public void getNullPremiereTest() throws AssertionError {

        assertNull(EMPTY_SERIES.getPremiereDate());

    }

    @Test
    public void getRatingTest() throws AssertionError {

        //Delta ensures that any loss of precision is accounted for (this is because of how doubles work).
        assertEquals(9.3, GAME_OF_THRONES.getAverageRating(), .01);

    }

    @Test
    public void getNullRatingTest() throws AssertionError {

        assertEquals(0.0, EMPTY_SERIES.getAverageRating(), 0.000001);

    }

    @Test
    public void getNetworkTest() throws AssertionError {

        assertEquals("HBO", GAME_OF_THRONES.getNetworkName());

    }

    @Test
    public void getNullNetworkTest() throws AssertionError {

        assertNull(EMPTY_SERIES.getNetworkName());

    }

    @Test
    public void getSeriesSummaryTest() throws AssertionError {

        String homelandSummary = "The winner of 6 Emmy Awards including Outstanding Drama Series, Homeland is an edge-of-your-seat sensation. Marine Sergeant Nicholas Brody is both a decorated hero and a serious threat. CIA officer Carrie Mathison is tops in her field despite being bipolar. The delicate dance these two complex characters perform, built on lies, suspicion, and desire, is at the heart of this gripping, emotional thriller in which nothing short of the fate of our nation is at stake.";
        assertEquals(homelandSummary, HOMELAND.getSeriesSummary());

    }

    @Test
    public void getNullSeriesSummaryTest() throws AssertionError {

        assertNull(EMPTY_SERIES.getSeriesSummary());

    }

    @Test
    public void getEpisodeSummaryTest() throws AssertionError {

        String homelandS1E1Summary = "In the opener of this \"Manchurian Candidate\"-like political thriller, a marine rescued after eight years as a POW in Afghanistan returns home a war hero. But a CIA operative suspects he may actually be an enemy agent with a connection to Al Qaeda and part of a plan to commit a terrorist act on U.S. soil.";
        assertEquals(homelandS1E1Summary, HOMELAND.getEpisode(1, 1).getSummary());

    }

    @Test
    public void getEpisodeNullSummary() throws AssertionError {

        TVSeries.TVEpisode noSummaryEpisode = EMPTY_SERIES.createNewEpisode();
        assertNull(noSummaryEpisode.getSummary());

    }

    @Test
    public void getEpisodeTest() throws AssertionError {

        //Creating equivalent episode for comparison
        TVSeries.TVEpisode homelandS7E12 = HOMELAND.createNewEpisode();
        homelandS7E12.setName("Paean to the People");
        homelandS7E12.setSummary("Carrie and Saul's mission doesn't go as planned. Elizabeth Keane fights for her presidency.");
        homelandS7E12.setAirdate("2018-04-29");
        homelandS7E12.setSeason(7);
        homelandS7E12.setNumber(12);
        homelandS7E12.setRuntimeInMinutes(60);

        assertEquals(homelandS7E12, HOMELAND.getEpisode(7, 12));

    }

    @Test
    public void getNullSeasonAndEpisodeTest() throws AssertionError {

        //Checks to make sure getEpisode returns null for seasons and episodes that don't exist
        boolean exceptionThrown = false;

        try {
            GAME_OF_THRONES.getEpisode(13, 5);
        } catch (Exception e) {
            exceptionThrown = true;
        }

        assertTrue("Season/Episodes doesn't exist", exceptionThrown);

    }

    @Test
    public void getNullEpisodeInViableSeasonTest() throws AssertionError {

        //Checks to make sure getEpisode returns null for an episode that doesn't exist, even if the season does
        boolean exceptionThrown = false;

        try {
            GAME_OF_THRONES.getEpisode(7, 8);
        } catch (Exception e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);

    }

    @Test
    public void getEpisodeNameTest() throws AssertionError {

        assertEquals("Blackwater", GAME_OF_THRONES.getEpisode(2, 9).getEpisodeName());

    }

    @Test
    public void getNullEpisodeName() throws AssertionError {

        TVSeries.TVEpisode noNameEpisode = EMPTY_SERIES.createNewEpisode();

        assertNull(noNameEpisode.getEpisodeName());

    }

    @Test
    public void getEpisodeAirDateTest() throws AssertionError {

        assertEquals("2018-04-29", HOMELAND.getEpisode(7, 12).getAirdate());

    }

    @Test
    public void setNullEpisodeTest() throws AssertionError {

        boolean exceptionThrown = false;
        try {
            TVSeries.TVEpisode[] nullContents = new TVSeries.TVEpisode[1];
            GAME_OF_THRONES.setEpisodes(nullContents);
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertTrue("Setting null episodes should throw an exception", exceptionThrown);

    }

    @Test
    public void setBadEpisodeAirDateTest() throws AssertionError {

        TVSeries.TVEpisode badEpisode = EMPTY_SERIES.createNewEpisode();
        badEpisode.setAirdate("notanairdate");
        assertNull(badEpisode.getAirdate());

    }

    @Test
    public void sedGoodEpisodeAirDateTest() throws AssertionError {

        TVSeries.TVEpisode goodEpisode = EMPTY_SERIES.createNewEpisode();
        goodEpisode.setAirdate("2011-03-04");
        assertEquals("2011-03-04", goodEpisode.getAirdate());

    }

    @Test
    public void getNullAirDateTest() throws AssertionError {

        TVSeries.TVEpisode nullAirDate = EMPTY_SERIES.createNewEpisode();
        assertNull(nullAirDate.getAirdate());

    }

    @Test
    public void replaceEpisodeTest() {

        TVSeries.TVEpisode gotS8E1 = GAME_OF_THRONES.createNewEpisode();
        gotS8E1.setName("Still don't know the name");
        gotS8E1.setSummary("I wish I knew the plot!!!");
        gotS8E1.setAirdate("2011-04-17");
        gotS8E1.setSeason(8);
        gotS8E1.setNumber(1);
        gotS8E1.setRuntimeInMinutes(60);

        GAME_OF_THRONES.addOrReplaceEpisode(gotS8E1);
        assertEquals(gotS8E1, GAME_OF_THRONES.getEpisode(8, 1));

    }

    @Test
    public void addNewEpisodeTest() {

        TVSeries.TVEpisode gotS8E2 = GAME_OF_THRONES.createNewEpisode();
        gotS8E2.setName("Episode hasn't been announced");
        gotS8E2.setSummary("I'm no plot leaker :)");
        gotS8E2.setAirdate("2019-04-21");
        gotS8E2.setSeason(8);
        gotS8E2.setNumber(2);

        GAME_OF_THRONES.addOrReplaceEpisode(gotS8E2);
        assertEquals(gotS8E2, GAME_OF_THRONES.getEpisode(8, 2));

    }

    @Test
    public void getEpisodeSeasonTest() throws AssertionError {

        //Test for middle values
        assertEquals(3, GAME_OF_THRONES.getEpisode(3, 2).getSeason());

    }

    @Test
    public void getEpisodeNullSeasonTest() throws AssertionError {

        TVSeries.TVEpisode emptyEpisode = EMPTY_SERIES.createNewEpisode();
        assertEquals(0, emptyEpisode.getSeason());

    }

    @Test
    public void getEpisodeNumberTest() throws AssertionError {

        assertEquals(7, GAME_OF_THRONES.getEpisode(7, 7).getNumber());

    }

    @Test
    public void getEpisodeNullNumberTest() {

        TVSeries.TVEpisode emptyEpisode = EMPTY_SERIES.createNewEpisode();
        assertEquals(0, emptyEpisode.getNumber());

    }

    @Test
    public void getEpisodesByDateTest() throws AssertionError {

        assertEquals(HOMELAND.getEpisode(1, 12),
                     TVSeries.getEpisodesOnDate(HOMELAND.getEpisodes(), "2011-12-18")[0]);

    }

    @Test
    public void getEpisodeByBadDateTest() throws AssertionError {

        assertArrayEquals(new TVSeries.TVEpisode[0],
                TVSeries.getEpisodesOnDate(HOMELAND.getEpisodes(), "nonformatted"));

    }

    @Test
    public void getEpisodeOnNullDateTest() throws AssertionError {

        assertArrayEquals(new TVSeries.TVEpisode[0],
                TVSeries.getEpisodesOnDate(HOMELAND.getEpisodes(), null));

    }

    @Test
    public void getEpisodesInYearTest() throws AssertionError {

        TVSeries.TVEpisode[] gotEpisodesIn2019 = { GAME_OF_THRONES.getEpisode(8 , 1) };
        assertArrayEquals(gotEpisodesIn2019, TVSeries.getEpisodesInYear(GAME_OF_THRONES.getEpisodes(), 2019));

    }

    @Test
    public void getEpisodesInEmptyYearTest() throws AssertionError {

        assertTrue(TVSeries.getEpisodesInYear(GAME_OF_THRONES.getEpisodes(), 2020).length == 0);

    }

    @Test
    public void searchForEpisodesTest() throws AssertionError {

        TVSeries.TVEpisode[] searchResults = new TVSeries.TVEpisode[]
                { GAME_OF_THRONES.getEpisode(1, 1)};

        assertArrayEquals(searchResults,
                 TVSeries.searchEpisodesByName(GAME_OF_THRONES.getEpisodes(), "winter is coming"));

    }

    @Test
    public void searchForImaginaryEpisodeTest() throws AssertionError {

        TVSeries.TVEpisode[] searchResults = new TVSeries.TVEpisode[0];
        assertArrayEquals("Searching for non-existent episodes should return empty array",
                searchResults, TVSeries.searchEpisodesByName(GAME_OF_THRONES.getEpisodes(), "imaginary episode"));

    }

    @Test
    public void searchEpisodesByMaxRuntimeTest() throws AssertionError {

        LinkedList<TVSeries.TVEpisode> allHomelandEpisodes = new LinkedList<>();
        for (TVSeries.TVEpisode[] season : HOMELAND.getEpisodes()) {
            Collections.addAll(allHomelandEpisodes, season);
        }
        //All Homeland episodes are 60 minutes so they should be equal
        assertArrayEquals(allHomelandEpisodes.toArray(new TVSeries.TVEpisode[allHomelandEpisodes.size()]),
                TVSeries.searchEpisodesByMaxRuntime(HOMELAND.getEpisodes(), 60));

    }

    @Test
    public void searchEpisodesByCharacterTest() throws AssertionError {

        TVSeries.TVEpisode[] episodesWithJorah = new TVSeries.TVEpisode[3];
        episodesWithJorah[0] = GAME_OF_THRONES.getEpisode(3, 9);
        episodesWithJorah[1] = GAME_OF_THRONES.getEpisode(5, 6);
        episodesWithJorah[2] = GAME_OF_THRONES.getEpisode(6, 4);

        assertArrayEquals(episodesWithJorah,
                TVSeries.searchEpisodesByCharacter(GAME_OF_THRONES.getEpisodes(), "Jorah"));

    }

    @Test
    public void searchEpisodesByNullCharacterTest() throws AssertionError {

        assertArrayEquals(new TVSeries.TVEpisode[0],
                TVSeries.searchEpisodesByCharacter(GAME_OF_THRONES.getEpisodes(), null));

    }

    @Test
    public void getTotalNumberOfEpisodesTest() throws AssertionError {

        int numEpisodesInHomeland = 84;
        assertEquals(numEpisodesInHomeland, TVSeries.totalNumberOfEpisodes(HOMELAND.getEpisodes()));

    }

    @Test
    public void getTotalNumberOfEpisodesInEmptySeriesTest() throws AssertionError {

        assertEquals(0, TVSeries.totalNumberOfEpisodes(EMPTY_SERIES.getEpisodes()));

    }

    @Test
    public void getAverageRuntimeOfSeriesTest() throws AssertionError {

        int averageRuntimeInHomeland = 60;
        assertEquals(averageRuntimeInHomeland, TVSeries.averageRuntimeOfEpisodes(HOMELAND.getEpisodes()));

    }

    @Test
    public void getAverageRuntimeOfSeasonTest() throws AssertionError {

        int averageRuntimeGOTS4 = 60;
        assertEquals(averageRuntimeGOTS4, TVSeries.averageRuntimeOfEpisodes(GAME_OF_THRONES.getEpisodes()[3]));

    }

    @Test
    public void getMaximumRuntimeOfSeriesTest() throws AssertionError {

        int maxRuntimeInGameOfThrones = 60;
        assertEquals(maxRuntimeInGameOfThrones, TVSeries.maxRuntimeOfEpisodes(GAME_OF_THRONES.getEpisodes()));

    }

    @Test
    public void getMaximumRuntimeOfSeasonTest() throws AssertionError {

        int maxRuntimeInHomelandS1 = 60;
        assertEquals(maxRuntimeInHomelandS1, TVSeries.maxRuntimeOfEpisodes(HOMELAND.getEpisodes()[0]));

    }
    @Test
    public void getMinimumRuntimeOfSeriesTest() throws AssertionError {

        int minRuntimeInHomeland = 60;
        assertEquals(minRuntimeInHomeland, TVSeries.minRuntimeOfEpisodes(GAME_OF_THRONES.getEpisodes()));

    }

    @Test
    public void getMinimumRuntimeOfSeasonTest() throws AssertionError {

        int minRuntimeInGOTS5 = 60;
        assertEquals(minRuntimeInGOTS5, TVSeries.minRuntimeOfEpisodes(GAME_OF_THRONES.getEpisodes()[4]));

    }

}
