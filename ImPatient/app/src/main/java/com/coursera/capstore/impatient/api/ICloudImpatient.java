package com.coursera.capstore.impatient.api;

import com.coursera.capstore.impatient.bean.CheckIn;
import com.coursera.capstore.impatient.bean.TreatmentList;
import com.coursera.capstore.impatient.bean.User;
import com.coursera.capstore.impatient.bean.UserDate;
import com.coursera.capstore.impatient.bean.UserLogin;
import com.coursera.capstore.impatient.bean.Waiting;
import com.coursera.capstore.impatient.bean.WaitingList;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

public interface ICloudImpatient {

	    @POST("userLogin")
	    //Call<UserLogin> getUserLogin(@Query("username") String userName);
	    Call<UserLogin> getUserLogin(@Body UserLogin userLogin);
	    
	  	@POST("user")
	    Call<User> getUser(@Body UserLogin userLogin);
	    
	  	@POST("doctor")
	    Call<User> getUserDoctor(@Body UserLogin userLogin);
	  	
	  	@POST("checkIn")
	    Call<CheckIn> getUserCheckIn(@Body UserLogin userLogin);
	  	
	  	@POST("date")
	    Call<UserDate> getUserDate(@Body UserLogin userLogin);

	  	@POST("treatment")
	    Call<TreatmentList> getTreatmentList(@Body UserLogin userLogin);

	  	@POST("waitingList")	  	
	    Call<WaitingList> getWaitingList(@Body UserLogin userLogin);
	  	
	  	@POST("updateUser")
	    Call<User> updateUser(@Body User user);
	  	
	  	@POST("updateWaitingList")
	    Call<WaitingList> updateWaitingList(@Body Waiting waiting);

	  	@POST("updateCheckIn")
	    Call<CheckIn> updateCheckIn(@Body CheckIn checkIn);
}
