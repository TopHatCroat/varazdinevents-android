package hr.foi.varazdinevents.ui;

import javax.inject.Inject;

import hr.foi.varazdinevents.ui.base.BasePresenter;

/**
 * Created by Antonio Martinović on 12.10.16.
 */

public class MainPresenter extends BasePresenter<MainView> {

    @Inject
    public MainPresenter(){

    }

    public void loadEvents(){
        checkViewAttached();
        //TODO: do something
    }
}
