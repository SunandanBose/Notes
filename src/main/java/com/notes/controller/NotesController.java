package com.notes.controller;

import com.notes.entity.Note;
import com.notes.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NotesController {

    private NotesService notesService;

    @Autowired
    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping(value = "/note")
    private Note createNotes(@RequestBody Note note){
        return notesService.create(note);
    }

    @GetMapping(value = "/user/{userId}/note")
    private List<Note> getNotesFromUser(@PathVariable(value = "userId") Integer userId){
        return  notesService.getUserNotes(userId);
    }

    @DeleteMapping(value = "/user/{userId}/note/{noteId}")
    private void deleteNote(@PathVariable(value = "userId") Integer userId, @PathVariable(value = "noteId") Integer noteId){
        notesService.deleteNote(userId, noteId);
    }

    @PutMapping(value = "/user/{userId}/restore/note/{noteId}")
    private Note restoreNote(@PathVariable(value = "userId") Integer userId, @PathVariable(value = "noteId") Integer noteId){
        return notesService.restoreNote(userId, noteId);
    }

    @DeleteMapping(value = "/user/{userId}/thrash/note")
    private void deleteTrashedNotes(@PathVariable(value = "userId") Integer userId){
        notesService.deleteThrashNoteofUser(userId);
    }

    @GetMapping(value = "/user/{userId}/deleted/note")
    private List<Note> getDeletedNotes(@PathVariable(value = "userId") Integer userId){
        return notesService.getDeleteNotes(userId);
    }

}
