package com.example.ms1.note.note.tag.tag;

import com.example.ms1.note.MainDataDto;
import com.example.ms1.note.MainService;
import com.example.ms1.note.ParamHandler;
import com.example.ms1.note.note.Note;
import com.example.ms1.note.note.tag.NoteTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    private final MainService mainService;

    @GetMapping("/{tagId}/notes")
    public String tagNotes(@PathVariable("tagId") Long tagId, Long notebookId, Long noteId, ParamHandler paramHandler, Model model){
        Tag tag = tagService.getTag(tagId);
        List<NoteTag> noteTagList = tag.getNoteTagList();
        List<Note> noteListByTag = new ArrayList<>();

        for(NoteTag noteTag : noteTagList){
            noteListByTag.add(noteTag.getNote());
        }

        MainDataDto mainDataDto = mainService.getMainData(notebookId,noteId,paramHandler.getKeyword(),paramHandler.getSort());

        model.addAttribute("mainDataDto", mainDataDto);
        model.addAttribute("noteListByTag",noteListByTag);
        model.addAttribute("targetTag", tag);

        return "main";
    }
}
