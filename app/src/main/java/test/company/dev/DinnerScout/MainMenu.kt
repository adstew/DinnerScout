package test.company.dev.DinnerScout

import LoginFragment
import android.Manifest
import android.annotation.TargetApi
import android.app.AppOpsManager
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.Business
import com.yelp.fusion.client.models.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import test.company.dev.DinnerScout.fragments.PreferencesFragment
import test.company.dev.DinnerScout.fragments.SignupFragment


class MainMenu : AppCompatActivity(), View.OnClickListener,
    SignupFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener,
    PreferencesFragment.OnFragmentInteractionListener {
    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.preferencesButton -> {
                inflatePreferencesFragment()
            }
            R.id.findRestButton -> {
                generateRestaurant()


            }
        }
    }

    companion object {
        val API_KEY: String = "M0CMWi9fgorumQJ8FK5oEL7k8npWFFovJd7LP0WQ55zjcVO1Nkkiai_iaWWAuTKv5Qu-ZD3oUG4gprEAnrNo7GJxNUGK47vcWdbp1WDYPDjvRj8YA-bGcepEQ62rXXYx"
//        val NOTIFICATION_ID = 1
//        var uid: String? = null
//        var appsMonitoredKey: String? =
//            null    // allows creation of user unique sharedpref for checkboxes
    }


    // inside a basic activity


    private var locationManager: LocationManager? = null
    private lateinit var preferencesButton: Button
    private lateinit var findRestaurantButton: Button


    private var manager: FragmentManager? = null
    private val trans: FragmentTransaction? = null
    var business: Business? = null
    var businesses: ArrayList<Business?> = arrayListOf()
    private var lat: Double = 0.0
    private var long: Double = 0.0
    var params: HashMap<String, String> = HashMap()
    var apiFactory = YelpFusionApiFactory()
    var yelpFusionApi = apiFactory.createAPI(API_KEY)
    var change = Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inflateFirebaseLogin()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        preferencesButton = findViewById(R.id.preferencesButton)
        findRestaurantButton = findViewById(R.id.findRestButton)
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                    // Create persistent LocationManager reference
                    locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

                    try {
                        // Request location updates
                        locationManager?.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            0L,
                            0f,
                            locationListener
                        );
                    } catch (ex: SecurityException) {
                        Log.d("myTag", "Security Exception, no location available");
                    }
                }

            }).check();

        preferencesButton.setOnClickListener(this)
        findRestaurantButton.setOnClickListener(this)




    }


    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            long = location.longitude
            lat = location.latitude
            Log.d("myTag", String.format("UPDATED LOCATION %f,%f", lat, long));

        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
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

    private fun inflatePreferencesFragment(){


        val manager = supportFragmentManager
        val trans = manager.beginTransaction()
        val fragment = PreferencesFragment.newInstance(lat.toString(), long.toString())
        trans.replace(R.id.frame, fragment, PreferencesFragment.FRAGMENT_TAG).addToBackStack("tag")
        trans.commit()

    }

    fun generateRestaurant(){
        params.put("location", "Tallahassee, FL")
//        params.put("latitude", lat.toString())
//        params.put("longitutde", long.toString())
//        params.put("latitude", "37.786882")
//        params.put("longitude", "-122.399972")
        params.put("radius","4000")
        params.put("open_now","true")
//        params.put("price","1")


//        val call = yelpFusionApi.getBusinessSearch(params)
//        val response = call.execute()



        val call = yelpFusionApi.getBusinessSearch(params);
        call.enqueue(
            object : Callback<SearchResponse> {

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

                }
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.isSuccessful) {
//                    val searchResponse : SearchResponse? = call.execute().body()
                        businesses = response.body()?.businesses ?: arrayListOf()
                        Log.d("businessTag", businesses.size.toString())
                        var a = 1 + 1
                        var b = a + 1
                        val randomRestaurant : Int = (0 until businesses.size-1).shuffled().first()
                        Toast.makeText(applicationContext, businesses[randomRestaurant]?.name, Toast.LENGTH_SHORT).show()

                    }

                }
            })

    }
    override fun onFragmentInteraction(uri: Uri) {

    }

    // Method that starts notification
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


    // Two of two methods for getting system level permissions for foreground process checking
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