import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import javax.management.DescriptorAccess;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class TVSeriesTest {

    //Gson object is created for loading the JSON data into a String.
    private static Gson loadData = new Gson();

    //Contains information about the TV Series in form of a TVSeries object.
    //Information is retrieved from files in the Data folder
    private static final TVSeries GAME_OF_THRONES = loadData
            .fromJson(Data.getFileContentsAsString("GameOfThronesJSON"), TVSeries.class);
    private static final TVSeries HOMELAND = loadData
            .fromJson(Data.getFileContentsAsString("HomelandJSON"), TVSeries.class);


    @Test
    public void getSeriesNameTest() {
        assertEquals("Game of Thrones", GAME_OF_THRONES.getName());
        assertEquals("Homeland", HOMELAND.getName());
    }

    @Test
    public void getGenreTest() {
        //Ensures that one of the contents of the genres array is "Drama"
        assertTrue("Drama", Arrays.asList(GAME_OF_THRONES.getGenres()).contains("Drama"));
    }

    @Test
    public void getGenreArrayTest() {
        assertArrayEquals(new String[] { "Drama", "Thriller", "Espionage" }, HOMELAND.getGenres());
    }

    @Test
    public void getPremiereTest() {

    }

    @Test
    public void getScheduleTest() {

    }

    @Test
    public void getRatingTest() {

    }

    @Test
    public void getNetworkTest() {

    }

    @Test
    public void getEpisodeTest() {
        //assertEquals();
    }

    @Test
    public void getEpisodeNameTest() {
        //assertEquals("Pilot", HOMELAND.getEpisodes()[0].getName());
    }

    @Test
    public void getEpisodeAirDate() {

    }

    @Test
    public void getEpisodeSeason() {

    }

    @Test
    public void getEpisodeNumber() {

    }

    @Test
    public void getEpisodeSeriesName() {

    }



}
