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
    private static String yearPath = startPath + "{YYYY}/";
    private static String monthPath = yearPath + "{MM}/";
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
        DateFormat lf = new SimpleDateFormat("yyyy");
        String day = localDate.getDayOfMonth() + "";
        String month = localDate.getMonthValue() + "";
        String year = df.format(date);
        String yearLong = lf.format(date);
        return message.replace("{uuid}", uuid.toString()).replace("{DD}", day).replace("{MM}", month).replace("{YY}", year).replace("{YYYY}", yearLong);
    }

    public static File getPlayerFolder(Player player) {
        return getPlayerFolder(player.getUniqueId());
    }
    public static File getPlayerFolder(UUID uuid) {
        return new File(getStartPath(uuid));
    }
    public static String getStartPath(UUID uuid) {
        return format(startPath, uuid);
    }

    public static File getYearFolder(Player player) {
        return getYearFolder(player.getUniqueId());
    }
    public static File getYearFolder(UUID uuid, String year) {
        String path = yearPath;
        path.replace("{uuid}", uuid.toString()).replace("{YYYY}", year);
        return new File(path);
    }
    public static File getYearFolder(UUID uuid) {
        return new File(getYearPath(uuid));
    }
    public static String getYearPath(UUID uuid) {
        return format(yearPath, uuid);
    }

    public static File getMonthFolder(Player player) {
        return getMonthFolder(player.getUniqueId());
    }
    public static File getMonthFolder(UUID uuid) {
        return new File(getMonthPath(uuid));
    }
    public static File getMonthFolder(UUID uuid, String year, String month) {
        String path = monthPath;
        path.replace("{uuid}", uuid.toString()).replace("{YYYY}", year).replace("{MM}", month);
        return new File(path);
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
    public static String getTodaysPath(Player player) {
        return getTodaysPath(player.getUniqueId());
    }
    public static String getTodaysPath(UUID uuid) {
        return format(mineLogPath, uuid);
    }

    public static File getInfoFile(Player player) {
        return getInfoFile(player.getUniqueId());
    }
    public static File getInfoFile(UUID uuid) {
        return new File(getInfoPath(uuid));
    }
    public static String getInfoPath(UUID uuid) {
        return format(infoPath, uuid);
    }
}
