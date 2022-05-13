package com.example.attendenceapp

import androidx.lifecycle.*
import com.example.attendenceapp.studentdatabase.Students
import com.example.attendenceapp.studentdatabase.StudentsDao
import kotlinx.coroutines.launch

class StudentViewModel(private val studentDao: StudentsDao) : ViewModel() {

    val allStudentsList: LiveData<List<Students>> = studentDao.getAllStudentData()

    fun addNewItem(studentName: String, studentRollNo: String, studentAge: String,studentGender: String) {
        val newStudent = getNewStudentEntry(studentName,studentRollNo,studentAge,studentGender)//added by dps
        insertStudentDataViewModel(newStudent)
    }

    private fun insertStudentDataViewModel(studentData: Students) = viewModelScope.launch{
        studentDao.insert(studentData)
    }

    private fun getNewStudentEntry(
        studentName: String,
        studentRollNo: String,
        studentAge: String,
        studentGender: String,
    ) : Students{
        return Students(
            studentName = studentName,
            rollNo = studentRollNo.toInt(),
            age = studentAge.toInt(),
            gender = studentGender,
        )
    }

    // check if edit text are empty or not
    fun isEntryValid(studentName: String, studentRollNo: String, studentAge: String,studentGender: String): Boolean {
        if (studentName.isBlank() || studentRollNo.isBlank() || studentAge.isBlank() || studentGender.isBlank()) {
            return false
        }
        return true
    }

    fun deleteStudentDataViewModel() = viewModelScope.launch{
        studentDao.deleteAllStudents()
    }

    fun retrieveStudent(id: Int): LiveData<Students> {
        val getStudent: LiveData<Students> = studentDao.getStudent(id)
        return getStudent
    }

    fun deleteSingleStudent(student: Students){
        viewModelScope.launch {
            studentDao.delete(student)
        }
    }

    fun updateStudent(
        studentId: Int,
        studentName: String,
        studentRollNo: String,
        studentAge: String,
        studentGender: String,
    ) {
        val updatedStudent = getUpdatedStudentEntry(studentId, studentName, studentRollNo, studentAge, studentGender)

        updateStudent(updatedStudent)
    }

    private fun updateStudent(student: Students) {
        viewModelScope.launch {
            studentDao.update(student)
        }
    }


    private fun getUpdatedStudentEntry(
        studentId: Int,
        studentName: String,
        studentRollNo: String,
        studentAge: String,
        studentGender: String,
    ): Students {

        return Students(
            id = studentId,
            studentName = studentName,
            rollNo = studentRollNo.toInt(),
            age = studentAge.toInt(),
            gender = studentGender,
        )
    }



}

class StudentViewModelFactory(private val studentDao: StudentsDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(studentDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}