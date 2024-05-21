package com.example.ms1.note.note.tag;

import com.example.ms1.note.ParamHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("notes/{noteId}/tags")
public class NoteTagController {

    private final NoteTagService noteTagService;

    @PostMapping("/create")
    public String create(@PathVariable("noteId") Long noteId, String name, ParamHandler paramHandler){
        NoteTag noteTag = noteTagService.create(noteId,name);
        Long notebookId = noteTag.getNote().getNotebook().getId();
        return paramHandler.getRedirectUrl("/books/%d/notes/%d").formatted(notebookId,noteId);
    }

    @PostMapping("{noteTagId}/delete")
    public String delete(@PathVariable("noteId")Long noteId, @PathVariable("noteTagId") Long noteTagId, ParamHandler paramHandler){
        NoteTag noteTag = noteTagService.getNoteTag(noteTagId);
        Long notebookId = noteTag.getNote().getNotebook().getId();
        noteTagService.delete(noteTagId);

        return paramHandler.getRedirectUrl("/books/%d/notes/%d").formatted(notebookId,noteId);
    }
}
