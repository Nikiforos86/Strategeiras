package gr.stratego.patrastournament.me.Services;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import gr.stratego.patrastournament.me.Services.ApiResponses.ApiTournamentProgressResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class WebServices {


    public static void getTournamentProgress(String url, final DefaultCallback<ApiTournamentProgressResponse> callback) {

        final Call<ResponseBody> call = RestClient.getConfigRestClient().getTournamentProgress(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if(response.body() != null){
                        try {
                            ApiTournamentProgressResponse tournamentResponse = new ApiTournamentProgressResponse();
                            tournamentResponse.setTournamentProgressHtml(response.body().string());
                            callback.onResponse(tournamentResponse);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable error) {
                Timber.e(error);
            }
        });
    }
}
