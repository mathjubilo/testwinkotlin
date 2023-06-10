package com.example.testwinkotlin.data.remote.api

import com.example.testwinkotlin.data.remote.dto.login.LoginRequestObject
import com.example.testwinkotlin.data.remote.dto.login.LoginResponseObject
import com.example.testwinkotlin.data.remote.dto.logout.LogoutRequestObject
import com.example.testwinkotlin.data.remote.dto.tokensManagement.RefreshTokenRequestObject
import com.example.testwinkotlin.data.remote.dto.user.UserInfoResponseObject
import com.example.testwinkotlin.data.remote.dto.user.UserSettingsResponseObject
import retrofit2.Response
import retrofit2.http.*

interface IWinApi {

    @POST("mobile/authenticate")
    suspend fun login(@Body loginRequest: LoginRequestObject): Response<LoginResponseObject>

    @Headers("Content-Type: application/json")
    @POST("mobile/authenticate/refresh")
    suspend fun renewToken(@Body refreshToken: RefreshTokenRequestObject): Response<Unit>

    @GET("sgawinws/api/v1/user/settings")
    suspend fun getUserSettings(): Response<UserSettingsResponseObject>;

    @GET("sgawinws/api/v1/user")
    suspend fun getUserInfo(): Response<UserInfoResponseObject?>?

    //@DELETE("mobile/authenticate/revoke")
    @HTTP(method = "DELETE", path = "mobile/authenticate/revoke", hasBody = true)
    suspend fun logout(@Body logoutRequest: LogoutRequestObject): Response<Unit>

    /*
    //@Headers("Authorization: Bearer ${ACCESS_TOKEN}")
    @GET("sgawinws/api/v1/incidences")
    suspend fun getIncidents(
        @QueryMap requestObject: Map<String, String>
    ): GetIncidentsResponseObject

    //@Headers("Authorization: Bearer ${ACCESS_TOKEN}")
    @GET("sgawinws/api/v1/incidences/{id}")
    suspend fun getIncidenceDetailById(@Path("id") incidenceId: Int): GetIncidentDetailResponseObject

    //@Headers("Authorization: Bearer ${ACCESS_TOKEN}")
    @PUT("sgawinws/api/v1/incidences/{id}")
    suspend fun followIncident(
        @Path("id") incidenceId: Int,
        @Body isFollowedByUser: Boolean
    ): FollowIncidentResponseObject

    //https://preint-api.inditex.com/sgawinws/api/v1/incidences/123898/message?offset=0&limit=10
    //@Headers("Authorization: Bearer ${ACCESS_TOKEN}")
    @GET("sgawinws/api/v1/incidences/{id}/message")
    suspend fun getMessagesAndWorklogs(
        @Path("id") incidenceId: Integer,
        @Query("offset") offset: Integer,
        @Query("limit") limit: Integer
    ): GetMessagesAndWorklogsResponseObject


        @GET("sgawinws/api/v1/incidences/followed")
        suspend fun getFollowedIncidences( /*@Header("Authorization") String accessToken,*/
            @Query("offset") offset: Int,
            @Query("limit") limit: Int,
            @Query("priority") priority: ArrayList<Int?>,
            @Query("state") state: ArrayList<Int?>,
            @Query("interval") interval: Int,
            @Query("order") order: String,
            @Query("centerId") centerId: Int,
            @Query("companyAreaLogisticIds") companyAreaLogisticIds: ArrayList<Int>,
            @Query("lacTypeIds") lacTypeIds: ArrayList<Int>,
            @Query("moduleIds") moduleIds: ArrayList<Int>,
            @Query("incidenceCodContains") incidenceCodContains: String,
            @Query("businessLineIds") businessLineIds: ArrayList<Int>
        ): Call<IncidencesList>

        @GET("sgawin-mdata/v2/lacs")
        suspend fun getCenters(
            @Query("offset") offset: Int,
            @Query("limit") limit: Int
        ): Call<GetLacs?>?

        // Get modules list
        //https://api.inditex.com/sgawin-mdata/v2/modules?offset=0&limit=30
        @GET("sgawin-mdata/v2/modules")
        suspend fun getModules(
            @Query("offset") offset: Int?,
            @Query("limit") limit: Int?
        ): Call<GetLacs?>?

        @GET("sgawinws/api/v1/incidence-priorities")
        suspend fun getPriority(
            @Query("offset") offset: Int?,
            @Query("limit") limit: Int?
        ): Call<GetLacs?>?

        /* @GET("posts/{id}/comments")
        Call<List<Comment>> getCommentsForId(@Path("id") int postId); */
        @GET("sgawinws/api/v1/incidences/{id}")
        suspend fun getIncidenceDetailById(@Path("id") incidenceId: Int?): Call<IncidenceDetailResponse?>?

        // Get center types list
        //https://api.inditex.com/sgawin-mdata/v2/lac-types?offset=0&limit=30
        @GET("sgawin-mdata/v2/lac-types")
        suspend fun getCenterTypes(
            @Query("offset") offset: Int?,
            @Query("limit") limit: Int?
        ): Call<GetLacs?>?

        // Get business lines list
        //https://api.inditex.com/sgawin-mdata/v2/business-lines?offset=0&limit=30
        @GET("sgawin-mdata/v2/business-lines")
        suspend fun getBusinessLines(
            @Query("offset") offset: Int?,
            @Query("limit") limit: Int?
        ): Call<GetLacs?>?

        @PUT("sgawinws/api/v1/incidences/{id}")
        suspend fun followIncidence(
            @Path("id") incidenceId: Int?,
            @Body isFollowedByUser: Boolean?
        ): Call<Boolean?>?


      @PUT("sgawinws/api/v1/incidences/{id}")
      Call<FollowIncidenceResponse> followIncidence(@Path("id") int incidenceId, @Body boolean isFollowedByUser);

      @POST("mobile/authenticate/refresh")
      Call<Void> renewTask(@Body RefreshTokenRequestParameters refreshToken);

      @GET("sgawinws/api/v1/user/settings")
      Call<UserSettingsResponse> getUserSettings();

      @POST("mobile/authenticate")
      Call<Void> loginTask(@Body LoginRequest loginRequest);

      @GET("sgawin-mdata/v2/lacs")
      Call<GetLacs> getCenters(@Query("offset") Integer offset, @Query("limit") Integer limit, @Query("search") String search);

      @GET("sgawin-mdata/v2/modules")
      Call<GetLacs> getModules(@Query("offset") Integer offset, @Query("limit") Integer limit, @Query("search") String search);

      @GET("sgawinws/api/v1/incidence-priorities")
      Call<GetLacs> getPriorities(@Query("offset") Integer offset, @Query("limit") Integer limit);

      @GET("sgawin-mdata/v2/lac-types")
      Call<GetLacs> getCenterType(@Query("offset") Integer offset, @Query("limit") Integer limit);

      @GET("sgawin-mdata/v2/business-lines")
      Call<GetLacs> getBusinessLine(@Query("offset") Integer offset, @Query("limit") Integer limit, @Query("search") String search);

      @GET("sgawinws/api/v1/incidences")
      Call<IncidencesList> getIncidencesList(
          @Query("offset") Integer offset,
          @Query("limit") Integer limit,
          @Query("priority") ArrayList<Integer> priority,
          @Query("state") ArrayList<Integer> state,
          @Query("interval") Integer interval,
          @Query("order") String order,
          @Query("centerId") Integer centerId,
          @Query("companyAreaLogisticIds") ArrayList<Integer> companyAreaLogisticIds,
          @Query("lacTypeIds") ArrayList<Integer> lacTypeIds,
          @Query("moduleIds") ArrayList<Integer> moduleIds,
          @Query("incidenceCodContains") String incidenceCodContains,
          @Query("businessLineIds") ArrayList<Integer> businessLineIds);

      @GET("sgawinws/api/v1/incidences/followed")
      Call<IncidencesList> getIncidencesFollowedList(
              /*@Header("Authorization") String accessToken,*/
              @Query("offset") Integer offset,
              @Query("limit") Integer limit,
              @Query("priority") ArrayList<Integer> priority,
              @Query("state") ArrayList<Integer> state,
              @Query("interval") Integer interval,
              @Query("order") String order,
              @Query("centerId") Integer centerId,
              @Query("companyAreaLogisticIds") ArrayList<Integer> companyAreaLogisticIds,
              @Query("lacTypeIds") ArrayList<Integer> lacTypeIds,
              @Query("moduleIds") ArrayList<Integer> moduleIds,
              @Query("incidenceCodContains") String incidenceCodContains,
              @Query("businessLineIds") ArrayList<Integer> businessLineIds
      );

      @GET("sgawinws/api/v1/incidences/{id}")
      Call<IncidenceDetailResponse> getIncidenceDetailById(@Path("id") Integer incidenceId);

      @GET("sgawinws/api/v1/centers")
      Call<DistributionCenterResponse> getDistributionCenters(
              @Query("offset") Integer offset,
              @Query("limit") Integer limit,
              @Query("priority") ArrayList<Integer> priority,
              @Query("state") ArrayList<Integer> state,
              @Query("interval") Integer interval,
              @Query("order") String order,
              @Query("centerId") Integer centerId,
              @Query("companyAreaLogisticIds") ArrayList<Integer> companyAreaLogisticIds,
              @Query("lacTypeIds") ArrayList<Integer> lacTypeIds,
              @Query("moduleIds") ArrayList<Integer> moduleIds,
              @Query("incidenceCodContains") String incidenceCodContains,
              @Query("businessLineIds") ArrayList<Integer> businessLineIds
      );

      //https://preint-api.inditex.com/sgawinws/api/v1/incidences/123898/message?offset=0&limit=10
      @GET("sgawinws/api/v1/incidences/{id}/message")
      Call<WorklogMessageResponse> getMessages(@Path("id") Integer incidenceId, @Query("offset") Integer offset, @Query("limit") Integer limit);

      //http://preint-api.inditex.com:8000/sgawinws/v1/incidences/123898/worklog?_links=false&offset=0&limit=10
      @GET("sgawinws/api/v1/incidences/{id}/worklog")
      Call<WorklogMessageResponse> getWorklogs(@Path("id") Integer incidenceId, @Query("offset") Integer offset, @Query("limit") Integer limit);

      //https://preint-api.inditex.com/sgawinws/api/v1/incidences/123898/message?
      @POST("sgawinws/api/v1/incidences/{incidenceId}/message")
      Call<SendMessageResponse> sendMessage(@Path("incidenceId") Integer incidenceId, @Body SendMessageRequestBody body);

      @GET("sgawinws/api/v1/user")
      Call<MyProfileInfoResponse> getMyProfileInfo();
    */
    companion object {
        var USERNAME = ""
        var PASSWORD = ""
        var ACCESS_TOKEN = ""
        var REFRESH_TOKEN = ""
        const val BASE_URL = "https://preint-api.inditex.com/"
    }
}