package com.example.smartfarming.ui.harvest

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.res.stringArrayResource
import androidx.lifecycle.*
import com.example.smartfarming.R
import com.example.smartfarming.data.repositories.garden.GardenRepo
import com.example.smartfarming.data.room.entities.Garden
import com.example.smartfarming.data.room.entities.Harvest
import com.example.smartfarming.utils.HARVEST_TYPE
import com.example.smartfarming.utils.PersianCalender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class HarvestViewModel(val repo : GardenRepo) : ViewModel() {

    var harvestDate =
        mutableStateOf(mutableMapOf("day" to "", "month" to "", "year" to ""))

    var selectedGarden = MutableLiveData<String>("")
    var harvestWeight = MutableLiveData<Float>()
    var harvestType = MutableLiveData<String>().apply {
        value = HARVEST_TYPE[0]
    }
    var thisYear = PersianCalender.getShamsiDateMap()["year"].toString()
    var _harvestList = mutableStateOf<List<Harvest>>(emptyList())
    val mHarvestList = mutableStateListOf<Harvest>()
    var selectedYear = mutableStateOf(thisYear)
    var selectedType = mutableStateOf("همه")

    fun setDate(date : MutableMap<String, String>){
        harvestDate.value = date
    }

    fun getGardens() : LiveData<List<Garden>>{
        var list = liveData<List<Garden>> {  }

        viewModelScope.launch() {
            list = repo.getGardens().asLiveData()
        }
        return list
    }

    fun addHarvest2DB(harvest: Harvest){
        viewModelScope.launch {
            repo.insertHarvest(harvest)
        }
    }

    fun getHarvestByGardenName(gardeName : String){
        viewModelScope.launch {
            repo.getHarvestByGardenName(gardeName).collect{
                _harvestList.value = it
            }
        }
    }

    fun getHarvestByYear(gardeName: String, year: String){
        viewModelScope.launch {
            _harvestList.value = repo.getHarvestByYear(gardeName, year)
        }
    }

    fun getHarvestByYearType(gardeName: String, year: String, type : String){
        viewModelScope.launch {
            _harvestList.value = repo.getHarvestByYearType(gardeName, year, type)
        }
    }

    fun getHarvestByType(gardeName: String, type : String){
        viewModelScope.launch {
            _harvestList.value = repo.getHarvestByType(gardeName, type)
        }
    }

    fun getYearSum(year:String) : Double{
        var sum = 0.0
        if (_harvestList.value.isNullOrEmpty()){
            sum = 0.0
        } else {
            for (h in _harvestList.value){
                if (h.year == year){
                    sum += h.weight
                }
            }

        }
        return sum
    }

    fun deleteHarvestItem(harvest: Harvest){
        viewModelScope.launch {
            repo.deleteHarvest(harvest)
        }
    }

}

class HarvestViewModelFactory(val repo : GardenRepo) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HarvestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HarvestViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}