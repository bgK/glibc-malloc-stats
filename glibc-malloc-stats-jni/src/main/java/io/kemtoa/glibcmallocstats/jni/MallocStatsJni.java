package io.kemtoa.glibcmallocstats.jni;

import io.kemtoa.glibcmallocstats.MallocStats;

public class MallocStatsJni {

    private MallocStatsJni() {
    }

    /**
     * This function returns information about the current dynamic memory usage
     */
    public static native MallocStats mallinfo();

}
