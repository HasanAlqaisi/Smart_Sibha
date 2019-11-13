package com.hraa.sibha2

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_tasbihaat_list.*

class tasbihaat_list : AppCompatActivity() {

    private val i = Intent()
    private val tasbihaat = ArrayList<tasbiha_class>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasbihaat_list)

        tasbihaat_ADDBYME() // add tasbihat from index 0 to index of last sp (which are added by me)


        for (i in 9 until getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).all.size) {
            tasbihaat.add(
                tasbiha_class(
                    "${getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).getString("tasbihaa$i", "")}",
                    R.drawable.list_draw,
                    R.drawable.remove_icon
                )
            )
        } //add what user added (after last default index)

        ListView_tasbihaat.adapter = tasbiha_listview_adapter(this, tasbihaat)

        ListView_tasbihaat.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            when (position) {
                position -> {
                    getData(position)
                }
            }
        }

    }


    private fun getData(po: Int) {
        i.putExtra("value1", tasbihaat[po].tasbihaatList)
//        i.putExtra("value2", j)
        i.putExtra("indexOfTasbihaa",po)
        setResult(Activity.RESULT_OK, i)
        finish()
    } //send data to activity_main

    fun tasbihaat_ADDBYME() {
        getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).edit()
            .putString("tasbihaa0", "استغفر الله").apply()
        getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).edit()
            .putString("tasbihaa1", "الحمدلله").apply()
        getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).edit()
            .putString("tasbihaa2", "لا إله إلا الله").apply()
        getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).edit()
            .putString("tasbihaa3", "الله اكبر").apply()
        getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).edit()
            .putString("tasbihaa4", "سبحان الله").apply()
        getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).edit()
            .putString("tasbihaa5", "سبحان الله وبحمده سبحان الله العظيم").apply()
        getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).edit()
            .putString("tasbihaa6", "لا حول ولا قوة إلا بالله").apply()
        getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).edit()
            .putString("tasbihaa7", "اللهم صل على محمد وعلى آل محمد").apply()
        getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).edit()
            .putString("tasbihaa8", "الحمدلله حتى يبلغ الحمد منتهاه").apply()
        for (i in 0..8) {
            tasbihaat.add(
                tasbiha_class(
                    "${getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).getString(
                        "tasbihaa$i",
                        ""
                    )}", R.drawable.list_draw, R.drawable.list_draw
                )
            )
        }

    }

    override fun onDestroy() {
        tasbihaat.clear()
        super.onDestroy()
    }

}

