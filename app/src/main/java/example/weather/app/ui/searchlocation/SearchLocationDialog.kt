package example.weather.app.ui.searchlocation

import android.content.Context
import android.location.Address
import android.text.Editable
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import example.weather.app.databinding.DialogSearchLocationBinding
import example.weather.app.ui.main.MainViewModel
import example.weather.app.utils.getWindowHeight

class SearchLocationDialog(
    context: Context,
    viewModel: MainViewModel,
    onSelected : (location : String?) -> Unit
) : BottomSheetDialog(context) {
    private var binding : DialogSearchLocationBinding? = null
    private var adapter: SearchLocationAdapter = SearchLocationAdapter {
        onSelected(it)
        dismiss()
    }

    init {
        binding = DialogSearchLocationBinding.inflate(layoutInflater)
        binding!!.searchLocationRv.adapter = adapter
        binding!!.searchLocationEt.addTextChangedListener{ text: Editable? ->
            if (text.isNullOrEmpty()) update(listOf())
            else {
                binding!!.progress.root.isVisible = true
                viewModel.searchLocation(text.toString(), context)
            }
        }
        binding!!.useMyGeoDataTv.setOnClickListener {
            onSelected(null)
            dismiss()
        }
        this.setContentView(binding!!.root)
        binding!!.root.post {
            val lp = binding!!.root.layoutParams
            lp.height = getWindowHeight(window!!, context)
            binding!!.root.layoutParams = lp
        }
        show()
    }

    fun update(newAddresses : List<Address>) {
        if (binding == null) return
        adapter.update(newAddresses)
        binding!!.progress.root.isVisible = false
    }
}