package com.softnik.noteappfirebase.model;

public class NoteModel {
    String noteText;
    Long timeStamp;
    String nodeAddress;

    public NoteModel(String noteText, String nodeAddress) {
        this.noteText = noteText;
        this.timeStamp = System.currentTimeMillis();
        this.nodeAddress = nodeAddress;
    }

    public NoteModel() {
    }

    public String getNoteText() {
        return noteText;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public String getNodeAddress() {
        return nodeAddress;
    }
}
