package com.example.smartfarming.data.repositories.garden

import androidx.annotation.WorkerThread
import com.example.smartfarming.data.room.daos.*
import com.example.smartfarming.data.room.entities.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GardenRepo @Inject constructor(
    private val gardenDao : GardenDao,
    private val taskDao : TaskDao,
    private val irrigationDao : IrrigationDao,
    private val harvestDao: HarvestDao,
    private val fertilizationDao: FertilizationDao,
    private val pesticideDao: PesticideDao,
    private val otherActivitiesDao: OtherActivitiesDao
) {

    // Garden repo
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getGardens() : Flow<List<Garden>> {
        return gardenDao.getAllGardens()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertGarden(garden: Garden){
        gardenDao.insert(garden)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getGardenByName(gardenName : String) : Garden {
        return gardenDao.getGardenByName(gardenName)
    }

    @WorkerThread
    suspend fun getGardenById(gardenId : Int) : Garden {
        return gardenDao.getGardenById(gardenId)
    }

    @WorkerThread
    suspend fun updateGarden(garden: Garden) {
        gardenDao.updateGarden(garden)
    }

    // Task repo
    @WorkerThread
    suspend fun insertTask(task : Task){
        taskDao.insert(task)
    }

    @WorkerThread
    fun getTasksForGarden(gardenIds: Int) : Flow<List<Task>>{
        /**
         * Returns the tasks that are not done
         */
        return taskDao.getTasksForGarden("%${gardenIds}%")
    }

    @WorkerThread
    suspend fun getAllTasks() :  Flow<List<Task>>{
        return taskDao.getAllTasks()
    }

    @WorkerThread
    fun getAllUndoneTasks() : Flow<List<Task>> {
        return taskDao.getAllUndoneTasks()
    }

    @WorkerThread
    suspend fun deleteTask(task: Task) {
        taskDao.delete(task)
    }

    @WorkerThread
    suspend fun updateTask(taskId: Int){
        taskDao.updateTaskStatus(taskId)
    }

    @WorkerThread
    suspend fun updateTaskStatus(taskId: Int, status : String){
        taskDao.setTaskStatus(taskId, status)
    }

    // Irrigation repo
    @WorkerThread
    suspend fun insertIrrigation(irrigationEntity: IrrigationEntity){
        irrigationDao.insert(irrigationEntity)
    }

    @WorkerThread
    suspend fun getIrrigationByGardenName(gardenName : String) : List<IrrigationEntity> {
        return irrigationDao.getIrrigationByGardenName(gardenName)
    }

    @WorkerThread
    suspend fun getIrrigationByGardenYear(gardenName: String, year: String) : List<IrrigationEntity>{
        return irrigationDao.getIrrigationByGardenYear(gardenName, "%$year%")
    }

    @WorkerThread
    suspend fun deleteIrrigation(irrigationEntity: IrrigationEntity){
        irrigationDao.delete(irrigationEntity)
    }

    @WorkerThread
    fun getAllIrrigation() = irrigationDao.getAllIrrigation()

    // Harvest
    @WorkerThread
    suspend fun getHarvestByYear(gardenName: String, year: String) : List<Harvest> {
        return harvestDao.getHarvestByYear(gardenName, year)
    }

    @WorkerThread
    suspend fun getHarvestByType(gardenName: String, harvestType: String) : List<Harvest> {
        return harvestDao.getHarvestByType(gardenName, harvestType)
    }

    @WorkerThread
    suspend fun getHarvestByYearType(gardenName: String, year: String, harvestType: String) : List<Harvest> {
        return harvestDao.getHarvestByYearType(gardenName, year, harvestType)
    }

    @WorkerThread
    suspend fun insertHarvest(harvest: Harvest) {
        harvestDao.insertHarvest(harvest)
    }

    @WorkerThread
    suspend fun deleteHarvest(harvest: Harvest){
        harvestDao.deleteHarvest(harvest)
    }

    @WorkerThread
    suspend fun getHarvestByGardenName(gardenName : String) : Flow<List<Harvest>>{
        return harvestDao.getHarvestByGarden(gardenName)
    }

    //Fertilization
    @WorkerThread
    suspend fun insertFertilization(fertilizationEntity: FertilizationEntity) {
        fertilizationDao.insertFertilization(fertilizationEntity)
    }

    @WorkerThread
    fun getFertilizationByGardenId(gardenId: Int) : Flow<List<FertilizationEntity>>{
        return fertilizationDao.getFertilizationByGardenName(gardenId)
    }

    @WorkerThread
    suspend fun getFertilizationByGardenYear(gardenId: Int, year: String) : List<FertilizationEntity> {
        return fertilizationDao.getFertilizationByGardenNameYear(gardenId, "%$year%")
    }

    @WorkerThread
    suspend fun deleteFertilization(fertilizationEntity: FertilizationEntity){
        fertilizationDao.deleteFertilization(fertilizationEntity)
    }

    //Pesticide
    @WorkerThread
    fun getPesticidesByGardenName(gardenName: String) : Flow<List<PesticideEntity>>{
        return pesticideDao.getPesticideByGardenName(gardenName)
    }

    @WorkerThread
    suspend fun getPesticideByGardenNameYear(gardenName : String, year : String) : List<PesticideEntity> {
        return pesticideDao.getPesticideByGardenNameYear(gardenName, "%$year%")
    }

    @WorkerThread
    suspend fun insertPesticide(pesticideEntity: PesticideEntity){
        pesticideDao.insert(pesticideEntity)
    }

    @WorkerThread
    suspend fun deletePesticide(pesticideEntity: PesticideEntity){
        pesticideDao.delete(pesticideEntity)
    }

    //Other Activities
    @WorkerThread
    suspend fun insertOtherActivity(otherActivityEntity: OtherActivityEntity){
        otherActivitiesDao.insert(otherActivityEntity)
    }

    @WorkerThread
    suspend fun getOtherActivitiesByGardenId(gardenId: Int) : List<OtherActivityEntity> {
        return otherActivitiesDao.getOtherActivitiesByGardenId(gardenId)
    }

    @WorkerThread
    suspend fun getOtherActivityByGardenYear(gardenId: Int, year: String) : List<OtherActivityEntity>{
        return otherActivitiesDao.getOtherActivitiesByGardenYear(gardenId, "%$year%")
    }

    @WorkerThread
    suspend fun deleteOtherActivity(otherActivityEntity: OtherActivityEntity){
        otherActivitiesDao.delete(otherActivityEntity)
    }

    @WorkerThread
    suspend fun getAllOtherActivities() : List<OtherActivityEntity> {
        return otherActivitiesDao.getAllOtherActivities()
    }
}
