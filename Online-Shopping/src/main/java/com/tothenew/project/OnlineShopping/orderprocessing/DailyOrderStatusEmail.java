package com.tothenew.project.OnlineShopping.orderprocessing;

import io.swagger.annotations.ApiModel;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;

@Document
@ApiModel(description = "This Entity stores information about all daily emails in MongoDb, scheduled and sent to sellers")
public class DailyOrderStatusEmail {

    @Id
    private String id;
    @Indexed(direction = IndexDirection.ASCENDING)
    private String email;
    private String Subject;
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
