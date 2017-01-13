package hr.foi.varazdinevents.injection;


import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Dagger 2 scope annotation for activity
 * Specifies that an object provided by method annotated
 * with it to live only during the activity lifecycle
 */
@Scope
@Retention(RUNTIME)
public @interface ActivityScope {
}
