package foo.bar.foreapollo3.ui.launch

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import co.early.fore.core.ui.SyncableView
import co.early.fore.kt.core.ui.ForeLifecycleObserver
import co.early.fore.kt.core.ui.showOrGone
import co.early.fore.kt.core.ui.showOrInvisible
import co.early.fore.kt.core.ui.trigger.TriggerOnChange
import co.early.fore.kt.core.ui.trigger.TriggerWhen
import coil.load
import foo.bar.foreapollo3.OG
import foo.bar.foreapollo3.R
import foo.bar.foreapollo3.feature.launch.Launch
import foo.bar.foreapollo3.feature.launch.LaunchesModel
import foo.bar.foreapollo3.feature.launch.NO_LAUNCH
import kotlinx.android.synthetic.main.activity_launches.*

class LaunchActivity : FragmentActivity(R.layout.activity_launches), SyncableView {

    //models that we need to sync with
    private val launchesModel: LaunchesModel = OG[LaunchesModel::class.java]

    private lateinit var showSuccessTrigger: TriggerOnChange<Launch>
    private lateinit var showErrorTrigger: TriggerWhen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupButtonClickListeners()
        setupTriggers()

        lifecycle.addObserver(ForeLifecycleObserver(this, launchesModel))
    }

    private fun setupButtonClickListeners() {
        launch_fetch_btn.setOnClickListener { launchesModel.fetchLaunches() }
    }

    private fun setupTriggers() {

        showErrorTrigger = TriggerWhen({ launchesModel.currentState.errorMsg != null }) {
            showToast(launchesModel.currentState.errorMsg)
        }

        showSuccessTrigger = TriggerOnChange({ launchesModel.currentState.launch }) {
            if (it.now != NO_LAUNCH) {
                showToast("Success!")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun syncView() {

        launchesModel.currentState.apply {
            launch_fetch_btn.isEnabled = !isBusy
            launch_id_textview.text = "id:${launch.id}"
            launch_patch_img.load(launch.patchImgUrl)
            launch_busy_progbar.showOrInvisible(isBusy)
            launch_smallbusy_progbar.showOrInvisible(isBusy)
            launch_detailcontainer_linearlayout.showOrGone(!isBusy)
        }

        // this lets us fire one off events from changes of state, you can use your
        // preferred method, see here for more details:
        // https://erdo.github.io/android-fore/01-views.html#triggers
        showErrorTrigger.checkLazy()
        showSuccessTrigger.checkLazy()
    }
}
