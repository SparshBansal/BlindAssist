package basic.siemens.awesomedev.com.siemensapplication;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sparsh on 3/17/17.
 */

public class RetrofitHelper {

    private static ServerService mService = null;

    static {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.166:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        mService = retrofit.create(ServerService.class);
    }


    public static ServerService getInstance() {
        return mService;
    }

}
