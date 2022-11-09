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
    String startPath = Main.plugin.getDataFolder() + "/players/{uuid}/{MM}-{YY}/{DD}.yml";

    public boolean hasTodaysMiningLog(Player player) {
        File miningLog = new File(getTodaysPath(player));
        return miningLog.exists();
    }

    public void genLogs(Player player) {
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        try {
            File todaysLog = new File(getTodaysPath(player));
            boolean created = todaysLog.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public String getTodaysPath(Player player) {
        return getTodaysPath(player.getUniqueId());
    }

    public String getTodaysPath(UUID uuid) {
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DateFormat df = new SimpleDateFormat("yy");
        String day = localDate.getDayOfMonth() + "";
        String month = localDate.getMonthValue() + "";
        String year = df.format(localDate.getYear());
        return startPath.replace("{uuid}", uuid.toString()).replace("{DD}", day).replace("{MM}", month).replace("{YY}", year);
    }
}
