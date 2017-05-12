package hr.foi.varazdinevents.api;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hr.foi.varazdinevents.api.responses.CityResponseComplete;
import hr.foi.varazdinevents.models.City;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.util.SharedPrefs;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Antonio Martinović on 30.10.16.
 */

/**
 * Contains all methods for working with cities such as getting all cities and creating new ones
 */
public class CityManager {
    private final SharedPrefs sharedPrefs;
    private final User user;
    private final RestService restService;
    private List<City> cities;

    /**
     * @param user        current user logged in
     * @param restService Retrofit API calls interface
     */
    public CityManager(User user, RestService restService, SharedPrefs sharedPrefs) {
        this.restService = restService;
        this.user = user;
        this.sharedPrefs = sharedPrefs;
        cities = new ArrayList<>();
    }

    /**
     * Gets all cities from API
     *
     * @return list of cities
     */
    public Observable<List<City>> getCities() {
        return Observable.concat(fromMemory(), fromDatabase(), fromNetwork())
                .cache()
                .map(new Func1<List<City>, List<City>>() {
                    @Override
                    public List<City> call(List<City> cities) {
                        toMemory(cities);
                        return CityManager.this.cities;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Loads data from memory
     *
     * @return all cities loaded in memory
     */
    private Observable<List<City>> fromMemory() {
        return Observable.just(cities).doOnNext(new Action1<List<City>>() {
            @Override
            public void call(List<City> cities) {
                Timber.w("Loading from memory...");
            }
        });
    }

    /**
     * Loads event data from database that has the DATE_TO attribute set to lesser than now
     *
     * @return cities from database
     */
    private Observable<List<City>> fromDatabase() {
        return Observable.just(
                Select.from(City.class).list()
        )
                .doOnNext(new Action1<List<City>>() {
                    @Override
                    public void call(List<City> cities) {
                        Timber.w("Loading from database...");
                        toMemory(cities);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Loads all new cities from the API and stores the new ones to the database and the memory
     *
     * @return all recently changed or new cities
     */
    private Observable<List<City>> fromNetwork() {
        return restService.getCities()
                .map(new Func1<CityResponseComplete, List<City>>() {
                    @Override
                    public List<City> call(CityResponseComplete cityResponses) {
                        List<City> cities = new LinkedList<>();
                        for (CityResponseComplete.CityResponse cityResponse : cityResponses.cities) {
                            City city = new City();
                            city.setApiId(cityResponse.id);
                            city.setTitle(cityResponse.name);
                            city.setZip(cityResponse.postal_code);
                            city.setLongitude(cityResponse.longitude);
                            city.setLatitude(cityResponse.latitude);
                            cities.add(city);
                        }
                        return cities;
                    }
                })
                .doOnNext(new Action1<List<City>>() {
                    @Override
                    public void call(List<City> cities) {
                        Timber.w("Loading from REST...");
                        toDatabase(cities);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Stores cities into memory
     *
     * @param cities to save
     */
    private void toMemory(List<City> cities) {
        Timber.w("Saving to memory...");

        Map<Integer, City> cityMap = new HashMap<>();
        for (City city : this.cities) {
            cityMap.put(city.apiId, city);
        }
        for (City city : cities) {
            if (!cityMap.containsKey(city.apiId))
                cityMap.put(city.apiId, city);
        }

        this.cities = new ArrayList<City>(cityMap.values());
    }

    /**
     * Stores cities into the database
     *
     * @param cities to save
     */
    private void toDatabase(List<City> cities) {
        Timber.w("Saving to database...");
        City tmp;
        for (City city : cities) {
            tmp = Select.from(City.class)
                    .where(Condition.prop("API_ID").eq(city.apiId))
                    .first();
            if (tmp == null) {
                City.save(city);
            }
        }
    }
}
