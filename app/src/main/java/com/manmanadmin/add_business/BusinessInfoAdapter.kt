package com.manmanadmin.add_business


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.manmanadmin.R

class BusinessInfoAdapter(ctx: Context, @LayoutRes private val layoutResource: Int,
                          private val listOfBusinesses: MutableList<Business>): ArrayAdapter<Business>(ctx,layoutResource, listOfBusinesses) {

    private val businesses = ArrayList(listOfBusinesses)
    private val allBusinesses = ArrayList(listOfBusinesses)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position,convertView,parent)
    }

    override fun getCount(): Int {
        return businesses.size
    }

    override fun getItemId(position: Int): Long {
        return businesses[position].id
    }

    override fun getItem(position: Int): Business? {
        return businesses[position]
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun convertResultToString(resultValue: Any?): CharSequence {
                val businessSelected = (resultValue as Business)

                return context.getString(R.string.filter_result, businessSelected.lat.toString(),businessSelected.long.toString() )
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    val businessesSuggestion: MutableList<Business> = ArrayList()
                    for (business in listOfBusinesses) {

                        if (business.name?.lowercase()?.startsWith(constraint.toString().lowercase()) == true) {
                            businessesSuggestion.add(business)
                        }
                    }
                    filterResults.values = businessesSuggestion
                    filterResults.count = businessesSuggestion.size
                }
                return filterResults
            }



            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                businesses.clear()

                    if (results.count > 0) {
                        for (result in results.values as List<*>) {
                            if (result is Business) {
                                businesses.add(result)
                            }
                        }
                        notifyDataSetChanged()
                    } else if (constraint == null) {
                        businesses.addAll(allBusinesses)
                        notifyDataSetInvalidated()
                    }

            }
        }
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup): View{
        val view = convertView as LinearLayoutCompat? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as LinearLayoutCompat
        val nameTxt = view.findViewById<TextView>(R.id.business_name_txt)
        val coordinateTxt = view.findViewById<TextView>(R.id.coordinates_txt)
        nameTxt.text = businesses[position].name
        coordinateTxt.text = businesses[position].businessPhoneNumber

        return view
    }
}