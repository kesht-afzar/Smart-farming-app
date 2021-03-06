package com.example.smartfarming.ui.harvest

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.smartfarming.data.repositories.garden.GardenRepo
import com.example.smartfarming.data.room.entities.Garden
import com.example.smartfarming.data.room.entities.Harvest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class HarvestViewModel(val repo : GardenRepo) : ViewModel() {

    var harvestDate =
        mutableStateOf(mutableMapOf("day" to "", "month" to "", "year" to ""))
    var selectedGarden = MutableLiveData<String>("")
    var harvestWeight = MutableLiveData<Float>()
    var harvestType = MutableLiveData<String>()

    var harvestList = MutableLiveData<List<Harvest>>(listOf())
    var selectedYear = mutableStateOf("1401")
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
                harvestList.value = it
            }
        }
    }

    fun getHarvestByYear(gardeName: String, year: String){
        viewModelScope.launch {
            harvestList.value = repo.getHarvestByYear(gardeName, year)
        }
    }

    fun getHarvestByYearType(gardeName: String, year: String, type : String){
        viewModelScope.launch {
            harvestList.value = repo.getHarvestByYearType(gardeName, year, type)
        }
    }

    fun getHarvestByType(gardeName: String, type : String){
        viewModelScope.launch {
            harvestList.value = repo.getHarvestByType(gardeName, type)
        }
    }

    fun getYearSum(year:String) : Double{
        var sum = 0.0
        if (harvestList.value.isNullOrEmpty()){
            sum = 0.0
        } else {
            for (h in harvestList.value!!){
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
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HarvestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HarvestViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}