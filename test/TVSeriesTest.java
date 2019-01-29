import com.google.gson.JsonParser;
import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TVSeriesTest {

    private static final TVSeries GAME_OF_THRONES = new TVSeries(
            new JsonParser().parse(Data.getFileContentsAsString("GameOfThrones")).getAsJsonObject());
    private static final TVSeries HOMELAND = new TVSeries(
            new JsonParser().parse(Data.getFileContentsAsString("Homeland")).getAsJsonObject());
    private static final TVSeries CONTROL_SERIES = new TVSeries("An empty series");

    @Test
    public void getSeriesNameTest() throws AssertionError {

        assertEquals("Game of Thrones", GAME_OF_THRONES.getSeriesName());

    }

    @Test
    public void getNullSeriesNameTest() throws AssertionError {

        CONTROL_SERIES.setSeriesName(null);
        assertNull(CONTROL_SERIES.getSeriesName());
        CONTROL_SERIES.setSeriesName("An empty series");

    }

    @Test
    public void setNullEpisode() throws AssertionError {

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
    public void getGenresTest() throws AssertionError {

        //Tests all genres in genre array
        assertArrayEquals(new String[] { "Drama", "Thriller", "Espionage" }, HOMELAND.getGenres());

    }

    @Test
    public void getNullGenresTest() throws AssertionError {

        assertNull(CONTROL_SERIES.getGenres());

    }

    @Test
    public void getLanguageTest() throws AssertionError {

        assertEquals("English", GAME_OF_THRONES.getLanguage());

    }

    @Test
    public void getNullLanguageTest() throws AssertionError {

        assertNull(CONTROL_SERIES.getLanguage());

    }

    @Test
    public void getPremiereTest() throws AssertionError {

        assertEquals("2011-10-02", HOMELAND.getPremiereDate());

    }

    @Test
    public void getNullPremiereTest() throws AssertionError {

        assertNull(CONTROL_SERIES.getPremiereDate());

    }

    @Test
    public void getRatingTest() throws AssertionError {

        //Delta ensures that any loss of precision is accounted for (this is because of how doubles work).
        assertEquals(9.3, GAME_OF_THRONES.getAverageRating(), .01);

    }

    @Test
    public void getNullRatingTest() throws AssertionError {

        assertEquals(0.0, CONTROL_SERIES.getAverageRating(), 0.000001);

    }

    @Test
    public void getNetworkTest() throws AssertionError {

        assertEquals("HBO", GAME_OF_THRONES.getNetworkName());

    }

    @Test
    public void getNullNetworkTest() throws AssertionError {

        assertNull(CONTROL_SERIES.getNetworkName());

    }

    @Test
    public void getSeriesSummaryTest() throws AssertionError {

        String homelandSummary = "<p>The winner of 6 Emmy Awards including Outstanding Drama Series, <b>Homeland</b> is an edge-of-your-seat sensation. Marine Sergeant Nicholas Brody is both a decorated hero and a serious threat. CIA officer Carrie Mathison is tops in her field despite being bipolar. The delicate dance these two complex characters perform, built on lies, suspicion, and desire, is at the heart of this gripping, emotional thriller in which nothing short of the fate of our nation is at stake.</p>";
        assertEquals(homelandSummary, HOMELAND.getSeriesSummary());

    }

    @Test
    public void getNullSeriesSummaryTest() throws AssertionError {

        assertNull(CONTROL_SERIES.getSeriesSummary());

    }

    @Test
    public void getEpisodeSummaryTest() throws AssertionError {

        String homelandS1E1Summary = "<p>In the opener of this \"Manchurian Candidate\"-like political thriller, a marine rescued after eight years as a POW in Afghanistan returns home a war hero. But a CIA operative suspects he may actually be an enemy agent with a connection to Al Qaeda and part of a plan to commit a terrorist act on U.S. soil.</p>";
        assertEquals(homelandS1E1Summary, HOMELAND.getEpisode(1, 1).getSummary());

    }

    @Test
    public void getEpisodeNullSummary() throws AssertionError {

        TVSeries.TVEpisode noSummaryEpisode = CONTROL_SERIES.createNewEpisode();
        assertNull(noSummaryEpisode.getSummary());

    }

    @Test
    public void getEpisodeTest() throws AssertionError {

        //Creating equivalent episode for comparison
        TVSeries.TVEpisode homelandS7E12 = HOMELAND.createNewEpisode();
        homelandS7E12.setName("Paean to the People");
        homelandS7E12.setSummary("<p>Carrie and Saul's mission doesn't go as planned. Elizabeth Keane fights for her presidency.</p>");
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
    public void getEpisodesInSeasonTest() throws AssertionError {

        TVSeries.TVEpisode[] gotSeason1 = new TVSeries.TVEpisode[10];
        for (int episode = 1; episode <= gotSeason1.length; episode++) {
            gotSeason1[episode - 1] = GAME_OF_THRONES.getEpisode(1, episode);
        }

        assertArrayEquals(gotSeason1, GAME_OF_THRONES.getEpisodesInSeason(1));

    }

    @Test
    public void getEpisodesInNullSeasonTest() throws AssertionError {

        boolean exceptionThrown = false;
        try {
            GAME_OF_THRONES.getEpisodesInSeason(9);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue("Season doesn't exist", exceptionThrown);

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

        TVSeries.TVEpisode noNameEpisode = CONTROL_SERIES.createNewEpisode();

        assertNull(noNameEpisode.getEpisodeName());

    }

    @Test
    public void getEpisodeAirDateTest() throws AssertionError {

        assertEquals("2018-04-29", HOMELAND.getEpisode(7, 12).getAirdate());

    }

    @Test
    public void getNullAirDateTest() throws AssertionError {

        TVSeries.TVEpisode nullAirDate = CONTROL_SERIES.createNewEpisode();
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

        GAME_OF_THRONES.addNewEpisode(gotS8E1);
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

        GAME_OF_THRONES.addNewEpisode(gotS8E2);
        assertEquals(gotS8E2, GAME_OF_THRONES.getEpisode(8, 2));

    }

    @Test
    public void getEpisodeSeasonTest() throws AssertionError {

        //Test for middle values
        assertEquals(3, GAME_OF_THRONES.getEpisode(3, 2).getSeason());

    }

    @Test
    public void getEpisodeNullSeasonTest() throws AssertionError {

        TVSeries.TVEpisode emptyEpisode = CONTROL_SERIES.createNewEpisode();
        assertEquals(0, emptyEpisode.getSeason());

    }

    @Test
    public void getEpisodeNumberTest() throws AssertionError {

        assertEquals(7, GAME_OF_THRONES.getEpisode(7, 7).getNumber());

    }

    @Test
    public void getEpisodeNullNumberTest() {

        TVSeries.TVEpisode emptyEpisode = CONTROL_SERIES.createNewEpisode();
        assertEquals(0, emptyEpisode.getNumber());

    }

    @Test
    public void getEpisodeByDateTest() throws AssertionError {

        assertEquals(HOMELAND.getEpisode(1, 12),
                     HOMELAND.getEpisodeOnDate("2011-12-18"));

    }

    @Test
    public void getEpisodeByBadDateTest() throws AssertionError {

        assertNull(GAME_OF_THRONES.getEpisodeOnDate("non-formatted"));

    }

    @Test
    public void getEpisodeOnNullDateTest() throws AssertionError {

        assertNull(GAME_OF_THRONES.getEpisodeOnDate(null));

    }

    @Test
    public void getEpisodesInYearTest() throws AssertionError {

        TVSeries.TVEpisode[] gotEpisodesIn2019 = { GAME_OF_THRONES.getEpisode(8 , 1) };
        assertArrayEquals(gotEpisodesIn2019, GAME_OF_THRONES.getEpisodesInYear(2019));

    }

    @Test
    public void getEpisodesInEmptyYearTest() throws AssertionError {

        assertTrue(GAME_OF_THRONES.getEpisodesInYear(2020).length == 0);

    }

    @Test
    public void getSeasonPremiereEpisodesTest() throws AssertionError {

        TVSeries.TVEpisode[] gotSeasonPremieres = new TVSeries.TVEpisode[GAME_OF_THRONES.getEpisodes().length];

        for (int season = 0; season < gotSeasonPremieres.length; season++) {
            gotSeasonPremieres[season] = GAME_OF_THRONES.getEpisode(season + 1, 1);
        }

        assertArrayEquals(gotSeasonPremieres, GAME_OF_THRONES.getSeasonPremiereEpisodes());

    }

    @Test
    public void getNullSeriesPremiereEpisodesTest() throws AssertionError {

        assertNull(CONTROL_SERIES.getSeasonPremiereEpisodes());

    }

}
