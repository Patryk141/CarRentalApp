package com.example.carrental.Fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.carrental.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*


class MapsFragment : Fragment(), OnMapReadyCallback {

  private lateinit var mMap: GoogleMap
  private lateinit var db : FirebaseFirestore
  private lateinit var requestPermissionLauncher : ActivityResultLauncher<Array<String>>
  private var location : Location? = null
  private lateinit var locationProviderClient : FusedLocationProviderClient
  private lateinit var mapSearchView : SearchView
  private lateinit var mapFragment : SupportMapFragment
  private var marker: Marker? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_maps, container, false)
    mapSearchView = view.findViewById(R.id.search_view)
    mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    locationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    db = Firebase.firestore

    requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
      val fineLocationGranted =
        permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
      val coarseLocationGranted =
        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
      if(fineLocationGranted || coarseLocationGranted) {
        getLastLocation() // udzielono uprawnień
      } else {
        Toast.makeText(requireContext(), "Location permission is denied 🤖🤖🤖", Toast.LENGTH_SHORT).show()
      }
    }

    getLastLocation() // getting the location from the user and
    mapFragment.getMapAsync(this)

    mapSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        if (marker != null) {
          marker!!.remove()
        }
        val location = mapSearchView.query.toString()
        val geocoder = Geocoder(requireContext())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
          if (location != null) {
            val geocoderListener = object : Geocoder.GeocodeListener {
              override fun onGeocode(addresses: MutableList<Address>) {
                var list = mutableListOf<Address>()
                for (address in addresses) {
                  // Wykonaj operacje na pojedynczym adresie
                  val latitude = address.latitude
                  val longitude = address.longitude
                  address.latitude = latitude
                  address.longitude = longitude
                  list.add(address)
                }

                if (list.isNotEmpty()) {
                  val address = list[0]
                  val LatLng = LatLng(address.latitude, address.longitude)
                  activity!!.runOnUiThread {
                    marker = mMap.addMarker(MarkerOptions().position(LatLng).title(location))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng, 9f))
                  }
                }
              }
            }
            var addressList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
              geocoder.getFromLocationName(location, 1, geocoderListener)
            } else {

            }
          }
        }
        else {
          var addressList: List<Address?>? = null
          try {
            addressList = geocoder.getFromLocationName(location, 1)
          } catch (e: IOException) {
            e.printStackTrace()
          }
          val address: Address? = addressList!!.get(0)
          val latLng = LatLng(address!!.getLatitude(), address!!.getLongitude())
          marker = mMap.addMarker(MarkerOptions().position(latLng).title(location))
          mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9f))
        }
        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        return false
      }
    })

  }

  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap
    mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    mMap.uiSettings.isZoomControlsEnabled = true
    mMap.uiSettings.isCompassEnabled = true
    mMap.uiSettings.isMyLocationButtonEnabled = true
    mMap.uiSettings.isIndoorLevelPickerEnabled = false

    if(location != null) {
      val latlng = LatLng(location!!.latitude, location!!.longitude)
      //    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
      mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng))
      mMap.addMarker(MarkerOptions().position(latlng).title("My current location"))
    } else {
      addCarMarkers()
    }
  }

  private fun getLastLocation() {
    if (ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_COARSE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
      return
    }

    val task: Task<Location> = locationProviderClient.lastLocation

    task.addOnSuccessListener { newLocation ->
      if(newLocation != null) {
        location = newLocation
        mapFragment.getMapAsync(this)
      }
    }
  }

  private fun bitmapDescriptorFromVector(context: Context, vectorResId : Int) : BitmapDescriptor? { // helper function to convert vector to bitmap
    return ContextCompat.getDrawable(context, vectorResId)?.run {
      setBounds(0, 0, intrinsicWidth, intrinsicHeight)
      val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
      draw(Canvas(bitmap))
      BitmapDescriptorFactory.fromBitmap(bitmap)
    }
  }

  private fun addCarMarkers() {

    GlobalScope.launch(Dispatchers.IO) {

      db.
        collection("carsData")
//        .document("0g6mGG1HYFcs98IKfUGo")
        .get()
        .addOnSuccessListener { querySnapshot ->
          for (documentSnapshot in querySnapshot) {
            val geoLocationColl = documentSnapshot.reference.collection("geolocation")
            geoLocationColl.get()
            .addOnSuccessListener { geolocationQuerySnapshot ->
                for(geoDocumentSnapshot in geolocationQuerySnapshot) {
                  val latitude = geoDocumentSnapshot.get("latitude").toString().toDouble()
                  val longitude = geoDocumentSnapshot.get("longitude").toString().toDouble()
                  if (latitude != null && longitude != null) {
                    val location = LatLng(latitude, longitude)
                    val options = MarkerOptions().position(location).title("Marker auta ${documentSnapshot.get("brand")} ${documentSnapshot.get("model")}")
                    options.icon(bitmapDescriptorFromVector(requireContext(), R.drawable.maps_marker))
                    mMap.addMarker(options)
                  }
                }
            }
            .addOnFailureListener {
              println(it.message)
            }
          }
        }
        .addOnFailureListener {
          println(it.message)
        }
    }
  }
}