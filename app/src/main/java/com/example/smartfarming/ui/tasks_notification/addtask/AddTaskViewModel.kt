package com.example.smartfarming.ui.tasks_notification.addtask

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfarming.data.repositories.garden.GardenRepo
import com.example.smartfarming.data.room.entities.Garden
import com.example.smartfarming.data.room.entities.Task
import com.example.smartfarming.utils.PersianCalender
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(private val repo : GardenRepo) : ViewModel(){
    var step = mutableStateOf(0)
    val MAX_STEP = 4
    val gardensList = mutableStateOf<List<Garden>>(listOf())
    val gardenNameList = mutableStateListOf<String>()
    val selectedGarden = mutableStateOf("")
    val selectedActivity = mutableStateOf("")
    val showCustomActivity = mutableStateOf(false)
    val customActivity = mutableStateOf("")
    val remainingDays = mutableStateOf(0)
    val description = mutableStateOf("")
    val day = mutableStateOf("")
    val month = mutableStateOf("")
    val year = mutableStateOf("")

    init {
        getGardens()
    }

    private fun getGardens(){
        viewModelScope.launch {
            repo.getGardens().collect{
                gardensList.value = it
                Log.i("xxGarden1", "${it}")
                populateGardensName()
            }
        }
    }

    private fun populateGardensName(){
        for (g in gardensList.value){
            Log.i("xxGarden", "${g.name}")
            gardenNameList.add(g.name)
        }
    }

    fun setDescription(txt : String){
        description.value = txt
    }

    fun setRemainingDays(num : Int){
        remainingDays.value = num
        calculateFinishDate()
    }

    fun setCustomActivity(txt : String){
        customActivity.value = txt
    }

    fun increaseStep(){
        if (step.value < MAX_STEP) step.value++
    }

    fun decreaseStep(){
        if (step.value > 0) step.value--
    }

    fun setSelectedGarden(garden: String){
        selectedGarden.value = garden
    }

    fun handleActivitySelection(activity: String){
        setSelectedActivity(activity)
        if (isOtherActivity()){
            showCustomActivity.value = true
        } else {
            increaseStep()
        }
    }

    private fun setSelectedActivity(activity : String){
        selectedActivity.value = activity
    }

    private fun isOtherActivity() : Boolean{
        return selectedActivity.value == "سایر"
    }

    private fun calculateFinishDate(){
        val todayDate = PersianCalender.getCurrentDatePlusDays(remainingDays.value)
        day.value = todayDate["day"].toString()
        month.value = todayDate["month"].toString()
        year.value = todayDate["year"].toString()
    }

    fun submitClickHandler(){
        if (step.value == MAX_STEP - 1){
            insertTask2Db()
        } else {
            increaseStep()
        }
    }

    private fun insertTask2Db(){
        viewModelScope.launch {
            repo.insertTask(
                Task(
                    0,
                    getActivityName(),
                    selectedGarden.value,
                    description.value,
                    selectedActivity.value,
                    getStartDate(),
                    getFinishDate(),
                    recommendations = "",
                    0,
                    false
                )
            )
        }
    }

    private fun getActivityName() : String{
        if (showCustomActivity.value){
            return customActivity.value
        }
        return selectedActivity.value
    }

    private fun getStartDate() : String{
        val currentDate = PersianCalender.getShamsiDateMap()
        return "${currentDate["year"]}/${currentDate["month"]}/${currentDate["day"]}"
    }

    private fun getFinishDate() : String {
        return "$year/$month/$day"
    }
}