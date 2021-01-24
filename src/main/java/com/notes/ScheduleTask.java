package com.notes;

import com.notes.repository.NotesRepository;
import com.notes.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {

    private NotesService notesService;

    @Autowired
    public ScheduleTask(NotesService notesService) {
        this.notesService = notesService;
    }

    @Scheduled(fixedDelay = 300000)
    public void scheduleTask(){
        System.out.println("Scheduling Task");
        System.out.println("Delete Expired Notes");
        notesService.deleteExpiredNotes();
        System.out.println("Scheduling Task - completed");
    }
}
