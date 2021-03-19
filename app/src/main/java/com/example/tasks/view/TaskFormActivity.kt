package com.example.tasks.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.model.TaskModel
import com.example.tasks.viewmodel.TaskFormViewModel
import kotlinx.android.synthetic.main.activity_register.button_save
import kotlinx.android.synthetic.main.activity_task_form.*
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var mViewModel: TaskFormViewModel
    private val mDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    private val mLisPriorityId: MutableList<Int> = arrayListOf()
    private var mTaskId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        mViewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)

        // Inicializa eventos
        listeners()
        observe()
        loadDataFromActivity()
        mViewModel.listPriorities()
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras
        if (bundle != null) {
            mTaskId = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            mViewModel.loadTask(mTaskId)
            button_save.setText(getString(R.string.update_task))
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.button_save) {
            handleSave()
        }
        if (id == R.id.button_date) {
            showDatePicker()
        }
    }

    private fun handleSave() {
        val task = TaskModel().apply {
            this.description = edit_description.text.toString()
            this.priorityId = mLisPriorityId[spinner_priority.selectedItemPosition]
            this.complete = check_complete.isChecked
            this.dueDate = button_date.text.toString()
            this.id = mTaskId
        }
        mViewModel.saveTask(task)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONDAY)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, this, year, month, day).show()
    }

    private fun observe() {
        mViewModel.getList.observe(this, androidx.lifecycle.Observer {
            val list: MutableList<String> = arrayListOf()
            for (item in it) {
                list.add(item.description)
                mLisPriorityId.add(item.id)
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            spinner_priority.adapter = adapter
        })
        mViewModel.validation.observe(this, androidx.lifecycle.Observer {
            if (it.validator.not())
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            else {
                val sucessMessage = if (mTaskId == 0) R.string.task_created else R.string.task_updated
                Toast.makeText(this, sucessMessage , Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        mViewModel.task.observe(this, androidx.lifecycle.Observer {
            edit_description.setText(it.description)
            check_complete.isChecked = it.complete
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(it.dueDate)
            button_date.text = mDateFormat.format(date!!)
            spinner_priority.setSelection(getSpinnerIndex(it.priorityId))
        })
    }

    private fun getSpinnerIndex(priorityId: Int): Int {
        var index = 0
        for (i in 0 until mLisPriorityId.size) {
            if (mLisPriorityId[i] == priorityId) {
                index = i
            }
        }
        return index
    }

    private fun listeners() {
        button_save.setOnClickListener(this)
        button_date.setOnClickListener(this)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val chosedDay = mDateFormat.format(calendar.time)
        button_date.text = chosedDay
    }
}
