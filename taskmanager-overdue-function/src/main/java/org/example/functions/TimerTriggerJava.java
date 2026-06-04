package org.example.functions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Timer trigger.
 */
public class TimerTriggerJava {
    /**
     * This function will be invoked periodically according to the specified schedule.
     */
    @FunctionName("TimerTriggerJava")
    public void run(
            //For testing schedule = "0 */1 * * * *" - every minute
            //"0 */5 * * * *" - every 5 min
            //When all ok schedule = "0 0 0 * * *" - Once every day at 00:00
            @TimerTrigger(name = "timerInfo", schedule = "0 */1 * * * *")
            String timerInfo,
            final ExecutionContext context
    ) {

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        String sql = """
            UPDATE task
            SET overdue = 1
            WHERE due_date < CONVERT(date, GETDATE())
              AND overdue = 0
              AND status IN ('TODO', 'IN_PROGRESS');
        """;

        try (
                Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            int updated = stmt.executeUpdate();

            context.getLogger().info("Overdue tasks updated: " + updated);

        } catch (Exception e) {
            context.getLogger().severe("Error updating overdue tasks: " + e.getMessage());
        }
    }
}
