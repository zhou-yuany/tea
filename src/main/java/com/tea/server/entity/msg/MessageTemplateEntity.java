package com.tea.server.entity.msg;

import lombok.Data;

@Data
public class MessageTemplateEntity {

    private MessageValueEntity thing6;

    private MessageValueEntity character_string5;

    private MessageValueEntity thing1;

    private MessageValueEntity date3;

    private MessageValueEntity thing4;

    public void setMessageData(MessageValueEntity thing6, MessageValueEntity character_string5, MessageValueEntity thing1, MessageValueEntity date3, MessageValueEntity thing4) {
        this.thing1 = thing1;
        this.thing6 = thing6;
        this.character_string5 = character_string5;
        this.date3 = date3;
        this.thing4 = thing4;
    }
}
