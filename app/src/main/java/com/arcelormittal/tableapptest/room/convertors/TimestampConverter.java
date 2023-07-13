package com.arcelormittal.tableapptest.room.convertors;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

public class TimestampConverter {

    @TypeConverter
    public static Instant fromTimestamp(Long value) {
        return value == null ? null : Instant.ofEpochMilli(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Instant date) {
        return date == null ? null : date.toEpochMilli();
    }
}
