package github.earth.placesscreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import github.earth.R
import github.earth.placesscreen.dialogs.map_edit_dialog
import github.earth.dialog_fragments.map_info_dialog
import github.earth.room.data.places_data.PlacesRoom
import github.earth.room.places_room.PlacesViewModel
import java.util.*

//internal — любой клиент внутри модуля, который видит объявленный класс, видит и его internal члены
class PlacesFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnPoiClickListener,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mPlacesViewModel: PlacesViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_places, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        mPlacesViewModel = ViewModelProvider(this).get(PlacesViewModel::class.java)
        var hear_me: ImageView? = view?.findViewById(R.id.hear_me)
        mMap = googleMap
        mMap.setMinZoomPreference(5.0f)//минимальный масштаб

        try {
            mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                override fun onMarkerDragStart(marker: Marker) {

                }

                override fun onMarkerDragEnd(marker: Marker) {
                    marker.tag = "marker_confirm"
                    marker.isDraggable = false

                    updateItem(marker.id.replace("m", "").toInt(),
                        marker.position.latitude,
                        marker.position.longitude,
                        marker.title,
                        marker.snippet)
                    showConfirmDialog()
                }

                override fun onMarkerDrag(marker: Marker) {}
            })
        } catch (ex: Exception) {
            Toast.makeText(requireContext(), "Error: " + ex.message, Toast.LENGTH_SHORT)
                .show()
        }

        mMap.setOnMyLocationClickListener {
            Toast.makeText(requireContext(), "My location", Toast.LENGTH_SHORT).show()
        }

        mMap.setOnMarkerClickListener { marker ->
            if (getAdress(marker.position.latitude,
                    marker.position.longitude)?.thoroughfare != null && getAdress(marker.position.latitude,
                    marker.position.longitude)?.subThoroughfare != null
            ) {
                marker.snippet = getAdress(marker.position.latitude,
                    marker.position.longitude)?.thoroughfare + ", " + getAdress(marker.position.latitude,
                    marker.position.longitude)?.subThoroughfare
            } else {
                marker.snippet =
                    getAdress(marker.position.latitude, marker.position.longitude)?.locality
            }

            false
        }

        mMap.setOnInfoWindowClickListener { marker ->
            deletePlace(marker.id.replace("m", "").toInt(),
                marker.position.latitude,
                marker.position.longitude)
            marker.remove()
            showEditDialog(marker)
        }

        val fine_status =
            ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse_status =
            ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)

        //проверка на наличие разрешений
        if (fine_status == PackageManager.PERMISSION_GRANTED && coarse_status == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = false
            Log.d("INFO: ", "PERMISSION GRANTED")
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),
                targetRequestCode)
        }

        val userMarker: ImageView? = view?.findViewById(R.id.marker)
        userMarker?.setOnClickListener {
            var marker = mMap.addMarker(
                MarkerOptions()
                    .position(mMap.cameraPosition.target)
                    .draggable(true)
                    .title("Место")
                    .snippet("Улица")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
            insertMarkersToDatabase(marker.id.replace("m", "").toInt(),
                marker.position.latitude,
                marker.position.longitude)

        }
        try {
            var listPlaces = listOf(mPlacesViewModel.readAllData)
            listPlaces.forEach { liveData ->
                for (i in 0..listPlaces.size) {
                    mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(liveData.value!![i].lattitude,
                                liveData.value!![i].longtitude))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    )
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(requireContext(), "eror: " + ex.message, Toast.LENGTH_SHORT).show()
        }

        //my geoloaction
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        var my_latitude = 55.45
        var my_longtitude = 37.36
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    my_latitude = location.latitude
                }
                if (location != null) {
                    my_longtitude = location.longitude
                }
                var myLocation = LatLng(my_latitude, my_longtitude)
                mMap.addMarker(
                    MarkerOptions()
                        .position(myLocation)
                        .draggable(true)
                        .title("Моё место")
                        .snippet("musor")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                )

                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))

                //получить точную геопозицию (таймер стоит чтобы обновлять координаты)
                hear_me?.setOnClickListener {
                    val timer = Timer()
                    timer.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            requireActivity().runOnUiThread {
                                fusedLocationClient.lastLocation
                                    .addOnSuccessListener { location: Location? ->
                                        if (location != null) {
                                            my_latitude = location.latitude
                                        }
                                        if (location != null) {
                                            my_longtitude = location.longitude
                                        }
                                        myLocation = LatLng(my_latitude, my_longtitude)
                                    }
                            }
                        }
                    }, 0, 1000)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))

                }


            }


        /*
        Функция moveCamera центрирует карту по координатам LatLng (Сидней, Австралия).
         Как правило, при добавлении карты первым делом нужно изменить настройки местоположения и камеры:
         угол обзора, ориентацию карты, масштаб и т. п
         */


    }


    override fun onPoiClick(poi: PointOfInterest) {
        Toast.makeText(PlacesFragment().context,
            """Clicked: ${poi.name}
            Place ID:${poi.placeId}
            Latitude:${poi.latLng.latitude}
             Longitude:${poi.latLng.longitude}""",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(requireContext(), "MyLocation button clicked", Toast.LENGTH_SHORT)
            .show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(requireContext(), "Current location:\n$location", Toast.LENGTH_LONG)
            .show()
    }

    override fun onMarkerDragStart(marker: Marker) {
        marker.snippet = marker.position.latitude.toString()
    }

    override fun onMarkerDrag(marker: Marker) {
        marker.snippet = marker.position.latitude.toString()
    }

    override fun onMarkerDragEnd(marker: Marker) {
        marker.snippet = marker.position.latitude.toString()
        showConfirmDialog()

    }

    override fun onMarkerClick(marker: Marker): Boolean {
        return false
    }

    override fun onInfoWindowClick(marker: Marker) {
        showConfirmDialog()
    }

    fun showConfirmDialog() {

        val dialogFragment = map_info_dialog()
        val manager = fragmentManager
        dialogFragment.show(manager!!, "map_info_dialog")

    }

    fun showEditDialog(marker: Marker) {

        val dialogFragment = map_edit_dialog()
        val manager = fragmentManager
        dialogFragment.show(manager!!, "map_edit_dialog")
    }

    //geocoding
    private fun getAdress(latitude: Double, longitude: Double): Address? {
        val geoCoder = Geocoder(requireContext())
        val matches: List<Address> = geoCoder.getFromLocation(latitude, longitude, 1)
        val bestMatch: Address? = if (matches.isEmpty()) null else matches[0]
        return bestMatch
    }

    fun insertMarkersToDatabase(
        id: Int,
        latitude: Double,
        longitude: Double,
    ) {

        //create user object
        val places = PlacesRoom(
            id,
            latitude,
            longitude
        )

        //add data to db
        mPlacesViewModel.addPlaces(places)

    }

    fun updateItem(
        id: Int,
        latitude: Double,
        longitude: Double,
        title: String,
        snippets: String,
    ) {
        //create stats object
        val updatePlaces = PlacesRoom(
            id,
            latitude,
            longitude
        )
        //update
        mPlacesViewModel.updatePlaces(updatePlaces)

    }


    fun deleteAllStats() {
        mPlacesViewModel.deleteAllPlaces() //удалить всё
    }

    fun deletePlace(
        id: Int,
        latitude: Double,
        longitude: Double,
    ) {
        val deletePlace = PlacesRoom(
            id,
            latitude,
            longitude
        )
        mPlacesViewModel.deletePlaces(deletePlace)
    }

}