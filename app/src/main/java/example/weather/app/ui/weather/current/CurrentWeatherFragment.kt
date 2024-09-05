package example.weather.app.ui.weather.current

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import example.weather.app.ui.searchlocation.SearchLocationDialog

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {
    private var _binding : FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentWeatherBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.locationTv.setOnClickListener {
            (activity as MainActivity).callSearchLocationDialog()
        }
        initObservers()
    }

    @SuppressLint("SetTextI18n")
    private fun initObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { binding.progress.root.isVisible = it }
        viewModel.weatherLocationData.observe(viewLifecycleOwner) {
            binding.locationTv.text = "${it.name}, ${it.region}, ${it.country}"
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