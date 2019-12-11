package ir.ac.kntu.patogh.Interfaces;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PatoghApi {
    @POST("/api/User/requestLogin")
    Call<ResponseBody> requestLogin(@Body RequestBody requestBody);

    @POST("/api/User/authenticate")
    Call<ResponseBody> authenticate(@Body RequestBody requestBody);

    @POST("/api/User/editUserDetails")
    Call<ResponseBody> editUserDetails(@Header("Authorization")String token, @Body RequestBody requestBody);

    @POST("/api/User/getUserDetails")
    Call<ResponseBody> getUserDetails(@Header("Authorization")String token);

    @GET("/api/Dorehami/getSummery")
    Call<ResponseBody> getSummery(@Header("Authorization")String token);

    @POST("/api/Dorehami/getDetail")
    Call<ResponseBody> getDetail(@Header("Authorization")String token, @Body RequestBody requestBody);

    @POST("/api/User/favDorehamiAdd")
    Call<ResponseBody> favDorehamiAdd(@Header("Authorization")String token, @Body RequestBody requestBody);

    @POST("/api/User/favDorehamiRemove")
    Call<ResponseBody> favDorehamiRemove(@Header("Authorization")String token, @Body RequestBody requestBody);

    @POST("/api/User/joinDorehamiAdd")
    Call<ResponseBody> joinDorehamiAdd(@Header("Authorization")String token, @Body RequestBody requestBody);

    @POST("/api/Image/downloadThumbnail")
    Call<ResponseBody> downloadThumbnail(@Header("Authorization")String token, @Body RequestBody requestBody);

    @POST("/api/Image/downloadImage")
    Call<ResponseBody> downloadImage(@Header("Authorization")String token, @Body RequestBody requestBody);
}
