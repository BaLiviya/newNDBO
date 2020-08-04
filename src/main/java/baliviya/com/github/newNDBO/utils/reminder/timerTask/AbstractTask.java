package baliviya.com.github.newNDBO.utils.reminder.timerTask;

import baliviya.com.github.newNDBO.config.Bot;
import baliviya.com.github.newNDBO.dao.DaoFactory;
import baliviya.com.github.newNDBO.dao.impl.ReminderTaskDao;
import baliviya.com.github.newNDBO.dao.impl.UserDao;
import baliviya.com.github.newNDBO.utils.reminder.Reminder;

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
