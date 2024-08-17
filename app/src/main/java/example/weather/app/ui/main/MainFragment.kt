package example.weather.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import example.weather.app.databinding.FragmentMainBinding
import example.weather.app.ui.weather.current.CurrentWeatherFragment

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!
    val viewModel : MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.requestCurrentWeather()
        childFragmentManager
            .beginTransaction()
            .add(binding.mainFragmnetContainer.id, CurrentWeatherFragment())
            .commit()
    }
}