package basic.siemens.awesomedev.com.siemensapplication;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.Part;

/**
 * Created by sparsh on 3/17/17.
 */

public interface ServerService {

    @Multipart
    @GET("/base/captions")
    Call<List<Caption>> getCaptions(@Part("image")RequestBody photo);

    @GET("/base/ping")
    Call<String> ping(@Field("ping")String ping);

}
