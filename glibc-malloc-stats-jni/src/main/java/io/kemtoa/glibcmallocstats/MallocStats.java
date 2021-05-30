package io.kemtoa.glibcmallocstats;

public class MallocStats {
    /** This is the total size of the main memory arena. */
    private final long arenaTotalSize;

    /**
     * This is the number of chunks not in use.
     *
     * The memory allocator internally gets chunks of memory from the operating system,
     * and then carves them up to satisfy individual malloc requests; see The GNU Allocator.
     */
    private final long arenaFreeBlocksCount;

    /** This is the number of fastbin blocks not in use. */
    private final long arenaFreeFastBinBlocksCount;

    /** This is the total number of chunks allocated with mmap. */
    private final long mmapBlocksCount;

    /** This is the total size of memory allocated with mmap, in bytes. */
    private final long mmapBlocksTotalSize;

    /** This is the total size of memory occupied by chunks handed out by malloc. */
    private final long arenaFreeSize;

    /** This is the total size of memory occupied by free (not in use) chunks. */
    private final long arenaUsedSize;

    /**
     * This is the size of the top-most releasable chunk that normally borders the end of the heap
     * (i.e., the high end of the virtual address space’s data segment).
     */
    private final long arenaReclaimableSize;

    public MallocStats(long arenaTotalSize, long arenaFreeBlocksCount, long arenaFreeFastBinBlocksCount, long mmapBlocksCount, long mmapBlocksTotalSize, long arenaFreeSize, long arenaUsedSize, long arenaReclaimableSize) {
        this.arenaTotalSize = arenaTotalSize;
        this.arenaFreeBlocksCount = arenaFreeBlocksCount;
        this.arenaFreeFastBinBlocksCount = arenaFreeFastBinBlocksCount;
        this.mmapBlocksCount = mmapBlocksCount;
        this.mmapBlocksTotalSize = mmapBlocksTotalSize;
        this.arenaFreeSize = arenaFreeSize;
        this.arenaUsedSize = arenaUsedSize;
        this.arenaReclaimableSize = arenaReclaimableSize;
    }

    public long arenaTotalSize() {
        return arenaTotalSize;
    }

    public long arenaFreeBlocksCount() {
        return arenaFreeBlocksCount;
    }

    public long arenaFreeFastBinBlocksCount() {
        return arenaFreeFastBinBlocksCount;
    }

    public long mmapBlocksCount() {
        return mmapBlocksCount;
    }

    public long mmapBlocksTotalSize() {
        return mmapBlocksTotalSize;
    }

    public long arenaFreeSize() {
        return arenaFreeSize;
    }

    public long arenaUsedSize() {
        return arenaUsedSize;
    }

    public long arenaReclaimableSize() {
        return arenaReclaimableSize;
    }
}
