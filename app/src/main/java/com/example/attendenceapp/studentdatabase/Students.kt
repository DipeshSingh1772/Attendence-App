package com.example.attendenceapp.studentdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_record")
data class Students(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,

    @ColumnInfo(name = "StudentName")
    val studentName:String,

    @ColumnInfo(name = "RollNo")
    val rollNo:Int,

    @ColumnInfo(name = "Age")
    val age:Int,

    @ColumnInfo(name = "Gender")
    val gender:String,
){
    @ColumnInfo(name = "Attendance")
    var attendance:Int = 3
}