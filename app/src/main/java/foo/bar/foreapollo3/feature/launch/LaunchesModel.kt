package foo.bar.foreapollo3.feature.launch

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.Either.Left
import co.early.fore.kt.core.Either.Right
import co.early.fore.kt.core.coroutine.awaitMain
import co.early.fore.kt.core.coroutine.launchIO
import co.early.fore.kt.core.logging.Logger
import co.early.fore.kt.core.observer.ObservableImp
import co.early.fore.kt.net.apollo3.CallProcessorApollo3
import com.apollographql.apollo3.api.ApolloResponse
import foo.bar.foreapollo3.LaunchesQuery
import foo.bar.foreapollo3.message.ErrorMessage

data class LaunchService(
    val getLaunches: suspend () -> ApolloResponse<LaunchesQuery.Data>
)

@OptIn(ExperimentalStdlibApi::class)
class LaunchesModel constructor(
    private val launchService: LaunchService,
    private val callProcessor: CallProcessorApollo3<ErrorMessage>,
    private val logger: Logger
) : Observable by ObservableImp(logger = logger) {

    var currentState = LaunchesState(isBusy = false, launch = NO_LAUNCH, errorMsg = null)
        private set

    /**
     * fetch the list of launches using a GraphQl Query, select one at random for the UI
     */
    fun fetchLaunches() {

        logger.i("fetchLaunches()")

        if (currentState.isBusy) {
            currentState = currentState.copy(errorMsg = ErrorMessage.ERROR_BUSY)
            notifyObservers()
            return
        }

        currentState = currentState.copy(isBusy = true, errorMsg = null)
        notifyObservers()

        launchIO {

            val deferredResult = callProcessor.processCallAsync {
                launchService.getLaunches()
            }

            awaitMain {
                when (val result = deferredResult.await()) {
                    is Right -> handleSuccess(result.b.data.launches.selectRandomLaunch())
                    is Left -> handleFailure(result.a)
                }
            }
        }
    }

    private fun handleSuccess(launch: Launch) {

        logger.i("handleSuccess() t:" + Thread.currentThread().id)

        currentState = LaunchesState(false, launch, null)
        notifyObservers()
    }

    private fun handleFailure(failureMessage: ErrorMessage) {

        logger.i("handleFailure() t:" + Thread.currentThread().id)

        currentState = currentState.copy(isBusy = false, errorMsg = failureMessage)
        notifyObservers()
    }
}
