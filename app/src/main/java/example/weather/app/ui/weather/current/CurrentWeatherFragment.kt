package example.weather.app.ui.weather.current

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import example.weather.app.App
import example.weather.app.R
import example.weather.app.databinding.FragmentCurrentWeatherBinding
import example.weather.app.ui.main.MainActivity
import example.weather.app.ui.main.MainViewModel
import example.weather.app.utils.getFullLocationNameFormat
import example.weather.app.utils.getLocation
import example.weather.app.utils.languages
import example.weather.app.utils.preferences.PrefManager
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {
    private var _binding : FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MainViewModel by activityViewModels()
    @Inject
    lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentWeatherBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.locationTv.setOnClickListener {
            (activity as MainActivity).callSearchLocationDialog()
        }
        var lang = prefManager.savedLanguageCode
        if (lang == "") lang = (requireActivity().application as App).systemLanguageCode
        binding.languageTv.text = lang
        binding.languageLl.setOnClickListener { openLanguagesPopupMenu() }
        initObservers()
    }

    private fun openLanguagesPopupMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.languageLl)
        popupMenu.menu.add(getString(R.string.system))
        languages.forEach { locale -> popupMenu.menu.add(locale.getDisplayName(locale)) }
        val currentLanguage = prefManager.savedLanguageCode
        popupMenu
            .menu[if (currentLanguage == "" ) 0 else languages.indexOf(Locale(currentLanguage)) + 1]
            .isEnabled = false
        popupMenu.show()
        val app = requireActivity().application as App
        popupMenu.setOnMenuItemClickListener { menuItem ->
            val systemLocale = Locale(app.systemLanguageCode)
            if (menuItem.title == systemLocale.getDisplayName(systemLocale) ||
                popupMenu.menu.children.firstOrNull() == menuItem) {
                prefManager.savedLanguageCode = ""
            } else {
                prefManager.savedLanguageCode =
                    languages[popupMenu.menu.children.indexOf(menuItem) - 1].toLanguageTag()
            }
            app.setLanguage()
            true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { binding.progress.root.isVisible = it }
        viewModel.weatherLocationData.observe(viewLifecycleOwner) {
            val location = getLocation(prefManager)
            if (location == "") binding.locationTv.text = "${it.name}, ${it.region}, ${it.country}"
            else {
                viewModel.searchLocationName("${it.name}, ${it.region}, ${it.country}",
                    requireContext())
            }
        }
        viewModel.locationAddress.observe(viewLifecycleOwner) {
            binding.locationTv.text = getFullLocationNameFormat(it)
        }
        viewModel.currentWeatherData.observe(viewLifecycleOwner) {
            binding.lastUpdateValueTv.text = it.lastUpdatedDate
            binding.conditionTv.text = it.condition.text
            binding.temperatureTv.text = getString(R.string.celsius, it.tempC)
            Glide
                .with(requireContext())
                .load("https:${it.condition.icon}")
                .fitCenter()
                .placeholder(R.drawable.weather_loader)
                .into(binding.weatherIconImv)
        }
    }
}