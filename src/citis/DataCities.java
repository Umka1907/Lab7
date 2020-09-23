package citis;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * City Collection Class
 */

@XmlType(name = "cities")
@XmlRootElement(name="DataCities")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({City.class})
public class DataCities  {

    /**
     * Fields for collection initialization date
     */

    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    public LocalDateTime initializationTime;

    public LinkedHashSet<City> cities;
    public static Set<City> syncities ;


    public DataCities() {
        cities = new LinkedHashSet<>();
        initializationTime = LocalDateTime.now();
        syncities = Collections.synchronizedSet(cities);
    }

    @XmlElement(name = "cities", nillable = true)
    public LinkedHashSet<City> getQueue() {
        return cities;
    }

    public void setQueue(LinkedHashSet<City> queue) {
        this.cities = cities;
    }

    public void setInitializationTime(LocalDateTime initializationTime) {
        this.initializationTime = initializationTime;
    }




    public City getElementById(Integer id){
            for (City city : syncities) {
                if (city.getId() == id) {
                    return city;
                }
            }
        return null;
    }

    public Integer getMaxId() {

        Integer maxId = 0;
        for (City city : syncities) {
            if (city.getId() > maxId) {
                maxId = city.getId();
            }
        }
        return maxId;
    }


    public Integer getIdMaxMetersAboveSeaLevel(DataCities dataCities){
        Long MaxMetersAboveSeaLevel = dataCities.getElementById(1).getMetersAboveSeaLevel();
        Integer IdMaxMetersAboveSeaLevel = 1;
        for (City city : syncities) {
            if (city.getMetersAboveSeaLevel() > MaxMetersAboveSeaLevel) {
                MaxMetersAboveSeaLevel = city.getMetersAboveSeaLevel();
                IdMaxMetersAboveSeaLevel = city.getId();

            }
        }
        return IdMaxMetersAboveSeaLevel;
    }

    public void addElement(City city) {
        this.cities.add(city);
    }

     public void addId(City city){
         city.setId(getMaxId() + 1);
     }

    public void addLocalDate(City city){
        city.setCreationDate(LocalDate.now());
    }

    public void updateElementById(Integer id, City newCity) {
        City updateElement = null;
        for (City city : cities) {
            if (city.getId() == id) {
                updateElement = city;
            }
        }

        updateElement.setName(newCity.getName());
        updateElement.setCoordinates(newCity.getCoordinates());
        updateElement.setArea(newCity.getArea());
        updateElement.setPopulation(newCity.getPopulation());
        updateElement.setMetersAboveSeaLevel(newCity.getMetersAboveSeaLevel());
        updateElement.setTelephoneCode(newCity.getTelephoneCode());
        updateElement.setCreationDate(newCity.getCreationDate());
        updateElement.setAgglomeration(newCity.getAgglomeration());
        updateElement.setGovernment(newCity.getGovernment());
        updateElement.setGovernor(newCity.getGovernor());


    }


    public void removeElementById(Integer id) {
        City cityRemove = null;
        for (City city : syncities) {
            if (city.getId() == id) {
                cityRemove = city;
            }
        }
        cities.remove(cityRemove);
    }

    public void clearCollection() {
        syncities.clear();
    }


    public ArrayList<Integer> arrayListId() {
        ArrayList<Integer> arrayId = new ArrayList<>();
        for (City city : syncities) {
            arrayId.add(city.getId());
        }
        return  arrayId;
    }

    public Boolean ifMinValues( City newElement) {
        String minValue = newElement.getName();
        for(City city: syncities){
            if(city.compareTo(newElement)>0){
                minValue = city.getName();
            }
        }
        if (minValue == newElement.getName()){
            return true;
        } else {return false;}
    }


    public LinkedHashSet<City> removeGreater(City city){
        LinkedHashSet<City> removeElements = new LinkedHashSet<>();
        for (City city1: syncities){
            if(city1.compareTo(city)>0){
                removeElements.add(city1);
            }
        }
        if(!removeElements.isEmpty()) {
            for (City city1 : removeElements) {
                syncities.remove(city1);
            }
        } else {
            System.out.println(" The collection does not contain elements with values less than the specified");
        }
        return removeElements;
    }

    public LinkedHashSet<City> filterName(String name){
        LinkedHashSet<City> names = new LinkedHashSet<>();
        for (City city: syncities){
            if(city.getName().contains(name)){
                names.add(city);
            }
        }
        return names;
    }





    public String toString() {
        return "Информация о коллекции: " +'\n'+
                "Тип: "+ cities.getClass().getName()+'\n'+
                "Дата инициализации: " + initializationTime +'\n'+
                "Количество элементов: " + cities.size();

    }
}
