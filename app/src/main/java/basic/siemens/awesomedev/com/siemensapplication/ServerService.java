package basic.siemens.awesomedev.com.siemensapplication;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by sparsh on 3/17/17.
 */

public interface ServerService {

    @Multipart
    @POST("/base/getImageCaption/")
    Call<List<Caption>> getCaptions(@Part MultipartBody.Part photo);

    @FormUrlEncoded
    @POST("/base/ping/")
    Call<Ping> ping(@Field("ping") String ping);


    @Multipart
    @POST("/base/uploadImage/")
    Call<Ping> uploadImage(@Part MultipartBody.Part photo,@Part("name") RequestBody name);
}
