package hr.foi.varazdinevents.ui.base;

/**
 * Class that every application presenter should extend,
 * Activity is expected to call {@link BasePresenter#attachView(ViewLayer)}
 * before calling any presenter methods to allow the presenter access to
 * public methods of the activity
 * Created by Antonio Martinović on 12.10.16.
 */
public abstract class BasePresenter<T extends ViewLayer> implements PresenterLayer<T> {

    private T viewLayer;

    /**
     * Must be called before any other presenter method
     * @param viewLayer view extending @ViewLayer presented activity
     */
    @Override
    public void attachView(T viewLayer) {
        this.viewLayer = viewLayer;
    }

    /**
     * Must be called after destroying the activity to allow the
     * Garbage Collector to do it's thing
     */
    @Override
    public void detachView() {
        this.viewLayer = null;
    }

    /**
     * Checks if the view is available, useful for when experiencing difficulties
     * with the amazing awesomeness that is the Android activity lifecycle
     * @return
     */
    public boolean isViewAttached() {
        return viewLayer != null;
    }

    /**
     * @return currently attached activity
     */
    public T getViewLayer(){
        return viewLayer;
    }

    /**
     * Throws {@link ViewNotAttachedException} if no activity attached
     */
    public void checkViewAttached() {
        if (!isViewAttached()) throw new ViewNotAttachedException();
    }

    /**
     * Exception to be thrown when view is not attached
     */
    public static class ViewNotAttachedException extends RuntimeException {
        public ViewNotAttachedException() {
            super("Please save Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
