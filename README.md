# Glibc Malloc Statistics for the JVM

An utility for retrieving the JVM's malloc statistics when using glibc malloc.

This library calls the `mallinfo` glibc function via JNI to retrieve the glibc
heap statistics. If the `mallinfo2` function is available (glibc >= 2.35.0), it
is called instead. The reported values may wrap around at signed 32-bits integer
boundaries when using `mallinfo`.

The version of this library published on Maven Central only includes the
compiled native JNI wrapper code for x86_64 Linux. If you target any other
platform, you have to provide the appropriate native libraries compiled from
the `glibc-malloc-stats-mallinfo` and `glibc-malloc-stats-mallinfo2` modules.

## Usage

Maven dependency:
```xml
<dependency>
    <groupId>io.kemtoa</groupId>
    <artifactId>glibc-malloc-stats</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Usage example:
```java
if (MallocStatsUtils.areStatsAvailable()) {
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
```

