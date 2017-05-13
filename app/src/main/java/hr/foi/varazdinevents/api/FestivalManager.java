package hr.foi.varazdinevents.api;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hr.foi.varazdinevents.api.responses.FestivalResponseComplete;
import hr.foi.varazdinevents.models.Festival;
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
 * Contains all methods for working with festivals such as getting all festivals and creating new ones
 */
public class FestivalManager {
    private final SharedPrefs sharedPrefs;
    private final User user;
    private final RestService restService;
    private List<Festival> festivals;

    /**
     * @param user        current user logged in
     * @param restService Retrofit API calls interface
     */
    public FestivalManager(User user, RestService restService, SharedPrefs sharedPrefs) {
        this.restService = restService;
        this.user = user;
        this.sharedPrefs = sharedPrefs;
        festivals = new ArrayList<>();
    }

    /**
     * Gets all festivals from API
     *
     * @return list of festivals
     */
    public Observable<List<Festival>> getFestivals() {
        return Observable.concat(fromMemory(), fromDatabase(), fromNetwork())
                .cache()
                .map(new Func1<List<Festival>, List<Festival>>() {
                    @Override
                    public List<Festival> call(List<Festival> cities) {
                        toMemory(cities);
                        return FestivalManager.this.festivals;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Loads data from memory
     *
     * @return all festivals loaded in memory
     */
    private Observable<List<Festival>> fromMemory() {
        return Observable.just(festivals).doOnNext(new Action1<List<Festival>>() {
            @Override
            public void call(List<Festival> cities) {
                Timber.w("Loading from memory...");
            }
        });
    }

    /**
     * Loads event data from database that has the DATE_TO attribute set to lesser than now
     *
     * @return festivals from database
     */
    private Observable<List<Festival>> fromDatabase() {
        return Observable.just(
                Select.from(Festival.class).list()
        )
                .doOnNext(new Action1<List<Festival>>() {
                    @Override
                    public void call(List<Festival> cities) {
                        Timber.w("Loading from database...");
                        toMemory(cities);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Loads all new festivals from the API and stores the new ones to the database and the memory
     *
     * @return all recently changed or new festivals
     */
    private Observable<List<Festival>> fromNetwork() {
        return restService.getFestivals()
                .map(new Func1<FestivalResponseComplete, List<Festival>>() {
                    @Override
                    public List<Festival> call(FestivalResponseComplete festivalResponses) {
                        List<Festival> festivals = new LinkedList<>();
                        for (FestivalResponseComplete.FestivalResponse festivalResponse : festivalResponses.festivals) {
                            Festival festival = new Festival();
                            festival.setApiId(festivalResponse.id);
                            festival.setTitle(festivalResponse.name);
                            festival.setLongitude(festivalResponse.longitude);
                            festival.setLatitude(festivalResponse.latitude);
                            festivals.add(festival);
                        }
                        return festivals;
                    }
                })
                .doOnNext(new Action1<List<Festival>>() {
                    @Override
                    public void call(List<Festival> cities) {
                        Timber.w("Loading from REST...");
                        toDatabase(cities);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Stores festivals into memory
     *
     * @param festivals to save
     */
    private void toMemory(List<Festival> festivals) {
        Timber.w("Saving to memory...");

        Map<Integer, Festival> FestivalMap = new HashMap<>();
        for (Festival festival : this.festivals) {
            FestivalMap.put(festival.apiId, festival);
        }
        for (Festival Festival : festivals) {
            if (!FestivalMap.containsKey(Festival.apiId))
                FestivalMap.put(Festival.apiId, Festival);
        }

        this.festivals = new ArrayList<>(FestivalMap.values());
    }

    /**
     * Stores festivals into the database
     *
     * @param festivals to save
     */
    private void toDatabase(List<Festival> festivals) {
        Timber.w("Saving to database...");
        Festival tmp;
        for (Festival festival : festivals) {
            tmp = Select.from(Festival.class)
                    .where(Condition.prop("API_ID").eq(festival.apiId))
                    .first();
            if (tmp == null) {
                Festival.save(festival);
            }
        }
    }
}
