package ng.myflex.telehost.ui.account.activate

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_activate.*
import kotlinx.android.synthetic.main.toolbar.view.*
import ng.myflex.telehost.R
import ng.myflex.telehost.common.content.ApplicationFragment
import ng.myflex.telehost.databinding.FragmentActivateBinding
import ng.myflex.telehost.domain.Duration
import ng.myflex.telehost.util.ServiceResponse
import ng.myflex.telehost.util.Tools
import ng.myflex.telehost.util.md5
import javax.inject.Inject

class ActivateFragment : ApplicationFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    lateinit var viewModel: ActivateViewModel

    lateinit var binding: FragmentActivateBinding

    private val validation = AwesomeValidation(ValidationStyle.BASIC)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_activate, container, false
        )
        viewModel = ViewModelProviders.of(this, factory).get(
            ActivateViewModel::class.java
        )
        binding.apply {
            loading = false
            viewModel = this@ActivateFragment.viewModel
            lifecycleOwner = this@ActivateFragment
            validation = this@ActivateFragment.validation
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initToolbar()
        initDuration()
        initSimSlot()

        viewModel.onJob().observe(this, Observer {
            jobs.text = "$it"
        })
        viewModel.onActivate().observe(this, Observer { onActivated(it) })
        device_token.text = viewModel.getToken()
        device_hash.text = viewModel.getToken()?.md5()
        device_token.setOnClickListener {
            activity?.let {
                val clipboard = it.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", device_token.text)
                clipboard.setPrimaryClip(clip)

                Toast.makeText(context, "Token copied", Toast.LENGTH_LONG).show();
            }

        }
        viewModel.watch()
        btn_activate.setOnClickListener {
            var snackbar: Snackbar? = null
            when {
                rg_duration.checkedRadioButtonId == -1 -> {
                    snackbar = Snackbar.make(
                        view!!,
                        R.string.duration_warning, Snackbar.LENGTH_SHORT
                    )
                }
                rg_sim.checkedRadioButtonId == -1 -> {
                    snackbar = Snackbar.make(
                        view!!,
                        R.string.sim_slot_warning, Snackbar.LENGTH_SHORT
                    )
                }
                validation.validate() -> {
                    showLoading(true)
                    val duration = rg_duration.findViewById<AppCompatRadioButton>(
                        rg_duration.checkedRadioButtonId
                    ).tag
                    viewModel.onSubmit(duration as Duration, rg_sim.checkedRadioButtonId)
                }
            }
            snackbar?.show()
        }
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.navigationIcon!!.setColorFilter(
            resources.getColor(R.color.colorTextAction),
            PorterDuff.Mode.SRC_ATOP
        )
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setTitle(R.string.menu_activate)
        Tools.changeOverflowMenuIconColor(
            binding.toolbar,
            resources.getColor(R.color.colorTextAction)
        )
        Tools.setSmartSystemBar(activity)
    }


    private fun onActivated(response: ServiceResponse<Boolean>) {
        showLoading(false)
        when (response) {
            is ServiceResponse.OnSuccess -> {
                if (!response.data) {
                    Snackbar.make(
                        view!!,
                        "Activation was not successful", Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    Snackbar.make(
                        view!!,
                        "Activation successful!!!", Snackbar.LENGTH_SHORT
                    ).show()

                    rg_sim.clearCheck()
                    rg_duration.clearCheck()
                }
            }
            is ServiceResponse.OnError -> {
                Snackbar.make(
                    view!!,
                    response.error.message!!, Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initDuration() {
        rg_duration.removeAllViews()
        refreshRadioGroup(rg_duration.id)

        for ((index, duration) in viewModel.getDuration().withIndex()) {
            val idx = index + 1
            val option = getSortByRadioTemplate(idx, duration.title)
            option?.tag = duration
            rg_duration.addView(option)
        }
    }

    private fun initSimSlot() {
        rg_sim.removeAllViews()
        refreshRadioGroup(rg_sim.id)

        validation.addValidation(activity, rg_sim.id, SimpleCustomValidation {
            rg_sim.isSelected
        }, R.string.CANCEL)
        for (i in 0 until viewModel.getSlotCount()) {
            val idx = i + 1
            rg_sim.addView(getSortByRadioTemplate(idx))
        }
    }

    private fun refreshRadioGroup(groupId: Int) {
        rg_sim.setOnCheckedChangeListener(null)
        rg_sim.clearCheck()
    }

    private fun getSortByRadioTemplate(idx: Int, title: String? = null): AppCompatRadioButton? {
        val rb = AppCompatRadioButton(activity)
        rb.id = idx
        rb.text = title ?: "Sim $idx"
        rb.maxLines = 1
        rb.isSingleLine = true
        rb.ellipsize = TextUtils.TruncateAt.END
        rb.setTextAppearance(activity, R.style.TextAppearance_AppCompat_Body1)
        rb.highlightColor = resources.getColor(R.color.colorTextAction)
        rb.setTextColor(resources.getColor(R.color.grey_60))
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        rb.layoutParams = params
        rb.setPadding(0, 15, 0, 15)
        return rb
    }

    private fun showLoading(show: Boolean) {
        binding.loading = show

        btn_activate.visibility = if (show) View.INVISIBLE else View.VISIBLE
        progress_bar.visibility = if (!show) View.INVISIBLE else View.VISIBLE
    }
}
