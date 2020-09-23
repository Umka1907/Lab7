package Commands;

import citis.City;
import citis.CompareCoordinates;
import citis.DataCities;

import java.util.Set;
import java.util.TreeSet;

public class ShowCommand implements Command {
    public ShowCommand(){CommandExecutor.addCommand("show", this);}
    String answer = "" ;
    CompareCoordinates compareCoordinates = new CompareCoordinates();
    public Set<City> sorted = new TreeSet( compareCoordinates);

    @Override
    public void execute(String str, DataCities data) {
        if(data.syncities.size() != 0 ) {

            for (City city: data.syncities){
                sorted.add(city);

            }
            for (City city: sorted){
                answer = answer + city.toString() +'\n';
            }

        } else {
            answer = ("Коллекция пуста.");
        }

    }
    @Override
    public String getAnswer() {
        return answer;
    }
}
