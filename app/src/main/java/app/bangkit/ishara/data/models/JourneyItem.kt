package app.bangkit.ishara.data.models

import app.bangkit.ishara.data.responses.journey.DataItem
import app.bangkit.ishara.data.responses.journey.LevelsItem

sealed class Item {
    data class StageItem(val stageData: DataItem) : Item() // Represents a stage with its data
    data class LevelItem(val levelData: LevelsItem) : Item() // Represents a level with its data
}


