package com.example.ms1.note.note.tag.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    final private TagRepository tagRepository;

    public Tag getTag(Long id){
        return tagRepository.findById(id).orElseThrow();
    }

    public Tag create(String name){
        Tag tag = new Tag();
        tag.setName(name);
        return tagRepository.save(tag);
    }

    public List<Tag> getTagList(){
        return tagRepository.findAll();
    }


}
