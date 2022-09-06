import java.io.Serializable;
import java.util.TreeMap;

public class Planet implements Serializable {
   String name;
   TreeMap<String, Continent> continents = new TreeMap<>();

   Planet () {}

    Planet (String name){
        this.name = name;
    }

    void createContinent (String name) {
       Continent newContinent = new Continent(name);
       continents.put(newContinent.name, newContinent);
    }
}

class Continent{
    String name;
    TreeMap<String, Countries> countries = new TreeMap<>();

    Continent () {};

    Continent (String name){
        this.name = name;
    }

    void createCounty (String name) {
        Countries newCountry = new Countries(name);
        countries.put(newCountry.name, newCountry);
    }
}

class Countries{
    String name;
    long population;

    Countries () {}

    Countries (String name) {
        this.name = name;
        this.population = 0;
    }

    Countries (String name, long population){
        this.name = name;
        this.population = population;
    }
        }

