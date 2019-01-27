import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class TVSeriesTest {

    //Loads JSON data about the TV Series into form of a TVSeries object. Info retrieved from "Data" folder.
    private static final TVSeries GAME_OF_THRONES = deserializeData("GameOfThrones");
    private static final TVSeries HOMELAND = deserializeData("Homeland");

    public static TVSeries deserializeData(String tvShow) {

        JsonObject tvShowData = new JsonParser().parse(Data.getFileContentsAsString(tvShow)).getAsJsonObject();

        TVSeries tvSeries = new TVSeries();

        tvSeries.setName(tvShowData.get("name").getAsString());

        tvSeries.setAverageRating(tvShowData.get("rating").getAsJsonObject().get("average").getAsInt());

        tvSeries.setLanguage(tvShowData.get("language").getAsString());

        tvSeries.setNetwork(tvShowData.get("network").getAsJsonObject().get("name").getAsString());

        tvSeries.setPremiered(tvShowData.get("premiered").getAsString());

        tvSeries.setSummary(tvShowData.get("summary").getAsString());

        //Since genres is a JsonArray, we loop through it to convert to a java array
        String[] genres = new String[tvShowData.get("genres").getAsJsonArray().size()];
        for (int i = 0; i < genres.length; i++) {
            genres[i] = tvShowData.get("genres").getAsJsonArray().get(i).getAsString();
        }
        tvSeries.setGenres(genres);

        TVSeries.TVEpisode[] episodes = new TVSeries.TVEpisode[tvShowData.get("_embedded").getAsJsonObject()
                                                                         .get("episodes").getAsJsonArray().size()];
        for (int i = 0; i < episodes.length; i++) {
            Gson gson = new Gson();
            episodes[i] = gson.fromJson(tvShowData.get("_embedded").getAsJsonObject()
                                                  .get("episodes").getAsJsonArray().get(i), TVSeries.TVEpisode.class);
        }
        tvSeries.setEpisodes(episodes);

        return tvSeries;

    }


    @Test
    public void getSeriesNameTest() {
        assertEquals("Game of Thrones", GAME_OF_THRONES.getName());
        assertEquals("Homeland", HOMELAND.getName());
    }

    @Test
    public void getGenreTest() {
        //Ensures that one of the contents of the genres array is "Drama"
        assertTrue(Arrays.asList(GAME_OF_THRONES.getGenres()).contains("Drama"));
        assertTrue(Arrays.asList(HOMELAND.getGenres()).contains("Drama") );
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
        //assertEquals("Pilot", HOMELAND.getEpisodes().getEpisodes().get(1).getName());
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
