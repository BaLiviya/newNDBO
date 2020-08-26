package baliviya.com.github.newBOSTANDDBO.utils.reminder;

import baliviya.com.github.newBOSTANDDBO.config.Bot;
import baliviya.com.github.newBOSTANDDBO.utils.DateUtil;
import baliviya.com.github.newBOSTANDDBO.utils.reminder.timerTask.Task;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

@Slf4j
public class Reminder {

    private Bot                     bot;
    private Timer                   timer = new Timer(true);
    private ArrayList<SendMessage>  messages;

    public      Reminder(Bot bot) {
        this.bot = bot;
        setMorningTask(9);
    }

    public void setMorningTask(int hour) {
        Date date               = DateUtil.getHour(hour);
        log.info("Next check db task set to ", date);
        Task checkMorningTask   = new Task(bot, this);
        timer.schedule(checkMorningTask, date);
    }
}
