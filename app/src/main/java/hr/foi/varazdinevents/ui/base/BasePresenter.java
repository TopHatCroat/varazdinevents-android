package hr.foi.varazdinevents.ui.base;

/**
 * Created by Antonio Martinović on 12.10.16.
 */

public abstract class BasePresenter<T extends ViewLayer> implements PresenterLayer<T> {

    private T viewLayer;

    @Override
    public void attachView(T viewLayer) {
        this.viewLayer = viewLayer;
    }

    @Override
    public void detachView() {
        this.viewLayer = null;
    }

    public boolean isViewAttached() {
        return viewLayer != null;
    }

    public T getViewLayer(){
        return viewLayer;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new ViewNotAttachedException();
    }

    public static class ViewNotAttachedException extends RuntimeException {
        public ViewNotAttachedException() {
            super("Please save Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
