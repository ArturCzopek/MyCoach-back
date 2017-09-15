package pl.arturczopek.mycoach.service

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.arturczopek.mycoach.exception.DuplicatedNameException
import pl.arturczopek.mycoach.exception.InvalidDateException
import pl.arturczopek.mycoach.exception.InvalidFlowException
import pl.arturczopek.mycoach.exception.WrongPermissionException
import pl.arturczopek.mycoach.model.add.NewCycle
import pl.arturczopek.mycoach.model.add.NewSet
import pl.arturczopek.mycoach.model.database.Cycle
import pl.arturczopek.mycoach.model.database.ExerciseSession
import pl.arturczopek.mycoach.model.database.Set
import pl.arturczopek.mycoach.model.preview.CyclePreview
import pl.arturczopek.mycoach.repository.*

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Service
class CycleService(
        val cycleRepository: CycleRepository,
        val dateService: DateService,
        val exerciseSessionRepository: ExerciseSessionRepository,
        val exerciseRepository: ExerciseRepository,
        val trainingRepository: TrainingRepository,
        val seriesRepository: SeriesRepository,
        val setRepository: SetRepository,
        val dictionaryService: DictionaryService
) {

    @Cacheable(value = "activeCycle", key = "#userId")
    fun getActiveCycle(userId: Long) = cycleRepository.findOneByUserIdAndIsFinishedFalse(userId)

    @Cacheable(value = "cyclePreviews", key = "#userId")
    fun getCyclePreviews(userId: Long) = cycleRepository.findAllByUserIdOrderByIsFinishedDescEndDateAsc(userId).map { CyclePreview.buildFromCycle(it) }

    @Cacheable(value = "cycle", key = "#userId + ' ' + #cycleId")
    fun getCycleById(cycleId: Long, userId: Long): Cycle {
        val cycle = cycleRepository.findOne(cycleId)

        if (cycle.userId != userId) {
            throw WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).value)
        }

        cycle.sets.forEach {
            val nonSortedTrainings = it.trainings.toMutableList()   // order with non-sorted is needed later
            it.trainings = it.trainings.sortedBy { it.trainingDate }

            it.exercises.forEachIndexed { i, exercise ->
                val nonSortedSessions = exercise.exerciseSessions
                exercise.exerciseSessions.sortWith(Comparator<ExerciseSession> { s1, s2 ->
                    val s1Index = nonSortedSessions.indexOf(s1)
                    val s2Index = nonSortedSessions.indexOf(s2)
                    val t1 = nonSortedTrainings[s1Index]
                    val t2 = nonSortedTrainings[s2Index]
                    if (t1.trainingDate > t2.trainingDate) 1 else -1
                })
            }
        }

        return cycle
    }

    fun hasUserEveryCycleFinished(userId: Long) = cycleRepository.countByUserIdAndIsFinishedFalse(userId) == 0L

    @Transactional
    @Caching(evict = arrayOf(
            CacheEvict(value = "cyclePreviews", key = "#userId"),
            CacheEvict(value = "activeCycle", key = "#userId")
    ))
    fun addCycle(newCycle: NewCycle, userId: Long) {
        when {
            !isNewCycleDateValid(newCycle, userId) -> throw InvalidDateException(dictionaryService.translate("page.trainings.cycle.error.invalidDates.message", userId).value)
            !areNewSetsNamesValid(newCycle.sets) -> throw DuplicatedNameException(dictionaryService.translate("page.trainings.cycle.error.invalidSetNames.message", userId).value)
            !canCycleBeActive(userId) -> throw InvalidFlowException(dictionaryService.translate("page.trainings.cycle.error.cannotActivate.message", userId).value)
            else -> {
                val cycle = Cycle(startDate = newCycle.startDate ?: dateService.currentDate, userId = userId)

                cycleRepository.save(cycle)

                newCycle.sets
                        .map { Set().apply { setName = it.setName; cycleId = cycle.cycleId } }
                        .forEach { cycle.sets + it }

                cycleRepository.save(cycle)
            }
        }
    }

    @Caching(evict = arrayOf(
            CacheEvict(value = "cycle", key = "#userId + ' ' + #cycle.cycleId"),
            CacheEvict(value = "cyclePreviews", key = "#userId"),
            CacheEvict(value = "activeCycle", key = "#userId")
    ))
    fun deleteCycle(cycle: Cycle, userId: Long) {
        val cycleFromDb = cycleRepository.findOne(cycle.cycleId)

        if (cycleFromDb.userId != userId) {
            throw WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).value)
        }

        cycleFromDb.sets.forEach { set ->
            run {
                set.trainings.forEach { trainingRepository.delete(it.trainingId) }
                set.exercises.forEach { exercise ->
                    run {
                        exercise.exerciseSessions
                                .forEach { session ->
                                    run {
                                        session.series.forEach { seriesRepository.delete(it.seriesId) }
                                        exerciseSessionRepository.delete(session.exerciseSessionId)
                                    }
                                }
                        exerciseRepository.delete(exercise.exerciseId)
                    }
                }
                setRepository.delete(set.setId)
            }
        }

        cycleRepository.delete(cycleFromDb.cycleId)
    }


    @Caching(evict = arrayOf(
            CacheEvict(value = "cycle", key = "#userId + ' ' + #cycle.cycleId"),
            CacheEvict(value = "cyclePreviews", key = "#userId"),
            CacheEvict(value = "activeCycle", key = "#userId")
    ))
    fun updateCycle(cycle: Cycle, userId: Long) {
        val cycleFromDb = cycleRepository.findOne(cycle.cycleId)

        when {
            (cycleFromDb.userId != userId) -> throw WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).value)
            (!isCycleToUpdateDateValid(cycle, cycleFromDb, userId)) -> throw InvalidDateException(dictionaryService.translate("page.trainings.cycle.error.coveringDates.message", userId).value)
            (cycle.isFinished && !isCycleToCloseDateValid(cycle)) -> throw InvalidDateException(dictionaryService.translate("page.trainings.cycle.error.earlyEndDate.message", userId).value)
            // if we want to active cycle we need to make sure if there is any active cycle
            (!cycle.isFinished && !canCycleBeActive(userId)) -> throw InvalidFlowException(dictionaryService.translate("page.trainings.cycle.error.cannotActivate.message", userId).value)
            else -> {
                cycleFromDb.apply {
                    startDate = cycle.startDate
                    isFinished = cycle.isFinished
                    endDate = if (cycle.isFinished) cycle.endDate ?: dateService.currentDate else null
                }

                cycleRepository.save(cycleFromDb)
            }
        }
    }

    private fun isCycleToCloseDateValid(cycle: Cycle) = setRepository.findAllByCycleId(cycle.cycleId)
            .map { trainingRepository.findFirstBySetIdOrderByTrainingDateDesc(it.setId) }
            .any { it.trainingDate > cycle.endDate }

    private fun isNewCycleDateValid(newCycle: NewCycle, userId: Long): Boolean {
        val cycleFromDb = cycleRepository.findFirstByUserIdOrderByEndDateDesc(userId) ?: return true

        val dateToValidate = newCycle.startDate ?: dateService.currentDate

        if (dateToValidate < cycleFromDb.endDate) {
            return false
        }

        return dateToValidate != cycleFromDb.endDate
    }

    private fun canCycleBeActive(userId: Long) = getActiveCycle(userId) == null

    private fun areNewSetsNamesValid(sets: List<NewSet>): Boolean {
        // set contains unique elements. If set of sets is the same length as list of sets, everything is unique and is ok
        val setOfSets = HashSet<String>()
        sets.forEach { setOfSets + it.setName }
        return setOfSets.size === sets.size
    }

    private fun isCycleToUpdateDateValid(updateCycle: Cycle, currentCycle: Cycle, userId: Long): Boolean {
        val currentPreviousCycle = cycleRepository.findFirstByUserIdAndEndDateBeforeOrderByEndDateDesc(userId, currentCycle.startDate)
        var currentNextCycle: Cycle? = null

        currentCycle.endDate?.let {
            currentNextCycle = cycleRepository.findFirstByUserIdAndStartDateAfterOrderByStartDate(userId, currentCycle.endDate!!)
        }

        // it's first cycle, we can set dates as we want
        if (currentPreviousCycle == null && currentNextCycle == null) {
            return true
        }

        // new next/previous cycles should be the same, otherwise we would have covering dates or swap cycles, but for now it
        // is not handled
        val newPreviousCycle = cycleRepository.findFirstByUserIdAndEndDateBeforeOrderByEndDateDesc(userId, updateCycle.startDate)

        if ((currentPreviousCycle == null && newPreviousCycle != null) ||
                (currentPreviousCycle != null && newPreviousCycle == null)
                ) {
            return false
        }

        // both are not null and has different ids
        if (currentPreviousCycle != null && currentPreviousCycle.cycleId != newPreviousCycle.cycleId) {
            return false
        }

        var newNextCycle: Cycle? = null

        updateCycle.endDate?.let {
            newNextCycle = cycleRepository.findFirstByUserIdAndStartDateAfterOrderByStartDate(userId, updateCycle.endDate!!)
        }

        if (currentNextCycle != null && newNextCycle != null && currentNextCycle?.cycleId != newNextCycle?.cycleId) {
            return false
        }

        return true
    }
}