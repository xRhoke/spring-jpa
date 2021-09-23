package com.example.demo.Controller;

import com.example.demo.Exceptions.LessonNotFoundException;
import com.example.demo.Model.Lesson;
import com.example.demo.Repository.LessonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonRepository repository;

    public LessonController(LessonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public Iterable<Lesson> all(){
        return this.repository.findAll();
    }

    @PostMapping("")
    public Lesson create(@RequestBody Lesson lesson){
        return this.repository.save(lesson);
    }

    @GetMapping("/{id}")
    public Lesson getLessonWithId(@PathVariable long id){
        Optional<Lesson> lesson = this.repository.findById(id);
        if (lesson.isPresent()) return lesson.get();
        else throw new NoSuchElementException("Lesson does not exist");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Lesson> delete(@PathVariable long id){
        Optional<Lesson> lesson = this.repository.findById(id);
        if (lesson.isPresent()) {
            this.repository.deleteById(id);
        }
        else throw new NoSuchElementException("Lesson does not exist");

        return new ResponseEntity<>(lesson.get(), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Lesson> updateTitle(@PathVariable long id, @RequestBody Lesson newLesson) throws LessonNotFoundException {
        Lesson lesson = this.repository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException("Lesson does not exist"));

            if(newLesson.getTitle() != null) {
                lesson.setTitle(newLesson.getTitle());
            }
            if (newLesson.getDeliveredOn() != null) {
                lesson.setDeliveredOn(newLesson.getDeliveredOn());
            }

            this.repository.save(lesson);

        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }

}
