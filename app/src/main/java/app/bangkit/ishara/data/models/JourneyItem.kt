package app.bangkit.ishara.data.models

import app.bangkit.ishara.data.responses.journey.JourneyLevelItem
import app.bangkit.ishara.data.responses.journey.JourneyItem

sealed class Item {
    data class StageItem(val stageData: JourneyItem) : Item() // Represents a stage with its data
    data class StageLevelItem(val levelData: JourneyLevelItem) : Item() // Represents a level with its data
}


