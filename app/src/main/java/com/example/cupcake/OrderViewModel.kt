package com.example.cupcake

import androidx.lifecycle.*
import java.text.NumberFormat

class OrderViewModel: ViewModel() {
  val quantity = MutableLiveData<Int>()
  //val quantity: LiveData<Int> get() = _quantity

  val flavor = MutableLiveData<String>()
  //val flavor: LiveData<String> get() = _flavor

  val pickupDate = MutableLiveData<String>()
  //val pickupDate: LiveData<String> get() = _pickupDate

  private val _pickupDateOptions = MutableLiveData<List<String>>()
  val pickupDateOptions: LiveData<List<String>>
    get() = _pickupDateOptions

  private var cupcakePrice = 0.0
  private var shippingPrice = 0.0
  private val _price = MediatorLiveData<Double>().apply {
    addSource(quantity) {
      cupcakePrice = it?.times(Const.PRICE_PER_CUPCAKE) ?: 0.0
      value = cupcakePrice + shippingPrice
    }
    addSource(pickupDate) {
      shippingPrice = if(it == null || it != _pickupDateOptions.value?.firstOrNull()) 0.0
      else Const.PRICE_FOR_SAME_DAY_PICKUP
      value = cupcakePrice + shippingPrice
    }
  }
  val priceStr: LiveData<String> = Transformations.map(_price) {
    if(it == null) null
    else NumberFormat.getCurrencyInstance().format(it)
  }

  init {
    init()
  }

  fun init() {
    quantity.value = null
    flavor.value = null
    pickupDate.value = null
    _price.value = null
    cupcakePrice = 0.0
    shippingPrice = 0.0
    setPickupOptions()
  }

  private fun setPickupOptions() {
    _pickupDateOptions.value = Util.get5DateFurther()
  }

  /*
  ===========
  Setter to be accessed from XML with data binding
  ===========
   */

  fun setFlavor(v: String) {
    flavor.value = v
  }
  fun setQuantity(v: Int) {
    quantity.value = v
  }
}