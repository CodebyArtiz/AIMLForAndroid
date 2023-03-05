package com.example.classroomwithfacedetection;

import com.example.classroomwithfacedetection.UI.Models.Classroom;

import java.util.ArrayList;

public interface DataRecive {
    interface Classroom{
        interface List{
            void Classroom(ArrayList<com.example.classroomwithfacedetection.UI.Models.Classroom> classrooms);
        }
        interface One{
            void Classroom(com.example.classroomwithfacedetection.UI.Models.Classroom classroom);
        }
    }

}
