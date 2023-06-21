package net.vinceblas.middaratracker

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class TurnTrackerViewModel : ViewModel() {

    data class TurnTrackerUiState(
        val turnList: List<Combatant>,
        val undoList: ArrayDeque<List<Combatant>>,
        val availableCards: List<Combatant> = regularCards + hiddenCards,
        val selected: Combatant? = null,
        val pickerMode: Boolean = false,
        val shuffleInProgress: Boolean = false,
    )

    private val stateFlow = MutableStateFlow(TurnTrackerUiState(emptyList(), ArrayDeque()))
    val uiState: StateFlow<TurnTrackerUiState> = stateFlow.asStateFlow()

    suspend fun newRound() {
        stateFlow.update { currentState ->
            currentState.copy(
                turnList = currentState.turnList.shuffled(),
                undoList = currentState.undoList.apply { add(currentState.turnList) }, // technically can just mutate in place but this seems clearer
                shuffleInProgress = true
            )
        }
        // spam state a few more times to animate more, but don't update undo state
        stateFlow.update { currentState ->
            delay(400)
            currentState.copy(turnList = currentState.turnList.shuffled())
        }
        stateFlow.update { currentState ->
            delay(400)
            currentState.copy(
                turnList = currentState.turnList.shuffled(), shuffleInProgress = false
            )
        }
    }

    fun selectCard(combatant: Combatant) {
        stateFlow.update { state ->

            if (state.pickerMode) {
                val newList = state.turnList.toMutableList()

                if (state.turnList.contains(combatant)) {
                    newList.remove(combatant)
                } else {
                    if (newList.contains(state.selected)) {
                        newList.add(newList.indexOf(state.selected) + 1, combatant)
                    } else {
                        newList.add(combatant)
                    }
                }
                val allCards = state.availableCards.sortedBy { c ->
                    if (newList.contains(c))
                        newList.indexOf(c)
                    else
                        99
                }
                // sort selected to front. Since this otherwise preserves order this means recently used cards bubble to top

                state.copy(
                    turnList = newList,
                    availableCards = allCards
                )
            } else {

                if (state.selected == combatant) {
                    state.copy(
                        selected = null
                    )
                } else {
                    state.copy(
                        selected = combatant
                    )
                }
            }
        }
    }

    fun removeCard(combatant: Combatant) {
        stateFlow.update { state ->
            val selected = if (state.selected == combatant) {
                null
            } else {
                state.selected
            }
            state.copy(
                turnList = state.turnList.toMutableList().apply { remove(combatant) },
                selected = selected
            )
        }
    }

    fun nudge(combatant: Combatant, direction: Int) {
        stateFlow.update { state ->
            val newList = state.turnList.toMutableList()

            val idx = (newList.indexOf(combatant) + direction).coerceIn(0, newList.size-1)

            newList.remove(combatant)
            newList.add(idx, combatant)

            state.copy(
                turnList = newList
            )
        }
    }

    fun startPickerMode() {
        stateFlow.update { currentState ->
            currentState.copy(
                availableCards = currentState.availableCards.sortedBy { c ->
                    if (currentState.turnList.contains(c))
                        currentState.turnList.indexOf(c)
                    else
                        99
                },
                pickerMode = true
            )
        }
    }

    fun endPickerMode() {
        stateFlow.update { currentState ->
            currentState.copy(
                pickerMode = false,
                selected = if (currentState.turnList.contains(currentState.selected)) currentState.selected else null
                // clear selected state if it's orphaned
            )
        }
    }
}