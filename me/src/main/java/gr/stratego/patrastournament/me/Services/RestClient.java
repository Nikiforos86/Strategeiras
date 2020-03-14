package gr.stratego.patrastournament.me.Services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import gr.stratego.patrastournament.me.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Converter;
import retrofit2.Retrofit;

public class RestClient {
    private static ApiServices REST_CLIENT;
    private static ApiServices REST_SECURE_CLIENT;
    private static ApiServices REST_CONFIG_CLIENT;

    private static Retrofit RETROFIT;
    private static ApiServices REST_CLIENT_PIN;

    static {
        setupConfigRestClient();
    }

    public static ApiServices getPINClient() {
        return REST_CLIENT_PIN;
    }

    public static ApiServices get() {
        return REST_CLIENT;
    }

    public static Retrofit retrofit() {
        return RETROFIT;
    }


    public static ApiServices getConfigRestClient() {
        return REST_CONFIG_CLIENT;
    }

    private static void setupConfigRestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_CONFIG_URL)
                .client(getHttpClient())
                .build();
        REST_CONFIG_CLIENT = retrofit.create(ApiServices.class);
    }


//    static final class PageAdapter implements Converter<ResponseBody, SecondClass.Page> {
//        static final Converter.Factory FACTORY = new Converter.Factory() {
//            @Override
//            public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
//                if (type == SecondClass.Page.class) return new SecondClass.PageAdapter();
//                return null;
//            }
//        };
//
//        @Override
//        public SecondClass.Page convert(ResponseBody responseBody) throws IOException {
//            Document document = Jsoup.parse(responseBody.string());
//            Element value = document.select("script").get(1);
//            String content = value.html();
//            return new SecondClass.Page(content);
//        }
//    }

    private static OkHttpClient getHttpClient() {

        OkHttpClient.Builder httpBuilder = new OkHttpClient().newBuilder();
        httpBuilder.readTimeout(ApiConstants.HTTP_READ_TIMEOUT, TimeUnit.SECONDS);
        httpBuilder.connectTimeout(ApiConstants.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            httpBuilder.addInterceptor(getLoggingInterceptor());
        }
        httpBuilder.retryOnConnectionFailure(true);
        return httpBuilder.build();
    }

    // Log requests & responses.
    private static HttpLoggingInterceptor getLoggingInterceptor() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
