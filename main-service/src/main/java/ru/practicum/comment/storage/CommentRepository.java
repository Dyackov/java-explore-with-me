package ru.practicum.comment.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.enums.StatusComment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, QuerydslPredicateExecutor<Comment> {

    List<Comment> findByEventIdAndStatus(long eventId, StatusComment published, Pageable pageable);

}