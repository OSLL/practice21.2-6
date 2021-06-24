package com.makentoshe.androidgithubcitemplate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        val school_sub = findViewById<RecyclerView>(R.id.school_subjects)
        school_sub.layoutManager = LinearLayoutManager(this)
        school_sub.adapter = SchoolSubRecyclerAdapter(1, this)

        val add_button = findViewById<Button>(R.id.main_add_button)
        add_button.setOnClickListener {
            makeDialogWindow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.noti_but -> {
                val intent = Intent(this, NotificationsActivity::class.java)
                startActivity(intent)
            }
            R.id.info_but -> {
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun makeDialogWindow () {
        val li: LayoutInflater = LayoutInflater.from(this)
        val promptsView: View = li.inflate(R.layout.prompt_main, null)

        //Создаем AlertDialog
        val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView)

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        val text_input = promptsView.findViewById<EditText>(R.id.main_pro_text_input)

        mDialogBuilder
            .setCancelable(false)
            .setPositiveButton("Сохранить") { dialog, id ->
                if (text_input != null) {
                    //add_button.setText(text_input.text.toString())                                 //TODO
                }
            }
            .setNegativeButton("Отмена") { dialog, id -> dialog.cancel()}
        val alertDialog: AlertDialog = mDialogBuilder.create()
        alertDialog.show()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support librar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "chanel"
            val descriptionText = "noti"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

class SchoolSubRecyclerAdapter(val strings: Int, val ctx: MainActivity): RecyclerView.Adapter<SchoolSubViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolSubViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
        return SchoolSubViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SchoolSubViewHolder, position: Int) {
        holder.menu_but?.setOnClickListener {
            makeDialogWindow()
        }

        holder.name?.setOnClickListener {
            val intent = Intent(ctx, SubjectActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    override fun getItemCount() = strings

    private fun makeDialogWindow () {
        val li: LayoutInflater = LayoutInflater.from(ctx)
        val promptsView: View = li.inflate(R.layout.prompt_main, null)

        //Создаем AlertDialog
        val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(ctx)

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView)

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        val text_input = promptsView.findViewById<EditText>(R.id.main_pro_text_input)

        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Сохранить") { dialog, id ->
                    if (text_input != null) {
                        //add_button.setText(text_input.text.toString())                             //TODO
                    }
                }
                .setNegativeButton("Отмена") { dialog, id -> dialog.cancel()}
        val alertDialog: AlertDialog = mDialogBuilder.create()
        alertDialog.show()
    }
}

class SchoolSubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView? = null
    var menu_but: TextView? = null

    init {
        name = itemView.findViewById(R.id.textViewMenuItem)
        menu_but = itemView.findViewById(R.id.main_add_button)
    }
}
