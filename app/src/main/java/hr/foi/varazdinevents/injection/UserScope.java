package hr.foi.varazdinevents.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Dagger 2 scope annotation for user
 * Specifies that an object provided by method annotated
 * with it to live only during the user lifecycle
 * Created by Antonio Martinović on 09.11.16.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface UserScope {
}