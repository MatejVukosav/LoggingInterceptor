package ihsanbal.com.logginginterceptor.module;

import android.util.Log;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ihsanbal.com.logginginterceptor.BuildConfig;
import ihsanbal.com.logginginterceptor.api.Api;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author ihsan on 09/02/2017.
 */
@Module
public class NetModule {

    private String mEndPoint;

    public NetModule(String endpoint) {
        mEndPoint = endpoint;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(new LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Log.DEBUG)
                .tag("LoggingI")
                .request("Request")
                .response("Response")
                .addHeader("version", BuildConfig.VERSION_NAME)
                .build());
        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(mEndPoint)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    Api provideApi(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }

}
