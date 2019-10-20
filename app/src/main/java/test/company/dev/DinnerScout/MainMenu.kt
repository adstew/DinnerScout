package test.company.dev.DinnerScout
import LoginFragment
import android.annotation.TargetApi
import android.app.AppOpsManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.yelp.fusion.client.connection.interceptors.ApiKeyInterceptor;
import com.yelp.fusion.client.exception.ErrorHandlingInterceptor;
import com.yelp.fusion.client.models.ApiKey;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;


import test.company.dev.DinnerScout.fragments.SignupFragment
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.SearchResponse
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.yelp.fusion.client.models.Business
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainMenu : AppCompatActivity(), View.OnClickListener,
    SignupFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener{
    override fun onClick(p0: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val API_KEY : String =  "M0CMWi9fgorumQJ8FK5oEL7k8npWFFovJd7LP0WQ55zjcVO1Nkkiai_iaWWAuTKv5Qu-ZD3oUG4gprEAnrNo7GJxNUGK47vcWdbp1WDYPDjvRj8YA-bGcepEQ62rXXYx"
//        val NOTIFICATION_ID = 1
//        var uid: String? = null
//        var appsMonitoredKey: String? =
//            null    // allows creation of user unique sharedpref for checkboxes
    }
    private var manager: FragmentManager? = null
    private val trans: FragmentTransaction? = null
     var business : Business? = null
    var params:HashMap<String, String> = HashMap()
    var apiFactory = YelpFusionApiFactory()
    var yelpFusionApi = apiFactory.createAPI(API_KEY)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inflateFirebaseLogin()


        params.put("latitude",  "40.581140")
        params.put("longitude","-111.914184")
//        val call = yelpFusionApi.getBusinessSearch(params)
//        val response = call.execute()
        var a = 1 + 1

        val call = yelpFusionApi.getBusiness("gR9DTbKCvezQlqvD7_FzPw");
        call.enqueue(object : Callback<Business> {

            override fun onResponse(call : Call<Business>?, response : Response<Business> ) {
                if(response != null && response.isSuccessful) {
                     business = response.body()
                     var a  = 1 + 1
                }
                // Update UI text with the Business object.

            }
            override fun onFailure(call: Call<Business>?, t: Throwable?) {
                // HTTP error happened, do something to handle it.
            }
        })
        // Initialize view variables with respective IDs.
//        settingsButton = findViewById(R.id.settingsButton)
//        aboutButton = findViewById(R.id.aboutButton)
//        usageButton = findViewById(R.id.usageButton)



        // Inflates Firebase login

        // creates notification bar
//        createNotificationChannel()
//        modifyNotification("stop")


        // Method to used with initial install of app to get proper system level permission
        // allowing checking of foreground app
//        requestUsageStatsPermission()


        // Load package, friendlynames, and apps to be monitored into sharedresources
        // also clear old stale usage data. New/current data retrieved via intent svc
        //        String[] packageKeys = this.getResources().getStringArray(R.array.package_names);
        //        String[] friendlyNameAppValues = this.getResources().getStringArray(R.array.friendly_app_names);
        //
        //        Log.d("intentsvc", "2: " + MainMenu.uid);
        //
        //        SharedPreferences sharedPrefPackageNames = this.getSharedPreferences("package_friendly_names",Context.MODE_PRIVATE);
        //        SharedPreferences.Editor editor1 = sharedPrefPackageNames.edit();
        //        SharedPreferences sharedPrefAppsMonitored = this.getSharedPreferences("apps_monitored",Context.MODE_PRIVATE);
        //        SharedPreferences.Editor editor2 = sharedPrefAppsMonitored.edit();
        //        SharedPreferences usageData = this.getSharedPreferences("USAGE_DATA", 0);
        //        SharedPreferences.Editor editor3 = usageData.edit();
        //        editor3.clear();
        //        editor3.commit();
        //
        //        for (int i = 0; i < Math.min(packageKeys.length, friendlyNameAppValues.length); ++i) {
        //            editor1.putString(packageKeys[i], friendlyNameAppValues[i]);
        //
        //            if(!sharedPrefAppsMonitored.getBoolean(packageKeys[i], false)) //if key/value doesn't exist create it otherwise do nothing
        //                editor2.putBoolean(packageKeys[i], false);
        //        }
        //        editor1.commit();
        //        editor2.commit();

        //Store Firebase UID
        // Save username for later usage
        //        SharedPreferences loginInfo = this.getSharedPreferences("LOGIN", 0);
        //        SharedPreferences.Editor editor3 = loginInfo.edit();
        //        editor3.clear();
        //        editor3.commit();


        //editor3.putString("userID", uid);
        //editor3.commit();




}








    /**
     * Inflates LoginFragment which gives the option to inflate
     * SignUpFragment, and leads back to MainMenu (here) after
     * a successful login.
     */
    private fun inflateFirebaseLogin() {
        val manager = supportFragmentManager
        val trans = manager.beginTransaction()
        val fragment = LoginFragment()
        trans.add(R.id.frame, fragment, LoginFragment.FRAGMENT_TAG).addToBackStack("tag")
        trans.commit()
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    //Method that starts notification
    fun modifyNotification(runState: String) {

        val textContent: String
        when (runState) {
            "start" -> textContent = "ScreenTime is Running"
            "stop" -> textContent = "ScreenTime is Idle"
            else -> textContent = "ScreenTime is Idle"
        }


        val textTitle = "ScreenTime"

        val mBuilder = NotificationCompat.Builder(this, "123")
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(this)
        val notificationId = 123
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, mBuilder.build())
    }

    // Method that creates notification
//    private fun createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = getString(R.string.channel_name)
//            val description = getString(R.string.channel_description)
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel("123", name, importance)
//            channel.description = description
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            val notificationManager = getSystemService(NotificationManager::class.java)
//            notificationManager.createNotificationChannel(channel)
//        }
//    }

    //One of two methods for getting system level permissions for foreground process checking
//    internal fun requestUsageStatsPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !hasUsageStatsPermission(
//                this
//            )
//        ) {
//            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
//        }
//    }

    //Two of two methods for getting system level permissions for foreground process checking
    @TargetApi(Build.VERSION_CODES.KITKAT)
    internal fun hasUsageStatsPermission(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            "android:get_usage_stats",
            android.os.Process.myUid(), context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }


}
