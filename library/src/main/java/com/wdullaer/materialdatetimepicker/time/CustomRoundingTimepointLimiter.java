package com.wdullaer.materialdatetimepicker.time;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.wdullaer.materialdatetimepicker.time.TimePickerDialog.HOUR_INDEX;
import static com.wdullaer.materialdatetimepicker.time.TimePickerDialog.MINUTE_INDEX;


class CustomRoundingTimepointLimiter extends DefaultTimepointLimiter {

    @Override
    public boolean isOutOfRange(@Nullable Timepoint current, int index, @NonNull Timepoint.TYPE resolution) {
        if (current == null) return false;

        if (index == HOUR_INDEX) {
            if (mMinTime != null && mMinTime.getHour() > current.getHour()) return true;

            if (mMaxTime != null && mMaxTime.getHour()+1 <= current.getHour()) return true;

            if (!exclusiveSelectableTimes.isEmpty()) {
                Timepoint ceil = exclusiveSelectableTimes.ceiling(current);
                Timepoint floor = exclusiveSelectableTimes.floor(current);
                return !(current.equals(ceil, Timepoint.TYPE.HOUR) || current.equals(floor, Timepoint.TYPE.HOUR));
            }

            if (!mDisabledTimes.isEmpty() && resolution == Timepoint.TYPE.HOUR) {
                Timepoint ceil = mDisabledTimes.ceiling(current);
                Timepoint floor = mDisabledTimes.floor(current);
                return current.equals(ceil, Timepoint.TYPE.HOUR) || current.equals(floor, Timepoint.TYPE.HOUR);
            }

            return false;
        }
        else if (index == MINUTE_INDEX) {
            if (mMinTime != null) {
                Timepoint roundedMin = new Timepoint(mMinTime.getHour(), mMinTime.getMinute());
                if (roundedMin.compareTo(current) > 0) return true;
            }

            if (mMaxTime != null) {
                Timepoint roundedMax = new Timepoint(mMaxTime.getHour(), mMaxTime.getMinute(), 59);
                if (roundedMax.compareTo(current) < 0) return true;
            }

            return false;
        }
        else return isOutOfRange(current);
    }
}
