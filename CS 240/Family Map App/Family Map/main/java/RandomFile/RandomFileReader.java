package RandomFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;

/**
 * read file and get random name or location by using gson
 */
public class RandomFileReader {
    /**
     * get random female name from json file
     * @return
     */
    public String getRandomFemaleName() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonReader jsonReader = null;

        try {
            //set file path and read file
            String femaleFilePath = "familymapserver/json/fnames.json";
            jsonReader = new JsonReader(new FileReader(femaleFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //use gson to read json file
        RandomName randomFemaleNames = gson.fromJson(jsonReader, RandomName.class);

        //pick one random female name
        return randomFemaleNames.getNames().get((int) (Math.random()* randomFemaleNames.getNames().size()));
    }

    /**
     * get random male name from json file
     * @return
     */
    public String getRandomMaleName() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonReader jsonReader = null;

        try {
            //set file path and read file
            String maleFilePath = "familymapserver/json/mnames.json";
            jsonReader = new JsonReader(new FileReader(maleFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //use gson to read json file
        RandomName randomMaleNames = gson.fromJson(jsonReader, RandomName.class);

        //pick one random male name
        return randomMaleNames.getNames().get((int) (Math.random()* randomMaleNames.getNames().size()));
    }

    /**
     * get random surname from json file
     * @return
     */
    public String getRandomSurname() {
        Gson gson = new Gson();
        JsonReader jsonReader = null;

        try {
            //set file path and read file
            String surnameFilePath = "familymapserver/json/snames.json";
            jsonReader = new JsonReader(new FileReader(surnameFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //use gson to read json file
        RandomName randomSurnames = gson.fromJson(jsonReader, RandomName.class);

        //pick one random surname
        return randomSurnames.getNames().get((int) (Math.random()* randomSurnames.getNames().size()));
    }

    /**
     * get random location from json file
     * @return Location
     */
    public RandomLocation.Location getRandomLocation() {
        Gson gson = new Gson();
        JsonReader jsonReader = null;

        try {
            //set file path and read file
            String locationFilePath = "familymapserver/json/locations.json";
            jsonReader = new JsonReader(new FileReader(locationFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //use gson to read json file
        RandomLocation randomLocation = gson.fromJson(jsonReader, RandomLocation.class);

        //pick one random location
        return randomLocation.data.get((int) (Math.random()* randomLocation.data.size()));
    }
}
