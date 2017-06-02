package com.askdog.dao.repository;

import com.askdog.model.entity.Template;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Locale;
import java.util.Optional;

@Repository
public interface TemplateRepository extends CrudRepository<Template, Long> {
    Optional<Template> findByNameAndLanguage(String name, Locale language);
}
