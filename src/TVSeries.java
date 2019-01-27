import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This class stores information about a TV show.
 * It allows a user to get information about a TV show using a variety of defined functions.
 */
public class TVSeries {


    private String name;
    private String language;
    private String[] genres;
    private String premiered;
    private double averageRating;
    private String network;
    private String summary;
    private TVEpisode[] episodes;

    public void setName(String name) {
        this.name = name;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public void setPremiered(String premiered) {
        this.premiered = premiered;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setEpisodes(TVEpisode[] episodes) {
        this.episodes = episodes;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public String[] getGenres() {
        return genres;
    }

    public String getPremiered() {
        return premiered;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public String getNetwork() {
        return network;
    }

    public String getSummary() {
        return summary;
    }

    public TVEpisode[] getEpisodes() {
        return episodes;
    }

    public class TVEpisode {

        private String name;
        private int season;
        private int number;
        private String airdate;
        private String summary;

        public String getName() {
            return name;
        }

        public int getSeason() {
            return season;
        }

        public int getNumber() {
            return number;
        }

        public String getAirdate() {
            return airdate;
        }

        public String getSummary() {
            return summary;
        }
    }
}
