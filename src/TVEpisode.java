/**
 * This class holds information about a single episode within a TV series.
 */

public class TVEpisode extends TVSeries {

    //private String name;
    private int season;
    private int number;
    private String airdate;
    //private String summary;

    //@Override
    //public String getName() {
    //    return name;
    //}

    public int getSeason() {
        return season;
    }

    public int getNumber() {
        return number;
    }

    public String getAirdate() {
        return airdate;
    }

    //public String getSummary() {
     //   return summary;
    //}

}
