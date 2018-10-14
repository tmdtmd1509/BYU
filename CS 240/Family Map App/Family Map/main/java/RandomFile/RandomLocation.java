package RandomFile;

import java.util.ArrayList;
import java.util.Random;

/**
 * store location file data from json
 */
public class RandomLocation {
    public ArrayList<Location> data;

    /**
     * store Location data
     */
    public class Location {
        private String country;
        private String city;
        private Double latitude;
        private Double longitude;

        public String getCountry() {
            return country;
        }

        public String getCity() {
            return city;
        }

        public Double getLatitude() {
            return latitude;
        }

        public Double getLongitude() {
            return longitude;
        }
    }
}
