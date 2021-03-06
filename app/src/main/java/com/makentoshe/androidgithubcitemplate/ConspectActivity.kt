package com.makentoshe.androidgithubcitemplate

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder

class Conspect(var text : String, var url : String)
{
    var color = Color.parseColor("#FFFFFF")
}

class ConspectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conspect)
        if (intent.hasExtra(IntentTags.TITLE_SUB_TO_CONSPECTS))
            title = intent.getStringExtra(IntentTags.TITLE_SUB_TO_CONSPECTS) + " Conspects"
        else {
            title = "Conspects"
        }

        val conspects = (0 until 1).map { Conspect("Зачет по физике", "https://docs.google.com/document/d/1YQRIhs7epxpXpq1V2wCrgdoDWa3eGNpKahO8qL-446M/edit") } as MutableList

        val recycleView = findViewById<RecyclerView>(R.id.recycleviewConspects)
        recycleView.adapter = RecyclerViewAdapterConspects(this, conspects)

        val builderSettings : AlertDialog.Builder = AlertDialog.Builder(this)
        // Settings builder
        val layoutInflater : LayoutInflater = LayoutInflater.from(this)
        val view : View = layoutInflater.inflate(R.layout.conspects_adding, null)
        val colorButton = view.findViewById<Button>(R.id.buttonConspectsAddingColor)
        colorButton.setBackgroundColor(Color.parseColor("#FFFFFF"))
        val newText = view.findViewById<EditText>(R.id.con_pro_text1_input)
        newText.setText("")
        builderSettings.setView(view)
        builderSettings.setCancelable(true);
        builderSettings.setPositiveButton("Add",
            DialogInterface.OnClickListener { dialog, id ->
                val newUrl = view.findViewById<EditText>(R.id.con_pro_url_inpit)
                val conspect = Conspect(newText?.text.toString(), newUrl?.text.toString())
                conspects.add(conspect)
                val background = colorButton.background as ColorDrawable
                conspect.color = background.color
                recycleView.adapter?.notifyDataSetChanged()
                dialog.dismiss()
            })
        builderSettings.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id -> dialog.dismiss()
            })

        val dialogSettings : AlertDialog = builderSettings.create()

        // Color picker
        val builderColorPicker : ColorPickerDialogBuilder = ColorPickerDialogBuilder.with(this)
        builderColorPicker.setTitle("Choose color")

        builderColorPicker.setPositiveButton("Select",
            ColorPickerClickListener { dialog, color, allColors ->
                colorButton.setBackgroundColor(color)
                dialog.dismiss()
            })

        val dialogColor : AlertDialog = builderColorPicker.build()
        colorButton.setOnClickListener { dialogColor.show() }

        // Adding button
        val addingButton = findViewById<Button>(R.id.buttonConspects)
        addingButton.setOnClickListener {
            dialogSettings.show()
        }
    }
}


class RecyclerViewAdapterConspects(val activity : ConspectActivity, val conspects : MutableList<Conspect>): RecyclerView.Adapter<ViewHolderConspects> ()
{
    private val builderSettings : AlertDialog.Builder = AlertDialog.Builder(activity)
    private val builderDeleteConfirm : AlertDialog.Builder = AlertDialog.Builder(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderConspects {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.conspects_item, parent, false)
        return ViewHolderConspects(view)
    }

    override fun onBindViewHolder(holder: ViewHolderConspects, position: Int) {
        holder.textView.text = conspects[position].text
        holder.cardView.setCardBackgroundColor(conspects[position].color)

        holder.textView.setTextColor(Color.parseColor("#000000"))

        /* Dialogs */
        val layoutInflater : LayoutInflater = LayoutInflater.from(activity)
        val view : View = layoutInflater.inflate(R.layout.conspects_settings, null)
        val colorButton = view.findViewById<Button>(R.id.buttonConspectsSettingsColor)
        colorButton.setBackgroundColor(conspects[position].color)

        builderDeleteConfirm.setMessage("Delete conspect?")
        builderDeleteConfirm.setCancelable(true)
        builderDeleteConfirm.setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id ->
                    conspects.removeAt(position)
                    dialog.cancel()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, conspects.size)
                    Toast.makeText(activity, "Conspect deleted", Toast.LENGTH_SHORT).show()

                })
        builderDeleteConfirm.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id -> dialog.dismiss()})
        val dialogDeleteConfirm : AlertDialog = builderDeleteConfirm.create()

        // Settings builder
        builderSettings.setView(view)
        builderSettings.setCancelable(true);
        builderSettings.setPositiveButton("Apply",
            DialogInterface.OnClickListener { dialog, id ->
                val newText = view.findViewById<EditText>(R.id.con_ed_text1_input)
                val newUrl = view.findViewById<EditText>(R.id.con_ed_url_inpit)
                conspects[position].text = newText?.text.toString()
                conspects[position].url = newUrl?.text.toString()
                val background = colorButton.background as ColorDrawable
                conspects[position].color = background.color
                notifyItemChanged(position)
                dialog.dismiss()
            })
        builderSettings.setNeutralButton("Cancel",
            DialogInterface.OnClickListener { dialog, id -> dialog.dismiss()})
        builderSettings.setNegativeButton("Delete", { dialog, id -> dialogDeleteConfirm.show()})

        val dialogSettings : AlertDialog = builderSettings.create()
        holder.imageButton.setOnClickListener {
            dialogSettings.show()

            val editText = dialogSettings.findViewById<EditText>(R.id.con_ed_text1_input)
            editText?.setText(conspects[position].text)
            val editUrl = dialogSettings.findViewById<EditText>(R.id.con_ed_url_inpit)
            editUrl?.setText(conspects[position].url)
        }

        // Color picker
        val builderColorPicker : ColorPickerDialogBuilder = ColorPickerDialogBuilder.with(activity)
        builderColorPicker.setTitle("Choose color")

        builderColorPicker.setPositiveButton("Select",
            ColorPickerClickListener { dialog, color, allColors ->
                colorButton.setBackgroundColor(color)
                dialog.dismiss()
                notifyItemChanged(position)
            })

        val dialogColor : AlertDialog = builderColorPicker.build()
        colorButton.setOnClickListener{dialogColor.show()}

        holder.cardView.setOnClickListener {
            if (conspects[position].url.isNotEmpty()) {
                val uri: Uri = Uri.parse(conspects[position].url)
                val launch_browser = Intent(Intent.ACTION_VIEW, uri)
                activity.startActivity(launch_browser)
            } else {
                Toast.makeText(activity, "Incorrect URL", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return conspects.size
    }
}

class ViewHolderConspects(itemView: View): RecyclerView.ViewHolder(itemView)
{
    val textView: TextView = itemView.findViewById<TextView>(R.id.textViewConspectsItem)
    val imageButton: ImageButton = itemView.findViewById<ImageButton>(R.id.imageButtonConspectsItem)
    val cardView: CardView = itemView.findViewById<CardView>(R.id.cardViewConspectsItem)
}