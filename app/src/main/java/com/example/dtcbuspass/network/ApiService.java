package com.example.dtcbuspass.network;

import android.widget.ImageView;

import com.example.dtcbuspass.network.entities.AccessToken;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {




    @POST("register")

    @FormUrlEncoded
    Call<AccessToken> register(@Field("name") String name, @Field("email") String email,  @Field("password") String password);

  @POST("login")

    @FormUrlEncoded

    Call<AccessToken> login(@Field("username") String username, @Field("password") String password);


    @Multipart
    @POST("general_pass")


    Call<ResponseBody> RegGeneralPass(

            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("phone") RequestBody phone,
            @Part("date_of_birth") RequestBody date_of_birth,
            @Part("gender") RequestBody gender,
            @Part MultipartBody.Part photograph,
            @Part MultipartBody.Part adhar_card



            );


    @Multipart
    @POST("student_pass")

    Call<ResponseBody> RegStudentPass(

            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("phone") RequestBody phone,
            @Part("date_of_birth") RequestBody date_of_birth,
            @Part("gender") RequestBody gender,
            @Part MultipartBody.Part photograph,
            @Part MultipartBody.Part adhar_card,
            @Part MultipartBody.Part student_id,
            @Part MultipartBody.Part verification_letter



    );

    @Multipart
    @POST("senior_citizen_pass")


    Call<ResponseBody> RegSeniorCitizen(

            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("phone") RequestBody phone,
            @Part("date_of_birth") RequestBody date_of_birth,
            @Part("gender") RequestBody gender,
            @Part MultipartBody.Part photograph,
            @Part MultipartBody.Part adhar_card



    );


    @Multipart
    @POST("disabled_pass")

    Call<ResponseBody> RegDisabledPass(

            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("phone") RequestBody phone,
            @Part("date_of_birth") RequestBody date_of_birth,
            @Part("gender") RequestBody gender,
            @Part MultipartBody.Part photograph,
            @Part MultipartBody.Part adhar_card,
            @Part MultipartBody.Part certificate



    );



  @POST("feedback")

  @FormUrlEncoded
  Call<AccessToken> feedbackSuggestion(@Field("name") String name, @Field("email") String email,  @Field("description") String description);










}
