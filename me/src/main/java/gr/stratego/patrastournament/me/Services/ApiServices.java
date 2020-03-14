package gr.stratego.patrastournament.me.Services;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface  ApiServices {


    @Headers("Content-Type: text/html")
    @POST("{url}")
    Call<ResponseBody> getTournamentProgress(@Path("url") String url);

}