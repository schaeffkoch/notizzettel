package com.example.notizzettel

import android.app.ActionBar
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.widget.ArrayAdapter as ArrayAdapter1
import android.widget.ListAdapter as ListAdapter1
import kotlin.collections.MutableList as MutableList1



class MainActivity : AppCompatActivity() {
    val notiz_list = mutableListOf("main")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { //view ->
            //Snackbar.make(view, "new note", Snackbar.LENGTH_LONG)
              //      .setAction("Action", null).show()
            this.inputDialog()
        }
        val file_name ="data.txt"
        val path =this.filesDir
        var datafile= File(path, file_name)
        if (datafile.exists()) {
            Toast.makeText(this, "load notizlist", Toast.LENGTH_LONG).show()
            load_data(file_name)
        } else {

            init_data()
        }
        init_notesview()
    }

    override fun onPause() {

        this.save_notizllist("data.txt")
        Toast.makeText(this, "save  notizllist", Toast.LENGTH_LONG).show()
        super.onPause()
    }

    fun inputDialog() {
        val mdialog= Dialog(this)
        val minput = EditText(this)
        val dlayout = LinearLayout(this)
        val ok_btn= Button(this)
        val cancle_btn=Button(this)
        ok_btn.setText("ok")
        cancle_btn.setText("cancle")
        dlayout.orientation=LinearLayout.VERTICAL
        mdialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, MATCH_PARENT)
        dlayout.addView(minput)
        dlayout.addView(ok_btn)
        dlayout.addView(cancle_btn)
        mdialog.setContentView(dlayout)
        ok_btn.setOnClickListener({
            val ntext=minput.text
            this.notiz_list.add(ntext.toString())
            mdialog.dismiss()
        })
        cancle_btn.setOnClickListener({
            mdialog.dismiss()
        })
        mdialog.show()
    }

    fun init_data() {
        var n : Integer
        this.notiz_list.clear()
        for (n in 0..50) {
            this.notiz_list.add(n.toString()+"note")
        }
    }
    fun load_data(file_name:String) {
        val path =this.filesDir
        val text= File(path, file_name).readText()
        val data = text.split("\n")
        this.notiz_list.clear()
        for (d in data) {
            if (!d.trim().isEmpty()) {
                this.notiz_list.add(d)
            }
        }

    }

    fun save_notizllist(file_name : String ) {
        val path = this.filesDir
        val t_file = File(path, file_name)
        //val new_file = t_file.createNewFile()
        if (t_file.canWrite()) {
            var st :String =""
            for (d in this.notiz_list) {
              st+=d+"\n"

            }
            t_file.writeText(st)
            Toast.makeText(this, path.toString(), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "save failed", Toast.LENGTH_LONG).show()

        }
    }

    fun init_notesview() {
        var listView : ListView = findViewById<ListView>(R.id.notiz_list)

        val l = notiz_list as ArrayList<String>
        val madapter = ArrayAdapter1(this, android.R.layout.simple_list_item_1, l)
        listView.adapter=madapter
        listView.onItemClickListener = AdapterView.OnItemClickListener{ parent, view,
                                                                            position, id ->
                val del_dia = AlertDialog.Builder(this)
                del_dia.setMessage("Delete?")
                del_dia.setPositiveButton("ok") {dialog, which -> run {
                    Toast.makeText(this, "delete", Toast.LENGTH_LONG).show()
                    this.notiz_list.removeAt(position)
                    madapter.notifyDataSetChanged()

                    }
                }
                del_dia.setNegativeButton("cancle") {dialog, which ->
                    Toast.makeText(this, "cancle", Toast.LENGTH_LONG).show()
                }
                del_dia.show()


        }
        //notes_view.text=s

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.save_notiz -> this.save_notizllist("data.txt")
            R.id.init_notiz -> this.init_data()
            R.id.clear_zettel -> this.notiz_list.clear()

        }
        this.init_notesview()
        return when (item.itemId) {
            R.id.action_settings -> true

            else -> super.onOptionsItemSelected(item)
        }
    }
}