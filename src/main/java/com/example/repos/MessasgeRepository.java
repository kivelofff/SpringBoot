package com.example.repos;

import com.example.domain.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessasgeRepository extends CrudRepository<Message, Long> {
}
