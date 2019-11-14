package ir.ac.kntu.patogh.Interfaces;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PatoghApi {
    @POST("/api/User/requestLogin")
    Call<ResponseBody> requestLogin(@Body RequestBody requestBody);

    @POST("/api/User/authenticate")
    Call<ResponseBody> authenticate(@Body RequestBody requestBody);

    @POST("/api/User/editUserDetails")
    Call<ResponseBody> editUserDetails(@Body RequestBody requestBody);
}
