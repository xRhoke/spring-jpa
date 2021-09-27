package com.example.demo.Repository;

import com.example.demo.Model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, Long> {

    Iterable<Lesson> findByTitle(String title);
    Iterable<Lesson> findByDeliveredOnBetween(Date date1, Date date2);

}
