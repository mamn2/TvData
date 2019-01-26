/**
 * This class stores information about a TV show.
 * It allows a user to get information about a TV show using a variety of defined functions.
 */
public class TVSeries {

    private String name;
    private String language;
    private String[] genres;
    //private String[] schedule;
    //private double rating;
    //private String network;
    private String summary;
    private TVEpisode[] episodes;

    public String getLanguage() {
        return this.language;
    }

    public String[] getGenres() {
        return this.genres;
    }

    public String getSummary() {
        return this.summary;
    }

    public TVEpisode[] getEpisodes() {
        return this.episodes;
    }

    public String getName() {
        return this.name;
    }

}
