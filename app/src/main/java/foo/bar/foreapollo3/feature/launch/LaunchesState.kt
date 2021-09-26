package foo.bar.foreapollo3.feature.launch

import foo.bar.foreapollo3.LaunchesQuery
import foo.bar.foreapollo3.message.ErrorMessage
import java.util.*

const val NO_ID = "(no launch)"

val NO_LAUNCH = Launch(NO_ID, "no site", false, "")

data class LaunchesState(
    val isBusy: Boolean,
    val launch: Launch,
    val errorMsg: ErrorMessage?,
)

data class Launch(
    val id: String,
    val site: String,
    val isBooked: Boolean = false,
    val patchImgUrl: String = ""
)

//we don't want the API / graphQL pojo abstractions leaking in to the rest of the app
//so we convert them here to app level items, nothing bellow the feature level
//knows anything about the API
fun LaunchesQuery.Launch.toApp(): Launch {
    return Launch(this.id, this.site ?: "unknown", this.isBooked, this.mission?.missionPatch ?: "")
}

fun LaunchesQuery.Launches.selectRandomLaunch(): Launch {
    val listOfLaunches = launches.filterNotNull()
    return when {
        listOfLaunches.isEmpty() -> NO_LAUNCH
        listOfLaunches.size == 1 -> listOfLaunches[0].toApp()
        else -> listOfLaunches[Random().nextInt(listOfLaunches.size - 1)].toApp()
    }
}