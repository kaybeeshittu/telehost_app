package ng.myflex.telehost.domain.converter

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import ng.myflex.telehost.domain.enumeration.ActivityStatus
import ng.myflex.telehost.domain.enumeration.ActivityType

class TypeConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromActivityStatus(value: ActivityStatus): String = value.name

        @SuppressLint("DefaultLocale")
        @TypeConverter
        @JvmStatic
        fun toActivityStatus(value: String): ActivityStatus {
            return ActivityStatus.valueOf(value.toUpperCase())
        }

        @TypeConverter
        @JvmStatic
        fun fromActivityType(value: ActivityType): String = value.name

        @SuppressLint("DefaultLocale")
        @TypeConverter
        @JvmStatic
        fun toActivityType(value: String): ActivityType {
            return ActivityType.valueOf(value.toUpperCase())
        }

        @TypeConverter
        @JvmStatic
        fun fromListType(value: List<String>?): String? {
            return value?.joinToString(separator = ",") { it }
        }

        @SuppressLint("DefaultLocale")
        @TypeConverter
        @JvmStatic
        fun toListType(value: String?): List<String>? {
            return value?.split(",")?.toList()
        }
    }
}