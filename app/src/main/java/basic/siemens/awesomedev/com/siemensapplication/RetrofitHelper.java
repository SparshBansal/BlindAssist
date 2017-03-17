package basic.siemens.awesomedev.com.siemensapplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sparsh on 3/17/17.
 */

public class RetrofitHelper {

    private static ServerService mService = null;

    static {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.154:8000").addConverterFactory(GsonConverterFactory.create()).build();
        mService = retrofit.create(ServerService.class);
    }


    public static ServerService getInstance(){
        return mService;
    }

}
