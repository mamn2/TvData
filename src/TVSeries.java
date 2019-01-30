import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * This class stores information about a TV show.
 * It allows a user to get information about a TV show using a variety of defined functions.
 */
public class TVSeries {

    //Name of TV series, e.g. Game of Thrones
    private String seriesName;

    //Primary language, e.g. English
    private String language;

    //All genres, e.g {Action, Fantasy, Thriller}
    private String[] genres;

    //Series premiere date in from YYYY-MM-DD, e.g. 2011-04-13
    private String premiereDate;

    //Average rating of the series, e.g. 9.5
    private double averageRating;

    //Name of primary network, e.g. HBO
    private String networkName;

    //Summary of series, e.g. "Mohamed the programmer encounters wild debugging in this new hit HBO series"
    private String seriesSummary;

    /* All episodes in series organized by season and episode. Note the array is jagged meaning different seasons
    may have different lengths and season/episodes start at 0 instead of 1 */
    private TVEpisode[][] episodes;

    public TVSeries (String seriesName) {
        this.seriesName = seriesName;
    }

    /**
     * This constructor deserializes a JSON object into a Java TVSeries object.
     * @param tvSeriesJSON is information about a TVSeries in the form of a JSON Object
     * @throws NullPointerException happens when JSONObject doesn't follow the structure that the deserializer does.
     */
    public TVSeries(JsonObject tvSeriesJSON) throws NullPointerException {

        //Initializing fields from information found in JSONObject
        this.setSeriesName(tvSeriesJSON.get("name").getAsString());
        this.setLanguage(tvSeriesJSON.get("language").getAsString());
        this.setSeriesSummary(tvSeriesJSON.get("summary").getAsString());
        this.setAverageRating(tvSeriesJSON.get("rating").getAsJsonObject().get("average").getAsDouble());
        this.setNetworkName(tvSeriesJSON.get("network").getAsJsonObject().get("name").getAsString());
        this.setPremiereDate(tvSeriesJSON.get("premiered").getAsString());

        //Since the genres are stored in a JsonArray, loop through it to convert to a java array
        String[] genres = new String[tvSeriesJSON.get("genres").getAsJsonArray().size()];
        for (int i = 0; i < genres.length; i++) {
            genres[i] = tvSeriesJSON.get("genres").getAsJsonArray().get(i).getAsString();
        }
        this.setGenres(genres);

        //Since the episodes are stored in a JsonArray, we loop through it to convert it to a java array
        JsonArray episodeElements = tvSeriesJSON.get("_embedded").getAsJsonObject()
                                                .get("episodes").getAsJsonArray();
        TVEpisode[] episodes = new TVEpisode[episodeElements.size()];
        for (int i = 0; i < episodes.length; i++) {
            episodes[i] = new Gson().fromJson(episodeElements.get(i), TVEpisode.class);
            episodes[i].setRuntimeInMinutes(episodeElements.get(i).getAsJsonObject().get("runtime").getAsInt());

            //Avoids error by placing in try catch block since sometimes the JSON is null
            JsonElement episodeSummary = episodeElements.get(i).getAsJsonObject().get("summary");
            try {
                episodes[i].setSummary(episodeSummary.getAsString());
            } catch (UnsupportedOperationException e) {
                System.out.println(e);
            }
        }
        this.setEpisodes(episodes);

    }

    /**
     * Getter for series name instance variable.
     * @return name of the series.
     */
    public String getSeriesName() {
        return seriesName;
    }

    /**
     * Getter for language instance variable.
     * @return primary language of the series.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Getter for genres instance variable.
     * @return an array of all the genres in the show.
     */
    public String[] getGenres() {
        return genres;
    }

    /**
     * Getter for premiereDate instance variable.
     * @return the premiere date of the series in the following format: YYYY-MM-DD.
     */
    public String getPremiereDate() {
        return premiereDate;
    }

    /**
     * Getter for average rating instance variable.
     * @return average rating for the series as a whole.
     */
    public double getAverageRating() {
        return averageRating;
    }

    /**
     * Getter for networkName instance variable.
     * @return primary network name of the series.
     */
    public String getNetworkName() {
        return networkName;
    }

    /**
     * Getter for seriesSummary instance variable.
     * @return a summary of the series as found on DVD cover.
     */
    public String getSeriesSummary() {
        return seriesSummary;
    }

    /**
     * Getter for single episode in episodes array.
     * @param seasonNum is the season number for the episode being searched.
     * @param episodeNum is the episode number for the episode being searched.
     * @return a TVEpisode that matches the season and episode number provided.
     * @throws IllegalArgumentException if the season/episode combo does not exist.
     */
    public TVEpisode getEpisode(int seasonNum, int episodeNum) throws IllegalArgumentException {

        try {
            return episodes[seasonNum - 1][episodeNum - 1];
        } catch (Exception e) {
            throw new IllegalArgumentException("Season/Episode number does not exist");
        }

    }

    /**
     * Getter for episodes instance variable.
     * @return All episodes in the series in 2D array. First dimension represents seasons, second is episode number.
     */
    public TVEpisode[][] getEpisodes() {
        return episodes;
    }

    /**
     * Setter for series name.
     * @param seriesName is whatever you want your series name to be.
     */
    public void setSeriesName(final String seriesName) {
        this.seriesName = seriesName;
    }

    /**
     * Setter for language.
     * @param language is whatever the primary language of the TVSeries is.
     */
    public void setLanguage(final String language) {
        this.language = language;
    }

    /**
     * Setter for genre.
     * @param genres is whatever genres the TVSeries belongs.
     */
    public void setGenres(final String[] genres) {
        this.genres = genres;
    }

    /**
     * Setter for premiere date.
     * @param premiereDate is whenever the premiere date of the series is in the form YYYY-MM-DD.
     */
    public void setPremiereDate(final String premiereDate) {
        this.premiereDate = premiereDate;
    }

    /**
     * Setter for average rating.
     * @param averageRating the average rating of the show as a whole.
     */
    public void setAverageRating(final double averageRating) {
        this.averageRating = averageRating;
    }

    /**
     * Setter for network name of the TVSeries.
     * @param networkName is the name of the primary network that the TVSeries is featured on.
     */
    public void setNetworkName(final String networkName) {
        this.networkName = networkName;
    }

    /**
     * Setter for summary of the TVSeries.
     * @param seriesSummary is the summary of the TVSeries, usually as found on imDB.com
     */
    public void setSeriesSummary(final String seriesSummary) {
        if (seriesSummary != null) {
            this.seriesSummary = seriesSummary.replaceAll("(<[a-z]>)|(</[a-z]>)", "");
        } else {
            this.seriesSummary = null;
        }
    }

    /**
     * Setter for episodes instance variable.
     * This functions converts the array of episodes into a 2D array for better readability and sets it to
     * episodes instance variable.
     * @param episodes is all the episodes in a show in the form of a 1D array.
     * @throws NullPointerException if one of the episodes in the array is null
     */
    public void setEpisodes(final TVEpisode[] episodes) throws NullPointerException {

        if (episodes != null) {

            //calculates how many seasons there are by going through the episodes and checking their season number
            int numSeasons = 0;
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
     * Note that this function does not affect the list of episodes in the series. However, addOrReplaceEpisode does.
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
    public void addOrReplaceEpisode(final TVEpisode newEpisode) {

        //All new episodes stored in this list.
        LinkedList<TVEpisode> newEpisodeList = new LinkedList<>();

        //Copy current episodes to a list.
        if (this.episodes != null && this.episodes.length != 0) {
            for (TVEpisode[] season : episodes) {
                Collections.addAll(newEpisodeList, season);
            }
        }

        //Add the new episode to the list.
        newEpisodeList.add(newEpisode);

        //Uses the setEpisodes function to reinitialize episodes array
        this.setEpisodes(newEpisodeList.toArray(new TVEpisode[newEpisodeList.size()]));

    }

    /**
     * Filter method to search for all episodes in 2d array that premiered in a given year.
     * @param year is the year being searched for episodes.
     * @return all the episodes from that series in the form of an array.
     */
    public static TVEpisode[] getEpisodesInYear(final TVEpisode[][] allEpisodes, final int year) {
        return getEpisodesInYear(convert2DEpisodesto1D(allEpisodes), year);
    }

    /**
     * Filter method to search for all episodes from 1d array that premiered in a given year.
     * @param year is the year being searched for episodes.
     * @return all the episodes from that series in the form of an array.
     */
    public static TVEpisode[] getEpisodesInYear(final TVEpisode[] episodes, final int year) {

        LinkedList<TVEpisode> episodesInYear = new LinkedList<>();

        for (TVEpisode episode : episodes) {
            if (episode.getAirdate().substring(0, 4).equals(Integer.toString(year))) {
                episodesInYear.add(episode);
            }
        }

        return episodesInYear.toArray(new TVEpisode[episodesInYear.size()]);

    }

    /**
     * Searches through all the episodes in a 2d array and returns episodes containing the name.
     * @param allEpisodes is all the episodes being searched
     * @param name is the search name criteria
     * @return all episodes with the name in it
     */
    public static TVEpisode[] searchEpisodesByName(final TVEpisode[][] allEpisodes, final String name) {
        return searchEpisodesByName(convert2DEpisodesto1D(allEpisodes), name);
    }

    /**
     * Searches through all the episodes in a 1d array and returns episodes containing the name.
     * @param episodes is all the episodes being searched
     * @param name is the search name criteria
     * @return all episodes with the name in it
     */
    public static TVEpisode[] searchEpisodesByName(final TVEpisode[] episodes, final String name) {

        if (episodes != null && name != null) {
            LinkedList<TVEpisode> allEpisodesWithName = new LinkedList<>();

            for (TVEpisode episode: episodes) {
                if (episode.getEpisodeName().toUpperCase().contains(name.toUpperCase())) {
                    allEpisodesWithName.add(episode);
                }
            }
            return allEpisodesWithName.toArray(new TVEpisode[allEpisodesWithName.size()]);
        } else {
            return null;
        }

    }

    /**
     * Function to search for all episodes under or equaling a certain runtime. Calls another method to do this.
     * @param allEpisodes includes all the episodes in a series or just a 2d array of episodes.
     * @param maxRuntimeMinutes is the maximum runtime the user permitted.
     * @return an array that includes the all episodes up to and including max runtime.
     */
    public static TVEpisode[] searchEpisodesByMaxRuntime(final TVEpisode[][] allEpisodes, int maxRuntimeMinutes) {

        return searchEpisodesByMaxRuntime(convert2DEpisodesto1D(allEpisodes), maxRuntimeMinutes);

    }

    /**
     * Function to search for all episodes under or equaling a certain runtime.
     * @param allEpisodes is all the episodes being searched for
     * @param maxRuntimeMinutes is the max runtime as set by the caller of the function.
     * @return all episodes under or equaling a specified runtime.
     */
    public static TVEpisode[] searchEpisodesByMaxRuntime(final TVEpisode[] allEpisodes, int maxRuntimeMinutes) {

        LinkedList<TVEpisode> allEpisodesUnderMaxRuntime = new LinkedList<>();

        if (allEpisodes != null) {
            for (TVEpisode episode : allEpisodes) {
                if (episode != null && episode.getRuntimeInMinutes() <= maxRuntimeMinutes) {
                    allEpisodesUnderMaxRuntime.add(episode);
                }
            }
        }

        return allEpisodesUnderMaxRuntime.toArray(new TVEpisode[allEpisodesUnderMaxRuntime.size()]);

    }

    public static TVEpisode[] getEpisodesOnDate(TVEpisode[][] unsortedEpisodes, final String date) {
        return getEpisodesOnDate(convert2DEpisodesto1D(unsortedEpisodes), date);
    }

    /**
     * Filter function to search for an episode on a given date.
     * @param unsortedEpisodes is all the episodes being searched
     * @param date is the date of the episode in the format: YYYY-MM-DD
     * @return an array of episodes on that date in that instance of TVSeries.
     */
    public static TVEpisode[] getEpisodesOnDate(TVEpisode[] unsortedEpisodes, final String date) {

        LinkedList<TVEpisode> allEpisodesOnDate = new LinkedList<>();
        if (unsortedEpisodes != null) {
            for (TVEpisode episode : unsortedEpisodes) {
                if (episode != null && episode.getAirdate() != null && episode.getAirdate().equals(date)) {
                    allEpisodesOnDate.add(episode);
                }
            }
        }

        return allEpisodesOnDate.toArray(new TVEpisode[allEpisodesOnDate.size()]);

    }

    /**
     * Filter function for list of characters.
     * Method is not completely accurate and is based on the summaries of the episode.
     * @param unsortedEpisodes 2d array of all unsorted episodes.
     * @param character the name of the character you are looking for.
     * @return all the episodes that contain the given character.
     */
    public static TVEpisode[] searchEpisodesByCharacter(final TVEpisode[][] unsortedEpisodes, final String character) {
        return searchEpisodesByCharacter(convert2DEpisodesto1D(unsortedEpisodes), character);
    }

    /**
     * Filter function for list of characters.
     * Method is not completely accurate and is based on the summaries of the episode.
     * @param unsortedEpisodes 1d array of all unsorted episodes.
     * @param character the name of the character you are looking for.
     * @return all the episodes that contain the given character.
     */
    public static TVEpisode[] searchEpisodesByCharacter(final TVEpisode[] unsortedEpisodes, final String character) {

        if (character == null || character.length() == 0) {
            return new TVEpisode[0];
        }

        LinkedList<TVEpisode> episodesWithCharacter = new LinkedList<>();

        for (TVEpisode episode : unsortedEpisodes) {
            if (episode != null && episode.getSummary().toUpperCase().contains(character.toUpperCase())) {
                episodesWithCharacter.add(episode);
            }
        }

        return episodesWithCharacter.toArray(new TVEpisode[episodesWithCharacter.size()]);

    }

    /**
     * Calculates the total number of episodes given all the episodes in the series.
     * @param allEpisodes is all the episodes in the series or all the episodes being searched.
     * @return the total number of episodes in the series.
     */
    public static int totalNumberOfEpisodes(final TVEpisode[][] allEpisodes) {

        if (allEpisodes == null) {
            return 0;
        }

        int totalNumEpisodes = 0;
        for (TVEpisode[] season : allEpisodes) {
            totalNumEpisodes += season.length;
        }

        return totalNumEpisodes;

    }

    /**
     * Calculates the average runtime of episodes in a given 2d array by calling helper method
     * @param episodes all the episodes that are being calculated
     * @return the average runtime of all the episodes
     */
    public static int averageRuntimeOfEpisodes(final TVEpisode[][] episodes) {
        return averageRuntimeOfEpisodes(convert2DEpisodesto1D(episodes));
    }

    /**
     * Calculates the average runtime of episodes in a given 1d array
     * @param episodes all the episodes that are being calculated
     * @return the average runtime of all the episodes
     */
    public static int averageRuntimeOfEpisodes(final TVEpisode[] episodes) {

        if (episodes == null || episodes.length == 0) {
            return 0;
        }

        int totalLengthOfSeries = 0;
        for (TVEpisode episode : episodes) {
            totalLengthOfSeries += episode.getRuntimeInMinutes();
        }

        return totalLengthOfSeries / episodes.length;

    }

    /**
     * Searches for episode with the max runtime.
     * @param episodes is a 2d array of episodes being searched through.
     * @return length in minutes of max runtime of episodes.
     */
    public static int maxRuntimeOfEpisodes(final TVEpisode[][] episodes) {
        return maxRuntimeOfEpisodes(convert2DEpisodesto1D(episodes));
    }

    /**
     * Searches for episode with the max runtime.
     * @param episodes is a 1d array of episodes being searched through.
     * @return length in minutes of max runtime of episodes.
     */
    public static int maxRuntimeOfEpisodes(final TVEpisode[] episodes) {

        if (episodes == null || episodes.length == 0) {
            return 0;
        }

        int currentMaxRuntime = 0;
        for (TVEpisode episode: episodes) {
            currentMaxRuntime = episode.getRuntimeInMinutes();
        }

        return currentMaxRuntime;

    }

    /**
     * Searches for episode with the min runtime.
     * @param episodes is a 2d array of episodes being searched through.
     * @return length in minutes of min runtime of episodes.
     */
    public static int minRuntimeOfEpisodes(final TVEpisode[][] episodes) {
        return minRuntimeOfEpisodes(convert2DEpisodesto1D(episodes));
    }

    /**
     * Searches for episode with the min runtime.
     * @param episodes is a 1d array of episodes being searched through.
     * @return length in minutes of min runtime of episodes.
     */
    public static int minRuntimeOfEpisodes(final TVEpisode[] episodes) {

        if (episodes == null || episodes.length == 0) {
            return 0;
        }

        int currentMinRuntime = maxRuntimeOfEpisodes(episodes);
        for (TVEpisode episode : episodes) {
            currentMinRuntime = episode.getRuntimeInMinutes();
        }

        return currentMinRuntime;

    }

    /**
     * Takes 2d array of episodes and converts it into 1d array of episodes
     * @param episodes is a 2d array of episodes
     * @return a 1d array of episodes containing all the episodes given in the parameter
     */
    public static TVEpisode[] convert2DEpisodesto1D(final TVEpisode[][] episodes) {

        if (episodes == null) {
            return null;
        }

        TVEpisode[] allEpisodes = new TVEpisode[totalNumberOfEpisodes(episodes)];

        int totalEpisodeNum = 0;
        for (int season = 0; season < episodes.length; season++) {
            for (int episode = 0; episode < episodes[season].length; episode++) {
                allEpisodes[totalEpisodeNum] = episodes[season][episode];
                totalEpisodeNum++;
            }
        }

        return allEpisodes;

    }

    /**
     * This class represents the data stored in a TVEpisode within an instance of TVSeries.
     */
    public class TVEpisode {

        //Name of the episode.
        private String name;

        //Which season it belongs to.
        private int season;

        //Which number it is within the season.
        private int number;

        //The date on which the episode first aired.
        private String airdate;

        //A short summary of what happens in the episode.
        private String summary;

        //Runtime in minutes of the episode
        private int runtimeInMinutes;

        /**
         * Getter for name instance variable.
         * @return the name of the episode.
         */
        public String getEpisodeName() {
            return name;
        }

        /**
         * Getter for the season instance variable.
         * @return the season that this episode belongs to.
         */
        public int getSeason() {
            return season;
        }

        /**
         * Getter for the number instance variable.
         * @return the number within the season that this episode belongs to.
         */
        public int getNumber() {
            return number;
        }

        /**
         * Getter for the airdate instance variable
         * @return the date in which this episode aired in the form YYYY-MM-DD
         */
        public String getAirdate() {
            return airdate;
        }

        /**
         * Getter for the summary instance variable.
         * @return a short summary of what happens in the episode.
         */
        public String getSummary() {
            return summary;
        }

        /**
         * Getter for the runtime instance variable.
         * @return total runtime of the episode in minutes
         */
        public int getRuntimeInMinutes() {
            return runtimeInMinutes;
        }

        /**
         * Setter for the name instance variable.
         * @param name the name of the episode.
         */
        public void setName(final String name) {
            this.name = name;
        }

        /**
         * Setter for the season number.
         * @param season is the season that this episode belongs to.
         */
        public void setSeason(final int season) {
            if (season > 0) {
                this.season = season;
            }
        }

        /**
         * Setter for the number instance variable.
         * @param number the episode number within the season that this episode belongs to
         */
        public void setNumber(final int number) {
            if (this.season > 0) {
                this.number = number;
            }
        }

        /**
         * Setter for the airdate instance variable.
         * @param airdate is the date the episode first aired in the form YYYY-MM-DD.
         */
        public void setAirdate(final String airdate) {
            if (airdate.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d")) {
                this.airdate = airdate;
            }
        }

        /**
         * Setter for the summary instance variable.
         * @param summary is a short summary of the episodes contents.
         */
        public void setSummary(final String summary) {
            if (summary != null) {
                this.summary = summary.replaceAll("(<[a-z]>)|(</[a-z]>)", "");
            } else {
                this.summary = null;
            }
        }

        /**
         * Setter for the runtime field.
         * @param runtimeInMinutes is the runtime of the episode in minutes.
         */
        public void setRuntimeInMinutes(final int runtimeInMinutes) {
            this.runtimeInMinutes = runtimeInMinutes;
        }

        /**
         * TVEpisode equality comparison. All fields must be initalized for this to work.
         * @param otherObject is the other episode or object being compared.
         * @return true if they are equal, false if they are not.
         */
        @Override
        public boolean equals(final Object otherObject) {

            //A TVEpisode cannot be equal to another object that is not a TVEpisode
            if (!(otherObject instanceof TVEpisode)) {
                return false;
            }

            TVEpisode otherEpisode = (TVEpisode) otherObject;

            /* Ensures that all fields of the episode are equal to each other.
            Could throw exception if some fields are null in which case it is false. */
            try {
                return this.name.equals(otherEpisode.getEpisodeName())
                    && this.season == otherEpisode.getSeason()
                    && this.number == otherEpisode.getNumber()
                    && this.airdate.equals(otherEpisode.getAirdate())
                    && this.summary.equals(otherEpisode.getSummary())
                    && this.runtimeInMinutes == otherEpisode.getRuntimeInMinutes();
            } catch (Exception e) {
                return false;
            }

        }

    }

}
