package com.example.mynotes

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.mynotes.models.TaskList
import com.example.mynotes.ui.detail.ListDetailFragment
import com.example.mynotes.ui.main.MainFragment
import com.example.mynotes.ui.main.MainViewModel
import com.example.mynotes.ui.main.MainViewModelFactory
import com.example.mynotes.databinding.MainActivityBinding
import android.util.Log

class MainActivity : AppCompatActivity(), MainFragment.MainFragmentInteractionListener {
    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel
    lateinit var listDetailEdittext: EditText
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this))
        )
            .get(MainViewModel::class.java)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance(this)
            val fragmentContainerViewId: Int = if (binding.mainFragmentContainer == null) {
                R.id.container }
            else {
                R.id.main_fragment_container
            }

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(fragmentContainerViewId, mainFragment)
            }
        }

        binding.taskListAddButton.setOnClickListener {
            showCreateListDialog()
        }
    }

    private fun showCreateListDialog() {
        val dialogTitle = "Create your note"
        val positiveButtonTitle = "Create"

        val builder = AlertDialog.Builder(this)
        val listTitleEditText = EditText(this)
        listTitleEditText.inputType = InputType.TYPE_CLASS_TEXT

        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)

        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            dialog.dismiss()
            val taskList = TaskList(listTitleEditText.text.toString())
            viewModel.saveList(taskList)
            showListDetail(taskList)
        }

        builder.create().show()
    }


    private fun showListDetail(list: TaskList) {
        if (binding.mainFragmentContainer == null) {
            val listDetailIntent = Intent(this, ListDetailActivity::class.java)
            listDetailIntent.putExtra(INTENT_LIST_KEY, list)
            startActivity(listDetailIntent)
            //startActivityForResult(listDetailIntent, LIST_DETAIL_REQUEST_CODE)
        } else {
            title = list.name
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.list_detail_fragment_container, ListDetailFragment.newInstance())
            }
        }
    }

    companion object {
        const val INTENT_LIST_KEY = "list"
        const val LIST_DETAIL_REQUEST_CODE = 123
        var LIST_NAME = "MyNotes"
    }

    override fun listItemTapped(list: TaskList) {
        LIST_NAME = list.name
        showListDetail(list)
    }

    fun LoadEditText() {
        if (binding.mainFragmentContainer != null){
            sharedPreference = getSharedPreferences("", MODE_PRIVATE)
            listDetailEdittext = findViewById(R.id.list_detail_edittext)
            var loadText = sharedPreference.getString(LIST_NAME,"")
            listDetailEdittext.setText(loadText)
        }
    }


    override fun onBackPressed() {

    if (binding.mainFragmentContainer != null) {
        sharedPreference = getSharedPreferences("", MODE_PRIVATE)
        listDetailEdittext = findViewById(R.id.list_detail_edittext)

        var edited = listDetailEdittext.text.toString()
        sharedPreference.edit().putString(LIST_NAME, edited).apply()

        title = resources.getString(R.string.app_name)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            remove(supportFragmentManager.findFragmentById(R.id.list_detail_fragment_container)!!)
        }
        }
        else{super.onBackPressed()}
        }


    }