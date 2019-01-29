import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * This class stores information about a TV show.
 * It allows a user to get information about a TV show using a variety of defined functions.
 */
public class TVSeries {

    private String seriesName;
    private String language;
    private String[] genres;
    private String premiereDate;
    private double averageRating;
    private String networkName;
    private String seriesSummary;
    private TVEpisode[][] episodes;

    public TVSeries(JsonObject tvShowJSON) throws NullPointerException {

        //Initializing fields from information found in JSONObject
        this.setSeriesName(tvShowJSON.get("name").getAsString());
        this.setLanguage(tvShowJSON.get("language").getAsString());
        this.setSeriesSummary(tvShowJSON.get("summary").getAsString());
        this.setAverageRating(tvShowJSON.get("rating").getAsJsonObject().get("average").getAsDouble());
        this.setNetworkName(tvShowJSON.get("network").getAsJsonObject().get("name").getAsString());
        this.setPremiereDate(tvShowJSON.get("premiered").getAsString());

        //Since the genres are stored in a JsonArray, loop through it to convert to a java array
        String[] genres = new String[tvShowJSON.get("genres").getAsJsonArray().size()];
        for (int i = 0; i < genres.length; i++) {
            genres[i] = tvShowJSON.get("genres").getAsJsonArray().get(i).getAsString();
        }
        this.setGenres(genres);

        //Since the episodes are stored in a JsonArray, we loop through it to convert it to a java array
        TVEpisode[] episodes = new TVEpisode[tvShowJSON.get("_embedded").getAsJsonObject()
                .get("episodes").getAsJsonArray().size()];
        for (int i = 0; i < episodes.length; i++) {
            episodes[i] = new Gson().fromJson(tvShowJSON.get("_embedded").getAsJsonObject()
                            .get("episodes").getAsJsonArray().get(i),
                    TVEpisode.class);
        }
        this.setEpisodes(episodes);

    }

    public String getSeriesName() {
        return seriesName;
    }

    public String getLanguage() {
        return language;
    }

    public String[] getGenres() {
        return genres;
    }

    public String getPremiereDate() {
        return premiereDate;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public String getNetworkName() {
        return networkName;
    }

    public String getSeriesSummary() {
        return seriesSummary;
    }

    public TVEpisode getEpisode(int seasonNum, int episodeNum) throws IllegalArgumentException {
        if (seasonNum > episodes.length || episodeNum > episodes[seasonNum - 1].length) {
            throw new IllegalArgumentException();
        }
        return episodes[seasonNum - 1][episodeNum - 1];
    }

    public TVEpisode[][] getEpisodes() {
        return episodes;
    }

    public TVEpisode getEpisodeOnDate(String date) {

        for (TVEpisode[] season : episodes) {
            for (TVEpisode episode : season) {
                if (episode != null && episode.getAirdate().equals(date)) {
                    return episode;
                }
            }
        }
        return null;

    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public void setPremiereDate(String premiereDate) {
        this.premiereDate = premiereDate;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public void setSeriesSummary(String seriesSummary) {
        this.seriesSummary = seriesSummary;
    }

    /**
     * This functions converts the array of episodes into a 2D array for better readability
     * @param episodes is all the episodes in a show in the form of a 1D array
     */
    public void setEpisodes(TVEpisode[] episodes) throws NullPointerException {

        if (episodes != null) {
            this.episodes = null;

            //calculates how many seasons there are by going through the episodes and checking their season number
            int numSeasons = 1;
            for (TVEpisode episode: episodes) {
                if (episode == null) {
                    throw new NullPointerException("Tried to set null episode");
                } else if (episode.getSeason() > numSeasons) {
                    numSeasons = episode.getSeason();
                }
            }

            //for loop calculates the max episodes in each season to ensure 2d array doesn't contain null episodes
            int maxEpisodesInEachSeason[] = new int[numSeasons]; //stores the max number of episodes in each season
            for (TVEpisode episode : episodes) {
                if (episode.getNumber() > maxEpisodesInEachSeason[episode.getSeason() - 1]) {
                    //Changes the number of episodes if the current episode is greater than the length of the season
                    maxEpisodesInEachSeason[episode.getSeason() - 1] = episode.getNumber();
                }
            }

            //array is jagged because different seasons may have different lengths.
            //Each season should only have a certain amount of episodes.
            TVEpisode[][] organizedEpisodeArray = new TVEpisode[numSeasons][];
            for (int season = 0; season < numSeasons; season++) {
                organizedEpisodeArray[season] = new TVEpisode[maxEpisodesInEachSeason[season]];
            }

            //Initialize and set each episode of the new 2D array
            for (TVEpisode episode: episodes) {
                organizedEpisodeArray[episode.getSeason() - 1 ][episode.getNumber() - 1] = episode;
            }

            this.episodes = organizedEpisodeArray;

        } else {
            this.episodes = null;
        }

    }

    /**
     * Creates a new TVEpisode for an instance of TVSeries.
     * Note that this function does not affect the list of episodes in the series. addNewEpisodes() does.
     * @return a new TVEpisode of the given instance.
     */
    public TVEpisode createNewEpisode() {
        return new TVEpisode();
    }

    /**
     * This function will add a new episode to the TVSeries episodes array.
     * If the episode already exists it will be replaced by the newEpisode
     * @param newEpisode contains a custom made episode
     */
    public void addNewEpisode(TVEpisode newEpisode) {

        //Copy current episodes to a list
        LinkedList<TVEpisode> currentEpisodeList = new LinkedList<>();
        for (TVEpisode[] season : episodes) {
            Collections.addAll(currentEpisodeList, season);
        }

        //Add the new episode to the list
        currentEpisodeList.add(newEpisode);

        //Uses the setEpisodes function to reinitialize episodes array
        TVEpisode[] newEpisodeList = currentEpisodeList.toArray(new TVEpisode[currentEpisodeList.size()]);
        this.setEpisodes(newEpisodeList);

    }

    public TVEpisode[] getEpisodesInYear(int year) {

        LinkedList<TVEpisode> episodesInYear = new LinkedList<>();

        for (TVEpisode[] season : episodes) {
            for (TVEpisode episode : season) {
                if (episode.getAirdate().substring(0, 4).equals(Integer.toString(year))) {
                    episodesInYear.add(episode);
                }
            }
        }

        return episodesInYear.toArray(new TVEpisode[episodesInYear.size()]);

    }

    public TVEpisode[] getSeasonPremiereEpisodes() {

        TVEpisode[] seasonPremiereEpisodes = new TVEpisode[episodes.length];
        for (int i = 0; i < seasonPremiereEpisodes.length; i++) {
            seasonPremiereEpisodes[i] = episodes[i][0];
        }

        return seasonPremiereEpisodes;

    }

    /**
     * This class represents the data stored in a TVEpisode within an instance of TVSeries.
     */
    public class TVEpisode {

        private String name;
        private int season;
        private int number;
        private String airdate;
        private String summary;

        public String getEpisodeName() {
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

        public void setName(String name) {
            this.name = name;
        }

        public void setSeason(int season) {
            this.season = season;
        }

        public void setNumber(int number) {
            if (this.getSeason() != 0) {
                this.number = number;
            }
        }

        public void setAirdate(String airdate) {
            this.airdate = airdate;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        @Override
        public boolean equals(final Object episode) {

            //A TVEpisode cannot be equal to another object that is not a TVEpisode
            if (!(episode instanceof TVEpisode)) {
                return false;
            }

            TVEpisode otherEpisode = (TVEpisode) episode;

            //Ensures that all fields of the episode are equal to each other.
            return this.name.equals(otherEpisode.getEpisodeName())
                    && this.season == otherEpisode.getSeason()
                    && this.number == otherEpisode.getNumber()
                    && this.airdate.equals(otherEpisode.getAirdate())
                    && this.summary.equals(otherEpisode.getSummary());

        }

    }

}
