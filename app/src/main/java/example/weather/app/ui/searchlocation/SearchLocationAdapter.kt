package example.weather.app.ui.searchlocation

import android.annotation.SuppressLint
import android.location.Address
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import example.weather.app.databinding.ItemSearchedLocationBinding
import example.weather.app.utils.getFullLocationNameFormat

class SearchLocationAdapter(val onClick : (locationName : String) -> Unit)
    : RecyclerView.Adapter<SearchLocationAdapter.SearchLocationViewHolder>() {

    private val addresses = mutableListOf<Address>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchLocationViewHolder(
        ItemSearchedLocationBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = addresses.size

    override fun onBindViewHolder(holder: SearchLocationViewHolder, position: Int) {
        val address = addresses[position]
        holder.binding.root.text = getFullLocationNameFormat(address)
        holder.binding.root.setOnClickListener {
            onClick("${address.latitude},${address.longitude}")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newLocationsList: List<Address>) {
        addresses.clear()
        addresses.addAll(newLocationsList)
        notifyDataSetChanged()
    }

    class SearchLocationViewHolder(
        val binding: ItemSearchedLocationBinding
    ) : RecyclerView.ViewHolder(binding.root)
}