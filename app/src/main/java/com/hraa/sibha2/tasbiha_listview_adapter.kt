package com.hraa.sibha2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.tasbihaat_list.view.*

class tasbiha_listview_adapter(val context: Context, var tasbihaat: ArrayList<tasbiha_class>) : BaseAdapter() {


    @SuppressLint("ViewHolder")

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        val listItemView = LayoutInflater.from(context).inflate(R.layout.tasbihaat_list, p2, false)
        listItemView.textView.text = tasbihaat[p0].tasbihaatList
        listItemView.imageView1.setImageResource(tasbihaat[p0].image1)
        listItemView.imageView2.setImageResource(tasbihaat[p0].image2)


        listItemView.imageView2.setOnClickListener {
            if (listItemView.imageView2.drawable.constantState == context.getDrawable(R.drawable.remove_icon)?.constantState)
                ifRemoveIconClicked(p0)
        }
        return listItemView
    }


    override fun getItem(p0: Int): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return tasbihaat.size
    }

    private fun ifRemoveIconClicked(p0: Int) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogCustom)
        val dis = builder.create()
        builder.setTitle("حذف التسبيحة")
        builder.setMessage("هل تريد حذف هذه التسبيحة؟")
        builder.setPositiveButton("نعم") { dialogInterface: DialogInterface, i: Int ->

            tasbihaat.removeAt(p0)
            context.getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).edit().remove("tasbihaa$p0").apply()
            context.getSharedPreferences("SavedValues", Context.MODE_PRIVATE).edit().remove("countForTasbihaa$p0")
                .apply()
            context.getSharedPreferences("SavedValues", Context.MODE_PRIVATE).edit().remove("lastText").apply()
            context.getSharedPreferences("SavedValues", Context.MODE_PRIVATE).edit().remove("countIs").apply()

            var sol = p0 + 1
            var c = 0
            val value = ArrayList<String?>()
            while (sol <= context.getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).all.size) {
                value.add(context.getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).getString("tasbihaa$sol", ""))
                context.getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).edit().remove("tasbihaa$sol").apply()
                context.getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).edit()
                    .putString("tasbihaa${sol - 1}", value[c]).apply()
                sol++
                c++
            } // add the value of strings after deleted string to value arraylist, then delete all these strings and replace it with a new strings and lower index by 1
            notifyDataSetChanged()
            dis.dismiss()
        }
        builder.setNegativeButton("لا") { dialogInterface: DialogInterface, i: Int ->
            dis.dismiss()
        }
        builder.show()
    }
}

