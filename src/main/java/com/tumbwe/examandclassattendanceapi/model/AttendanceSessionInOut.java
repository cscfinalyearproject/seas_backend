package com.tumbwe.examandclassattendanceapi.model;

import com.tumbwe.examandclassattendanceapi.dto.StartSession;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
public class AttendanceSessionInOut {
    private StartSession session;
    private Set<Student> students;
    private LocalDate timeStamp;

    public AttendanceSessionInOut(StartSession session, Set<Student> students) {
        this.session = session;
        this.students = students;
        this.timeStamp = LocalDate.now();
    }


}
