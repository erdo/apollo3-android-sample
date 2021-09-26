package foo.bar.foreapollo3

import android.app.Application
import co.early.fore.kt.core.delegate.DebugDelegateDefault
import co.early.fore.kt.core.delegate.ForeDelegateHolder
import co.early.fore.kt.core.logging.AndroidLogger
import co.early.fore.kt.core.logging.SilentLogger
import co.early.fore.kt.net.InterceptorLogging
import co.early.fore.kt.net.apollo3.CallProcessorApollo3
import foo.bar.foreapollo3.api.CustomApolloBuilder
import foo.bar.foreapollo3.api.CustomGlobalErrorHandler
import foo.bar.foreapollo3.api.CustomGlobalRequestInterceptor
import foo.bar.foreapollo3.feature.launch.LaunchService
import foo.bar.foreapollo3.feature.launch.LaunchesModel
import java.util.*


/**
 *
 * OG - Object Graph, pure DI implementation
 *
 * Copyright © 2019 early.co. All rights reserved.
 */
@Suppress("UNUSED_PARAMETER")
object OG {

    private var initialized = false
    private val dependencies = HashMap<Class<*>, Any>()

    @OptIn(ExperimentalStdlibApi::class)
    fun setApplication(application: Application) {

        // create dependency graph
        if (BuildConfig.DEBUG) { ForeDelegateHolder.setDelegate(DebugDelegateDefault("fore_")) }
        val logger = ForeDelegateHolder.getLogger()

        // networking classes common to all models
        val globalRequestInterceptor = CustomGlobalRequestInterceptor()
        val apolloClient = CustomApolloBuilder.create(
            globalRequestInterceptor,
            InterceptorLogging(logger)
        )//logging interceptor should be the last one

        val callProcessor = CallProcessorApollo3(
            globalErrorHandler = CustomGlobalErrorHandler(logger),
            logger = logger
        )

        // models
        val launchesModel = LaunchesModel(
            launchService = LaunchService(
                getLaunches = { apolloClient.query(LaunchesQuery()) }
            ),
            callProcessor,
            logger
        )

        // add models to the dependencies map if you will need them later
        dependencies[LaunchesModel::class.java] = launchesModel
    }


    fun init() {
        if (!initialized) {
            initialized = true

            // run any necessary initialization code once object graph has been created here

        }
    }


    /**
     * This is how dependencies get injected, typically an Activity/Fragment/View will call this
     * during the onCreate()/onCreateView()/onFinishInflate() method respectively for each of the
     * dependencies it needs.
     *
     * Can use a DI library for similar behaviour using annotations
     *
     * Will return mocks if they have been set previously in putMock()
     *
     *
     * Call it like this:
     *
     * <code>
     *     yourModel = OG[YourModel::class.java]
     * </code>
     *
     * If you want to more tightly scoped object, one way is to pass a factory class here and create
     * an instance where you need it
     *
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(model: Class<T>): T = dependencies[model] as T

    fun <T> putMock(clazz: Class<T>, instance: T) {
        dependencies[clazz] = instance as Any
    }
}
