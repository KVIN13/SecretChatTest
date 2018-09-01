package com.example.kvin.testchat;

public class MyMessage {
    private String name;
    private String messageText;


    public String getName() {
        return name;
    }

    public String getMessageText() {
        return messageText;
    }

    public String toString(){
        return(name+": "+messageText);
    }

    MyMessage(String name, String messageText){
        this.name = name;
        this.messageText = messageText;
    }

}
