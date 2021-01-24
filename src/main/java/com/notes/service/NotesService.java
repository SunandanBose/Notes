package com.notes.service;

import com.notes.entity.Note;
import com.notes.entity.User;
import com.notes.repository.NotesRepository;
import com.notes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotesService {

    private NotesRepository notesRepository;

    private UserRepository userRepository;

    @Autowired
    public NotesService(NotesRepository notesRepository, UserRepository userRepository) {
        this.notesRepository = notesRepository;
        this.userRepository = userRepository;
    }

    public Note create(Note note) {
        Note createdNote = null;
        if(note.getUser() != null &&  note.getUser().getId() != null){
            Optional<User> user = userRepository.findById(note.getUser().getId());
            note.setUser(user.get());
            createdNote = notesRepository.save(note);
        }
        return createdNote;
    }

    public List<Note> getUserNotes(Integer userId) {
        List<Note> userNotes = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            List<Note> notes = notesRepository.findByUser(user.get());
            userNotes.addAll(notes.stream().filter((note) -> note.isDeleted() == false).collect(Collectors.toList()));
        }
        return userNotes;
    }

    public void deleteNote(Integer userId, Integer noteId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Note> note = notesRepository.findById(noteId);
        if(user.isPresent() && note.isPresent()){
            if(note.get().getUser().getId() == user.get().getId()){
                note.get().setDeleted(true);
                note.get().setDeletedOn(new Date(System.currentTimeMillis()));
                notesRepository.save(note.get());
            }
        }
    }

    public Note restoreNote(Integer userId, Integer noteId) {
        Note note = null;
        Optional<User> user = userRepository.findById(userId);
        Optional<Note> existingNote = notesRepository.findById(noteId);
        if(user.isPresent() && existingNote.isPresent()){
            if(existingNote.get().isDeleted()){
                existingNote.get().setDeleted(false);
                existingNote.get().setDeletedOn(null);
                note = notesRepository.save(existingNote.get());
            }
        }
        return note;
    }

    public void deleteExpiredNotes(){
        notesRepository.deleteExpiredNotes();
    }

    public void deleteThrashNoteofUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            notesRepository.deleteThrashedNotesOfUser(userId);
        }
    }

    public List<Note> getDeleteNotes(Integer userId) {
        List<Note> notes = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            notes.addAll(notesRepository.getDeletedNotes(userId));
        }
        return notes;
    }
}
