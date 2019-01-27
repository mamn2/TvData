import javafx.scene.control.cell.TreeItemPropertyValueFactory;

/**
 * This class stores information about a TV show.
 * It allows a user to get information about a TV show using a variety of defined functions.
 */
public class TVSeries {

    public String name;
    private String language;
    private String[] genres;
    private String premiereDate;
    private double ratings;
    private String networkName;
    private String summary;
    private TVEpisode[][] episodes;

    public void setName(String name) {
        this.name = name;
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

    public void setRatings(double ratings) {
        this.ratings = ratings;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setEpisodes(TVEpisode[] episodes) {

        if (episodes != null) {
            int numSeasons = episodes[episodes.length - 1].getSeason();

            int maxEpisodesInSeason = 0;
            for (int i = 0; i < episodes.length; i++) {
                if (episodes[i].getNumber() > maxEpisodesInSeason) {
                    maxEpisodesInSeason = episodes[i].getNumber();
                }
            }

            TVEpisode[][] organizedEpisodeArray = new TVEpisode[numSeasons][maxEpisodesInSeason];
            for (TVEpisode episode: episodes) {
                organizedEpisodeArray[episode.getSeason() - 1 ][episode.getNumber() - 1] = episode;
            }
            this.episodes = organizedEpisodeArray;
        }

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

    public String getPremiereDate() {
        return premiereDate;
    }

    public double getRatings() {
        return ratings;
    }

    public String getNetworkName() {
        return networkName;
    }

    public String getSummary() {
        return summary;
    }

    public TVEpisode[][] getEpisodes() {
        return episodes;
    }

    public TVEpisode getEpisode(int seasonNum, int episodeNum) throws ArrayIndexOutOfBoundsException {
        return episodes[seasonNum - 1][episodeNum - 1];
    }

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

        @Override
        public String toString() {
            return "S" + season + "E" + number + name;
        }
    }
}
