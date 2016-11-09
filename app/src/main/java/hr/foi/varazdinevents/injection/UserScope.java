package hr.foi.varazdinevents.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Antonio Martinović on 09.11.16.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface UserScope {
}