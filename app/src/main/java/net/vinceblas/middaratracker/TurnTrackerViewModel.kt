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
        stateFlow.update { state ->
            state.copy(
                turnList = state.turnList.shuffled(),
                undoList = state.undoList.apply { add(state.turnList) }, // technically can just mutate in place but this seems clearer
                shuffleInProgress = true
            )
        }
        // spam state a few more times to animate more, but don't update undo state
        // is there better more paradigmatic way to do this? Probably!
        // Am I going to rewrite it? Clearly not
        stateFlow.update { state ->
            delay(400)
            state.copy(turnList = state.turnList.shuffled())
        }
        stateFlow.update { state ->
            delay(400)
            state.copy(
                turnList = state.turnList.shuffled(), shuffleInProgress = false
            )
        }
    }

    fun selectCard(combatant: Combatant) {
        stateFlow.update { state ->
            if (state.pickerMode) {
                // in pickermode, turnlist is the selected list
                val newList = state.turnList.toMutableList()

                if (state.turnList.contains(combatant)) {
                    newList.remove(combatant)
                } else {
                    if (newList.contains(state.selected)) {
                        // assume selected combatant is current turn and insert immediately after them, per game rules
                        newList.add(newList.indexOf(state.selected) + 1, combatant)
                    } else {
                        newList.add(combatant)
                    }
                }

                // sort selected to front. Since this otherwise preserves order
                // this means unselected cards stay in recently used order
                val allCards = state.availableCards.sortedBy { c ->
                    if (newList.contains(c))
                        newList.indexOf(c)
                    else
                        99999// if total card count ever exceeds this amount, uh, we have bigger problems
                }
                state.copy(
                    turnList = newList,
                    availableCards = allCards
                )
            } else {
                // not in picker mode, just move/toggle selection
                val selected = if (state.selected == combatant) {
                    null
                } else {
                    combatant
                }
                state.copy(selected = selected)

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

            val idx = (newList.indexOf(combatant) + direction).coerceIn(0, newList.size - 1)

            newList.remove(combatant)
            newList.add(idx, combatant)

            state.copy(
                turnList = newList
            )
        }
    }

    fun startPickerMode() {
        stateFlow.update { state ->
            state.copy(
                availableCards = state.availableCards.sortedBy { c ->
                    if (state.turnList.contains(c))
                        state.turnList.indexOf(c)
                    else
                        99
                },
                pickerMode = true
            )
        }
    }

    fun endPickerMode() {
        stateFlow.update { state ->
            state.copy(
                pickerMode = false,
                selected = if (state.turnList.contains(state.selected)) state.selected else null
                // clear selected state if it's orphaned
            )
        }
    }
}