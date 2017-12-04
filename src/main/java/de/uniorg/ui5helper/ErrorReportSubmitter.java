package de.uniorg.ui5helper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.openapi.extensions.PluginDescriptor;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;

public class ErrorReportSubmitter extends com.intellij.openapi.diagnostic.ErrorReportSubmitter {
    @NotNull
    @Override
    public String getReportActionText() {
        return "Report Error";
    }

    @Override
    public boolean submit(@NotNull IdeaLoggingEvent[] events, @Nullable String additionalInfo, @NotNull Component parentComponent, @NotNull Consumer<SubmittedReportInfo> consumer) {

        PluginDescriptor pluginDescriptor = this.getPluginDescriptor();

        JsonArray eventList = this.buildEventList(events);

        JsonObject report = new JsonObject();

        report.addProperty("timestamp", ZonedDateTime.now(ZoneOffset.UTC).toString());
        report.addProperty("plugin_version", pluginDescriptor.getPluginId().getIdString());
        report.addProperty("plugin_id", pluginDescriptor.getPluginId().toString());
        report.addProperty("additional_info", additionalInfo);
        report.add("events", eventList);

        try {
            final URL remote = new URL("https://ui5helper.uniorg.de/report_issue");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        consumer.consume(new SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE));

        return true;
    }

    private JsonArray buildEventList(@NotNull IdeaLoggingEvent[] events) {
        JsonArray allEvents = new JsonArray();
        for (IdeaLoggingEvent event : events) {
            JsonObject err = new JsonObject();
            err.addProperty("message", event.getMessage());

            if (event.getThrowable() != null) {
                Throwable ex = event.getThrowable();
                JsonArray stack = new JsonArray();
                Arrays.stream(ex.getStackTrace())
                        .map(callside -> {
                            JsonObject entry = new JsonObject();
                            entry.addProperty("file_name", callside.getFileName());
                            entry.addProperty("class_name", callside.getClassName());
                            entry.addProperty("method_name", callside.getMethodName());
                            entry.addProperty("line", callside.getLineNumber());
                            entry.addProperty("is_native", callside.isNativeMethod());

                            return entry;
                        })
                        .forEach(stack::add);

                JsonObject exception = new JsonObject();
                exception.addProperty("message", ex.getMessage());
                exception.add("stack_trace", stack);

                err.add("exception", exception);
            }

            allEvents.add(err);
        }

        return allEvents;
    }
}
