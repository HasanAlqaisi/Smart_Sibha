package com.hraa.sibha2

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import android.R.attr.button
import android.R.attr.gravity
import android.view.ViewGroup
import android.widget.LinearLayout



var sp_count = 9 //sharedpreferences default size (change it if you add a tasbiha in sp)

var j: Int = 0 //main counter for tasbih

class MainActivity : AppCompatActivity(), TextWatcher {

    var indexOfTas : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getLastData() // to get the last data had choosen by user and show it (from onDestroy func)
        tasbihaText_whenEnterclicked() //when someone write in edit text and clicked enter.. this func do its job
        add_tasbiha_text.addTextChangedListener(this) // to lock alright icon if edit text was empty
    }


    fun clickButtons(view: View) {
        when (view.id) {
            R.id.tasbihaatBTN -> {
                startActivityForResult(Intent(this, tasbihaat_list::class.java), 1)
            } // this let user to go and choose a tasbihaa and get back to this activity

            R.id.counterBTN -> {
                counterBTN.text = "${++j}" // when counterBTN clicked.. increase the count
                if (choosen_tasbiha_txt.text.toString() == "") {
                    val snackbar = Snackbar.make(mainLayout, "يرجى اختيار تسبيحة للبدء في العد", Snackbar.LENGTH_LONG)
                    ViewCompat.setLayoutDirection(snackbar.view, ViewCompat.LAYOUT_DIRECTION_RTL)
                    snackbar.show()
                    j = 0
                    counterBTN.text = "$j"
                } // if choosen text is empty.. then show a snackbar to remaind the user to choose before count

                getSharedPreferences("SavedValues", Context.MODE_PRIVATE).edit()
                    .putInt("countForTasbihaa$indexOfTas", j).apply()

            } // this button is to count if the user choose a tasbihaa

            R.id.add_tasbiha_icon -> {
                tasbihaatBTN.visibility = View.GONE
                add_tasbiha_icon.visibility = View.GONE
                add_tasbiha_text.visibility = View.VISIBLE
                alright_icon.visibility = View.VISIBLE
            } // no need to explain

            R.id.alright_icon -> {
                tasbihaatBTN.visibility = View.VISIBLE
                add_tasbiha_icon.visibility = View.VISIBLE
                add_tasbiha_text.visibility = View.GONE
                alright_icon.visibility = View.GONE
                j=0
                counterBTN.text = "$j"
                tasbihaat_ADDBYUSER()
            } // no need to explain

            R.id.close_icon -> {
                val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)
                val dis = builder.create()
                builder.setTitle("الغاء التسبيحة")
                builder.setMessage("هل تريد الغاء التسبيحة الحالية؟")
                builder.setPositiveButton("نعم") { dialogInterface: DialogInterface, i: Int ->
                    choosen_tasbiha_txt.text = ""
                    j = 0
                    counterBTN.text = "0"
                    getSharedPreferences("SavedValues",Context.MODE_PRIVATE).edit().remove("countForTasbihaa$indexOfTas").apply()
                    dis.dismiss()
                }
                builder.setNegativeButton("لا") { dialogInterface: DialogInterface, i: Int ->
                    dis.dismiss()
                }
                builder.show()
            }

            R.id.replay_icon -> {
                val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)
                val dis = builder.create()
                builder.setTitle("تصفير العداد")
                builder.setMessage("هل تريد تصفير العداد؟")
                builder.setPositiveButton("نعم") { dialogInterface: DialogInterface, i: Int ->
                    j = 0
                    counterBTN.text = "0"
                    getSharedPreferences("SavedValues",Context.MODE_PRIVATE).edit().remove("countForTasbihaa$indexOfTas").apply()
                    dis.dismiss()
                }
                builder.setNegativeButton("لا") { dialogInterface: DialogInterface, i: Int ->
                    dis.dismiss()
                }
                builder.show()
            }
        }
    }

    private fun tasbihaText_whenEnterclicked() {
        add_tasbiha_text.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                tasbihaatBTN.visibility = View.VISIBLE
                add_tasbiha_icon.visibility = View.VISIBLE
                add_tasbiha_text.visibility = View.GONE
                alright_icon.visibility = View.GONE
                tasbihaat_ADDBYUSER()
                return@OnKeyListener true
            }
            false
        }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
             indexOfTas = data?.extras?.getInt("indexOfTasbihaa")
            j = getSharedPreferences("SavedValues", Context.MODE_PRIVATE).getInt("countForTasbihaa$indexOfTas",0)
            counterBTN.text = "$j"
            choosen_tasbiha_txt.text = data?.extras?.getString("value1")

        }
    } // Take tasbihaa from tasbihaat_list activity

    fun tasbihaat_ADDBYUSER() {
        var t: String? = null
        sp_count = getSharedPreferences("tasbihaat", Context.MODE_PRIVATE).all.size
        t = add_tasbiha_text.text.toString()
        choosen_tasbiha_txt.text = t
        val sp = getSharedPreferences("tasbihaat", Context.MODE_PRIVATE)
        sp.edit().putString("tasbihaa$sp_count", t).apply()
        sp_count++
    }

    override fun afterTextChanged(p0: Editable?) {
        alright_icon.isEnabled = add_tasbiha_text.text.trim().toString().isNotEmpty()
    } // lock alright icon if edit text was empty!

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    private var back_pressed_time: Long = 0
    private val PERIOD: Long = 2000
    override fun onBackPressed() {
        if (alright_icon.visibility == View.VISIBLE) {
            tasbihaatBTN.visibility = View.VISIBLE
            add_tasbiha_icon.visibility = View.VISIBLE
            add_tasbiha_text.visibility = View.INVISIBLE
            alright_icon.visibility = View.INVISIBLE
        } else if (back_pressed_time + PERIOD > System.currentTimeMillis())
            super.onBackPressed()
        else
            Toast.makeText(this, "أضغط مرة اخرى للخروج من التطبيق", Toast.LENGTH_SHORT).show()
        back_pressed_time = System.currentTimeMillis()
    }

    private fun getLastData() {
        choosen_tasbiha_txt.text = getSharedPreferences("SavedValues", Context.MODE_PRIVATE).getString("lastText", "")
        j = getSharedPreferences("SavedValues", Context.MODE_PRIVATE).getInt("countIs", j)
        counterBTN.text = "$j"
    }

    override fun onStop() {
        getSharedPreferences("SavedValues", Context.MODE_PRIVATE).edit()
            .putInt("countIs", j).apply()
        getSharedPreferences("SavedValues", Context.MODE_PRIVATE).edit()
            .putString("lastText", choosen_tasbiha_txt.text.toString()).apply()
        super.onStop()
    }
}
