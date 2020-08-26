package baliviya.com.github.newBOSTANDDBO.utils.reminder.timerTask;

import baliviya.com.github.newBOSTANDDBO.config.Bot;
import baliviya.com.github.newBOSTANDDBO.dao.DaoFactory;
import baliviya.com.github.newBOSTANDDBO.dao.impl.ReminderTaskDao;
import baliviya.com.github.newBOSTANDDBO.dao.impl.UserDao;
import baliviya.com.github.newBOSTANDDBO.utils.reminder.Reminder;

import java.util.TimerTask;

public abstract class AbstractTask extends TimerTask {

    protected Bot               bot;
    protected Reminder          reminder;
    protected DaoFactory        daoFactory;
    protected ReminderTaskDao   reminderTaskDao;
    protected UserDao           userDao;

    public AbstractTask(Bot bot, Reminder reminder) {
        this.bot      = bot;
        this.reminder = reminder;
    }
}
