package com.visione.amndd.data;

import android.provider.BaseColumns;

public final class DeficiencyContract {

        private DeficiencyContract() {}

        /* Inner class that defines the table contents */
        public static class DeficiencyEntry implements BaseColumns {
            public static final String TABLE_NAME = "deficiency";
            public static final String COLUMN_NAME_DATE = "date";
            public static final String COLUMN_NAME_DEFICIENCY = "deficiency";
            public static final String COLUMN_NAME_SOLUTION = "solution";
            public static final String COLUMN_NAME_DIAGNOSED = "diagnosed";
            public static final String COLUMN_NAME_IMAGE = "image";
    }
}
