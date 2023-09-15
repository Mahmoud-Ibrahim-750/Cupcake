package com.mis.route.cupcake.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// a top level constants
// TODO: what is the difference between levels? and what are they?
private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel : ViewModel() {

    // properties
    private var _orderQuantity = MutableLiveData<Int>()
    private var _orderFlavor = MutableLiveData<String>()
    private var _orderPickupDate = MutableLiveData<String>()
    private var _orderPrice = MutableLiveData<Double>()
    val dateOptions = getPickupOptions()

    // backing properties
//    val orderQuantity: LiveData<Int>
//        get() = _orderQuantity
    val orderQuantity: LiveData<Int> = _orderQuantity // concise syntax
    val orderFlavor: LiveData<String> = _orderFlavor
    val orderPickupDate: LiveData<String> = _orderPickupDate
    val orderPrice: LiveData<String> = Transformations.map(_orderPrice) {
            NumberFormat.getCurrencyInstance().format(it)
        }

    // setter functions
    fun setQuantity(numberCupcakes: Int) {
        _orderQuantity.value = numberCupcakes
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String) {
        _orderFlavor.value = desiredFlavor
    }

    fun setDate(pickupDate: String) {
        _orderPickupDate.value = pickupDate
        updatePrice()
    }

    // other functions
//    fun hasNoFlavorSet(): Boolean {
//        return _orderFlavor.value.isNullOrEmpty()
//    }
    fun hasNoFlavorSet(): Boolean = _orderFlavor.value.isNullOrEmpty() // concise syntax

    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault()) // E MMM d -> Tue Dec 10
        val calendar = Calendar.getInstance()
        repeat(4) {
            options.add(formatter.format(calendar.time)) // format and add
            calendar.add(Calendar.DATE, 1) // increment the calendar by 1 day
        }
        return options
    }

    fun resetOrder() {
        _orderQuantity.value = 0
        _orderFlavor.value = ""
        _orderPickupDate.value = dateOptions[0]
        _orderPrice.value = 0.0
    }

    // initialize order
    init {
        resetOrder()
    }

    private fun updatePrice() {
        var calculatedPrice = (orderQuantity.value ?: 0) * PRICE_PER_CUPCAKE
        if (_orderPickupDate.value.equals(dateOptions[0])) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _orderPrice.value = calculatedPrice
    }
}