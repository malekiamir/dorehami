package ir.ac.kntu.patogh.Interfaces;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PatoghApi {
    @POST("/api/User/requestLogin")
    Call<ResponseBody> requestLogin(@Body RequestBody requestBody);

    @POST("/api/User/authenticate")
    Call<ResponseBody> authenticate(@Body RequestBody requestBody);

    @POST("/api/User/getUserDetails")
    Call<ResponseBody> getUserDetails(@Header("Authorization") String token);

    @POST("/api/User/editUserDetails")
    Call<ResponseBody> editUserDetails(@Header("Authorization") String token, @Body RequestBody requestBody);

    @POST("/api/User/favDorehamiGetSummery")
    Call<ResponseBody> getFavorites(@Header("Authorization") String token);

    @GET("/api/Dorehami/getSummery")
    Call<ResponseBody> getSummery(@Header("Authorization") String token);

    @POST("/api/Dorehami/getDetail")
    Call<ResponseBody> getDetail(@Header("Authorization") String token, @Body RequestBody requestBody);

    @POST("/api/User/favDorehamiAdd")
    Call<ResponseBody> favDorehamiAdd(@Header("Authorization") String token, @Body RequestBody requestBody);

    @POST("/api/User/favDorehamiRemove")
    Call<ResponseBody> favDorehamiRemove(@Header("Authorization") String token, @Body RequestBody requestBody);

    @POST("/api/User/joinDorehamiAdd")
    Call<ResponseBody> joinDorehamiAdd(@Header("Authorization") String token, @Body RequestBody requestBody);

    @POST("/api/User/joinDorehamiRemove")
    Call<ResponseBody> joinDorehamiRemove(@Header("Authorization") String token, @Body RequestBody requestBody);

    @POST("/api/Image/downloadThumbnail")
    Call<ResponseBody> downloadThumbnail(@Header("Authorization") String token, @Body RequestBody requestBody);

    @POST("/api/Image/downloadImage")
    Call<ResponseBody> downloadImage(@Header("Authorization") String token, @Body RequestBody requestBody);

    @Multipart
    @POST("/api/User/uploadProfilePicture")
    Call<ResponseBody> uploadProfile(@Header("Authorization") String token, @Part MultipartBody.Part file);

    @Multipart
    @POST("/api/Dorehami/uploadImage")
    Call<ResponseBody> uploadImage(@Header("Authorization") String token, @Part MultipartBody.Part file);

    @POST("/api/Dorehami/createDorehami")
    Call<ResponseBody> createDorehami(@Header("Authorization") String token, @Body RequestBody requestBody);

    @POST("/api/User/joinDorehamiGetSummery")
    Call<ResponseBody> getJoinedDorehami(@Header("Authorization") String token);

    @POST("/api/Dorehami/search")
    Call<ResponseBody> search(@Header("Authorization") String token, @Body RequestBody requestBody);

    @POST("/api/User/deleteUser")
    Call<ResponseBody> deleteUser(@Header("Authorization") String token);

    @POST("/api/Dorehami/deleteDorehami")
    Call<ResponseBody> deleteDorehami(@Header("Authorization") String token, @Body RequestBody requestBody);

    @GET("/api/Dorehami/getOwnedDorehamies")
    Call<ResponseBody> getOwnedDorehamies(@Header("Authorization") String token);
}
