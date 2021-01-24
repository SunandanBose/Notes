package com.notes.repository;

import com.notes.entity.Note;
import com.notes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotesRepository extends JpaRepository<Note, Integer> {

    Note save(Note note);

    List<Note> findByUser(User user);

    void deleteById(Integer id);

    Optional<Note> findById(Integer id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM notesDB.note WHERE deleted_on < NOW() - INTERVAL 1 DAY", nativeQuery = true)
    void deleteExpiredNotes();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM notesDB.note n where n.is_deleted=1 and n.user_id=:userId", nativeQuery = true)
    void deleteThrashedNotesOfUser(Integer userId);

    @Query(value = "Select n.* FROM notesDB.note n where n.is_deleted=1 and n.user_id=:userId", nativeQuery = true)
    List<Note> getDeletedNotes(Integer userId);


}
