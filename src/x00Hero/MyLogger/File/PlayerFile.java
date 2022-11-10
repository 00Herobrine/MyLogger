package x00Hero.MyLogger.File;

import org.bukkit.entity.Player;
import x00Hero.MyLogger.Main;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class PlayerFile {
    private static String startPath = Main.plugin.getDataFolder() + "/players/{uuid}/";
    private static String infoPath = startPath + "info.yml";
    private static String monthPath = startPath + "{MM}-{YY}/";
    private static String mineLogPath = monthPath + "{DD}.yml";

    public boolean hasTodaysMiningLog(Player player) {
        File miningLog = new File(getTodaysPath(player));
        return miningLog.exists();
    }

    public boolean genLogs(Player player) {
        boolean created = false;
        try {
            File todaysLog = new File(getTodaysPath(player));
            created = todaysLog.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return created;
    }

    public static String format(String message, UUID uuid) {
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DateFormat df = new SimpleDateFormat("yy");
        String day = localDate.getDayOfMonth() + "";
        String month = localDate.getMonthValue() + "";
        String year = df.format(date);
        return message.replace("{uuid}", uuid.toString()).replace("{DD}", day).replace("{MM}", month).replace("{YY}", year);
    }

    public static String getInfoPath(UUID uuid) {
        return format(infoPath, uuid);
    }

    public static File getMonthFile(Player player) {
        return getMonthFile(player.getUniqueId());
    }

    public static File getMonthFile(UUID uuid) {
        return new File(getMonthPath(uuid));
    }

    public static String getMonthPath(UUID uuid) {
        return format(monthPath, uuid);
    }

    public static File getTodaysFile(Player player) {
        return getTodaysFile(player.getUniqueId());
    }

    public static File getTodaysFile(UUID uuid) {
        return new File(getTodaysPath(uuid));
    }

    public static File getInfoFile(Player player) {
        return getInfoFile(player.getUniqueId());
    }

    public static File getInfoFile(UUID uuid) {
        return new File(getInfoPath(uuid));
    }

    public static String getTodaysPath(Player player) {
        return getTodaysPath(player.getUniqueId());
    }

    public static String getTodaysPath(UUID uuid) {
        return format(mineLogPath, uuid);
    }
}
