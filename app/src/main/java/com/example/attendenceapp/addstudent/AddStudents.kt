package com.example.attendenceapp.addstudent

import android.os.Bundle
import android.widget.*
import androidx.fragment.app.activityViewModels
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.attendenceapp.R
import com.example.attendenceapp.StudentApplication
import com.example.attendenceapp.StudentViewModel
import com.example.attendenceapp.StudentViewModelFactory
import com.example.attendenceapp.databinding.FragmentAddStudentsBinding
import com.example.attendenceapp.studentdatabase.Students


class AddStudents : Fragment() {

    private val addStudentViewModel: StudentViewModel by activityViewModels{
        StudentViewModelFactory(
            (activity?.application as StudentApplication).database.studentDao()
        )
    }

    private val navigationArgs: StudentDetailArgs by navArgs()
    lateinit var student: Students


    private var _binding: FragmentAddStudentsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddStudentsBinding.inflate(inflater, container, false)

        // Dropdown List
        val items = listOf("Male", "Female", "Others")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        binding.autoCompleteTextView.setAdapter(adapter)

        return binding.root
    }


    // check if edit text are empty or not
    private fun isEntryValid(): Boolean {
        return addStudentViewModel.isEntryValid(
            binding.studentName.text.toString(),
            binding.RollNo.text.toString(),
            binding.Age.text.toString(),
            binding.genderLable.editText?.text.toString()
        )
    }

    private fun bind(student: Students) {
        binding.apply {
            studentName.setText(student.studentName, TextView.BufferType.SPANNABLE)
            RollNo.setText(student.rollNo.toString(), TextView.BufferType.SPANNABLE)
            Age.setText(student.age.toString(), TextView.BufferType.SPANNABLE)
            genderLable.editText?.setText(student.gender, TextView.BufferType.SPANNABLE)

            saveBtn.setOnClickListener { updateItem() }
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            addStudentViewModel.updateStudent(
                this.navigationArgs.studentId,
                this.binding.studentName.text.toString(),
                this.binding.RollNo.text.toString(),
                this.binding.Age.text.toString(),
                this.binding.genderLable.editText?.text.toString(),
            )
            val action = AddStudentsDirections.actionAddStudentsToStudentFragment()
            findNavController().navigate(action)
        }
    }


    private fun addNewStudent() {
        if (isEntryValid()) {
            addStudentViewModel.addNewItem(
                binding.studentName.text.toString(),
                binding.RollNo.text.toString(),
                binding.Age.text.toString(),
                binding.genderLable.editText?.text.toString()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.studentId
        if (id > 0) {
            addStudentViewModel.retrieveStudent(id).observe(this.viewLifecycleOwner) { selectedItem ->
                student = selectedItem
                bind(student)
            }
        }

        else {
            binding.saveBtn.setOnClickListener {
                addNewStudent()
                findNavController().navigate(R.id.action_addStudents_to_studentFragment)
            }
        }
    }


}


