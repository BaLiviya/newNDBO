package baliviya.com.github.NDBO.entity.custom;

import lombok.Data;

@Data
public class Specialist {

    private int     id;
    private long    chatId;
    private String  fullName;

    public Specialist setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public Specialist setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

}
