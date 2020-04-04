package gr.stratego.patrastournament.me.Services;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import gr.stratego.patrastournament.me.Utils.StringUtils;
import timber.log.Timber;

public abstract class DefaultCallback<T> {

    /**
     * The fragment.
     */
    protected Fragment mFragment;

    /**
     * The activity.
     */
    protected FragmentActivity mActivity;

    private boolean mShowLoader = true;
    private boolean mForceShowLoader = false;

    private boolean canRetryCall = true;

    public DefaultCallback() {

    }

    /**
     * Creates a new instance with the fragment provided.
     *
     * @param fragment the fragment
     */
    public DefaultCallback(final Fragment fragment) {
        this.mFragment = fragment;
        this.mActivity = fragment.getActivity();
        startLoading();
    }

    public DefaultCallback(final Fragment fragment, boolean showLoader) {
        this.mFragment = fragment;
        this.mActivity = fragment.getActivity();
        this.mShowLoader = showLoader;
        startLoading();
    }

    public DefaultCallback(final Fragment fragment, boolean showLoader, boolean forceShowLoader) {
        this.mFragment = fragment;
        this.mActivity = fragment.getActivity();
        this.mShowLoader = showLoader;
        this.mForceShowLoader = forceShowLoader;
        startLoading();
    }

    public DefaultCallback(final FragmentActivity activity) {
        this.mActivity = activity;
        startLoading();
    }

    public DefaultCallback(final FragmentActivity activity, boolean showLoader) {
        this.mActivity = activity;
        this.mShowLoader = showLoader;
        startLoading();
    }

    public DefaultCallback(final FragmentActivity activity, boolean showLoader, boolean forceShowLoader) {
        this.mActivity = activity;
        this.mShowLoader = showLoader;
        this.mForceShowLoader = forceShowLoader;
        startLoading();
    }


    public void onResponse(T response) {
        stopLoading();
    }


    public void onSuccess() {
//        stopLoading();
    }

    public void onFailure(String errorMessage) {
        stopLoading();
        showErrorMessage(errorMessage);
    }


    //    /**
//     * Shows the errorMessage message provided.
//     *
//     * @param errorMessage the errorMessage message
//     */
    protected void showErrorMessage(String errorMessage) {
        if (errorMessage == null) {
            errorMessage = "";
        }
        if (!mShowLoader) {
            return;
        }
        String message = "Problem occured";
        if (StringUtils.isNotNullOrEmpty(errorMessage)) {
            message = errorMessage;
        }
//        if (mActivity != null) {
//            GeneralUtils.showErrorDialog(mActivity, errorMessage);
//        } else if (mFragment != null) {
//            GeneralUtils.showErrorDialog(mFragment, errorMessage);
//        }
        Timber.e("showErrorMessage " + message + ", " + errorMessage);
    }

    private void startLoading() {
        if (!mShowLoader) {
            return;
        }
        if (mFragment != null) {
//            if (mFragment instanceof BaseFragment) {
//                ((BaseFragment) mFragment).showProgressIndicator(true);
//            }
        } else {
//            if (mActivity instanceof BaseActivity) {
//                ((BaseActivity) mActivity).showProgressIndicator(true);
//            }
        }
    }

    //
    private void stopLoading() {
        Timber.d("stopLoading %s", "");
        if (mForceShowLoader) {
            return;
        }
//        if (mFragment != null) {
//            if (mFragment instanceof BaseFragment) {
//                ((BaseFragment) mFragment).showProgressIndicator(false);
//            }
//        } else {
//            if (mActivity instanceof BaseActivity) {
//                ((BaseActivity) mActivity).showProgressIndicator(false);
//            }
//        }
    }
//
//    public void handleException(Throwable error, DefaultCallback<T> callback) {
//        if (error instanceof SSLPeerUnverifiedException
//                || error instanceof SSLHandshakeException
//                || error instanceof SSLException) {
////            callback.onFailure("");
//            CertificatePinningHelper.getInstance().getPinset(true, new DefaultCallback<ApiGetPinSetResponse>() {
//                @Override
//                public void onSuccess() {
//                    super.onSuccess();
//                    RestClient.reSetupRestClients();
//                    restartAppProcesses();
//                }
//
//                @Override
//                protected void onFailure(Response<? extends ApiBaseResponse> response) {
//                    callback.onFailure(WebServices.handleRetrofitError(error));
//                }
//            });
//        } else {
//            callback.onFailure(WebServices.handleRetrofitError(error));
//        }
//    }

}