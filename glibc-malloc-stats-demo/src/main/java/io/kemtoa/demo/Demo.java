package io.kemtoa.demo;

import io.kemtoa.glibcmallocstats.MallocStats;
import io.kemtoa.glibcmallocstats.MallocStatsUtils;

import java.io.IOException;

public class Demo {

    public static void main(String[] args) {
        if (!MallocStatsUtils.areStatsAvailable()) {
            System.err.println("Malloc stats are not available");
            System.exit(1);
        }

        MallocStats mallocStats = MallocStatsUtils.getMallocStats();
        System.out.printf("Arena Total Size:                %10s KiB\n", mallocStats.arenaTotalSize() / 1024);
        System.out.printf("Arena Used Size:                 %10s KiB\n", mallocStats.arenaUsedSize() / 1024);
        System.out.printf("Arena Free Size:                 %10s KiB\n", mallocStats.arenaFreeSize() / 1024);
        System.out.printf("Arena Reclaimable Size:          %10s KiB\n", mallocStats.arenaReclaimableSize() / 1024);
        System.out.printf("Arena Free Blocks Count:         %10s\n",     mallocStats.arenaFreeBlocksCount());
        System.out.printf("Arena Free FastBin Blocks Count: %10s\n",     mallocStats.arenaFreeFastBinBlocksCount());
        System.out.printf("Mapped Blocks Count:             %10s\n",     mallocStats.mmapBlocksCount());
        System.out.printf("Mapped Blocks Total Size:        %10s KiB\n", mallocStats.mmapBlocksTotalSize() / 1024);
        System.out.printf("Total C Heap Size:               %10s KiB\n", (mallocStats.arenaTotalSize() + mallocStats.mmapBlocksTotalSize()) / 1024);
    }
}
