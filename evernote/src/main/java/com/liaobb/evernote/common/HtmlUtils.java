package com.liaobb.evernote.common;

import com.liaobb.evernote.bean.Note;
import com.liaobb.evernote.bean.NoteType;

/**
 * Created by S.King on 2015/10/14.
 */
public class HtmlUtils {
    /***
     * paragma ?:Note   Title
     * paragma ?:create Time
     * paragma ?:Upata  Time
     * paragma ?:Note Type
     * paragma ?:Note content
     */
    private static final String htmlData =
            "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "<meta charset=\"UTF-8\">\n" +
                    "<title></title>\n" +
                    "<style type=\"text/css\">\n" +
                    " h1,h2,h3,h4,h5,h6 {color:dodgerblue;text-align: center}\n" +
                    "p {color: black}\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>%s</h1>\n" +
                    " <h6><b>create time</b>:%s <b>update time</b>:%s</h6>" +
                    "<h6> <b>NoteType</b>\n :%s</h6>" +
                    "<p>\n" +
                    "%s\n" +
                    "</p>\n" +
                    "</body>\n" +
                    "</html>\n";

    public static String getHtmlData(Note note,String type) {
        StringBuffer stringBuffer = new StringBuffer(100);

               stringBuffer.append(String.format(htmlData, note.getNoteTitle(),
                NoteUtils.changeToDefaultTimeFormat(note.getCreateTime()), NoteUtils.changeToDefaultTimeFormat(note.getLastEditorTime()),
               type!=null?type:"默认", //note.getNoteType().getNoteTypeString()
                note.getNoteContent()));

        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        Note note = new Note();
        note.setNoteTitle("Hello World");
        note.setCreateTime(System.currentTimeMillis());
        note.setLastEditorTime(System.currentTimeMillis());
        note.setNoteType(new NoteType());
        note.getNoteType().setNoteTypeString("默认笔记");
        note.setNoteContent("Hello World , I am form feature");

        System.out.println(HtmlUtils.getHtmlData(note,"默认"));
    }
}
