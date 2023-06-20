package com.uptree.lawdiary.Retrofit;

import com.uptree.lawdiary.Model.AddCasePojo;
import com.uptree.lawdiary.Model.HearingModel;
import com.uptree.lawdiary.Model.LoginPojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("registration.php")
    Call<LoginPojo> register(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone") String phone,
            @Field("chamber_no") String chamber_no,
            @Field("chamber_address") String chamber_address,
            @Field("image") String image

    );

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginPojo> register(
            @Field("email") String email,
            @Field("password") String password

    );
    @FormUrlEncoded
    @POST("add_case.php")
    Call<AddCasePojo> addCase(

            @Field("case_title") String case_title,
            @Field("name_of_judge") String name_of_judge,
            @Field("name_of_culprit") String name_of_culprit,
            @Field("case_category") String case_category,
            @Field("case_details") String case_details,
            @Field("case_starting_date") String case_starting_date,
            @Field("current_date") String current_date,
            @Field("user_id") String user_id,
            @Field("name_of_complain") String name_of_complain,
            @Field("our_services_for") String our_services_for

    );

    @FormUrlEncoded
    @POST("all_cases.php")
    Call<List<AddCasePojo>> fetchAllCases(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("today_added_cases.php")
    Call<List<AddCasePojo>> fetchTodayCases(
            @Field("user_id") String user_id
            ,@Field("case_starting_date") String case_starting_date
    );
    @FormUrlEncoded
    @POST("delete_case.php")
    Call<AddCasePojo> deleteCase(
            @Field("id") String id

    );
    @FormUrlEncoded
    @POST("search_case.php")
    Call<List<AddCasePojo>> searchCase(
            @Field("user_id") String userId,
            @Field("search_text") String searchText

    );
    @FormUrlEncoded
    @POST("profile_update.php")
    Call<LoginPojo> editProfile(
            @Field("id") String id,
            @Field("username") String username,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("chamber_no") String chamber_no,
            @Field("chamber_address") String chamber_address,
            @Field("image") String image

    );

    @FormUrlEncoded
    @POST("add_next_hearing.php")
    Call<AddCasePojo>newDate(
            @Field("user_id") String user_id,
            @Field("case_id") String case_id,
            @Field("new_date") String new_date
    );

    @FormUrlEncoded
    @POST("fetch_next_hearing.php")
    Call<List<HearingModel>> fetchDates(
            @Field("user_id") String user_id,
            @Field("case_id") String case_id
    );

    @FormUrlEncoded
    @POST("forgot_password.php")
    Call<LoginPojo> resetPassword(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("fetch_phone.php")
    Call<LoginPojo>fetchPhone(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("update_password.php")
    Call<LoginPojo> updatePassword(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("case_judgement.php")
    Call<AddCasePojo> addJudgement(
            @Field("user_id") String user_id,
            @Field("case_id") String case_id,
            @Field("new_date") String new_date,
            @Field("win") String win,
            @Field("judgement") String judgement
    );

    @FormUrlEncoded
    @POST("fetch_case_judgement.php")
    Call<AddCasePojo> fetchJudgement(
            @Field("user_id") String user_id,
            @Field("case_id") String case_id
    );

    @FormUrlEncoded
    @POST("close_cases.php")
    Call<List<AddCasePojo>> fetchCloseCases(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("delete_next_hiring.php")
    Call<HearingModel> deleteHearingDate(
            @Field("user_id") String user_id,
            @Field("case_id") String case_id,
            @Field("id") String id
    );


    @FormUrlEncoded
    @POST("open_cases.php")
    Call<List<AddCasePojo>> openCases(

            @Field("user_id") String user_id



    );
}

