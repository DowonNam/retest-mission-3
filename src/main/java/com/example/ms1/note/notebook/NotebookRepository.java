package com.example.ms1.note.notebook;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotebookRepository extends JpaRepository<Notebook, Long>{
    List<Notebook> findByParentIsNull();

    List<Notebook> findByNameContaining(String keyword);
}
