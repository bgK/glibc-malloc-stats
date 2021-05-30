package io.kemtoa.glibcmallocstats;

import io.kemtoa.glibcmallocstats.jni.MallocStatsJni;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MallocStatsUtils {

    private static final Logger LOGGER =  Logger.getLogger(MallocStatsUtils.class.getName());
    private static boolean initialized;

    private MallocStatsUtils() {
    }

    /**
     * Check whether malloc statistics can be retrieved
     *
     * This may fail is the currently in use malloc implementation is not glibc,
     * if the native JNI library is not available for the current platform,
     * or if it could not ne loaded.
     *
     * Call {@link #getMallocStats()} without checking this first for an exception
     * with a message pinpointing the exact cause.
     */
    public static boolean areStatsAvailable() {
        if (!initialized) {
            try {
                initialize();
            } catch (Exception e) {
                LOGGER.log(Level.FINE, e, () -> "Unable to initialize, malloc statistics are not available");
            }
        }

        return initialized;
    }

    /**
     * Retrieve the current malloc statistics
     */
    public static MallocStats getMallocStats() {
        if (!initialized) {
            try {
                initialize();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        MallocStats mallinfo = MallocStatsJni.mallinfo();
        if (mallinfo == null) {
            throw new IllegalStateException("Unable to retrieve malloc statistics: Native code failure");
        }

        return mallinfo;
    }

    private static void initialize() throws IOException {
        final String os   = normalizeOs(System.getProperty("os.name"));
        final String arch = normalizeArch(System.getProperty("os.arch"));

        try {
            // Prefer the mallinfo2 glibc call as it does not overflow for heaps > 2 GB
            tryLoadNativeLibrary(String.format("glibc-malloc-stats-mallinfo2-%s-%s", os, arch));
        } catch (UnsatisfiedLinkError e) {
            LOGGER.log(Level.FINE, e, () -> "Unable to load the mallinfo2 native JNI library, falling back to mallinfo");
            tryLoadNativeLibrary(String.format("glibc-malloc-stats-mallinfo-%s-%s", os, arch));
        }

        initialized = true;
    }

    private static void tryLoadNativeLibrary(String libraryName) throws IOException {
        // First try to load the native library from java.library.path
        try {
            System.loadLibrary(libraryName);
            return;
        } catch (UnsatisfiedLinkError e) {
            LOGGER.log(Level.FINE, e, () -> String.format("Unable to load '%s' from java.library.path", libraryName));
        }

        // Otherwise, try to extract the appropriate library from the jar to be able to load it
        Path library = null;
        try {
            library = extractToTempFile(libraryName + ".so");
            System.load(library.toString());
        } catch (Exception e) {
            if (library != null) {
                Files.deleteIfExists(library);
            }
            throw e;
        }

        library.toFile().deleteOnExit();
    }

    private static Path extractToTempFile(String resourceName) throws IOException {
        URL resourceUrl = MallocStatsUtils.class.getResource("/" + resourceName);
        if (resourceUrl == null) {
            throw new UnsupportedOperationException("Unable to find native library on the classpath: " + resourceName);
        }

        Path tempFile = Files.createTempFile("java", resourceName);
        try (InputStream inputStream = resourceUrl.openStream()) {
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        return tempFile;
    }

    private static String normalizeOs(String value) {
        value = normalize(value);
        if (value.startsWith("aix")) {
            return "aix";
        }
        if (value.startsWith("hpux")) {
            return "hpux";
        }
        if (value.startsWith("os400")) {
            // Avoid the names such as os4000
            if (value.length() <= 5 || !Character.isDigit(value.charAt(5))) {
                return "os400";
            }
        }
        if (value.startsWith("linux")) {
            return "linux";
        }
        if (value.startsWith("macosx") || value.startsWith("osx")) {
            return "osx";
        }
        if (value.startsWith("freebsd")) {
            return "freebsd";
        }
        if (value.startsWith("openbsd")) {
            return "openbsd";
        }
        if (value.startsWith("netbsd")) {
            return "netbsd";
        }
        if (value.startsWith("solaris") || value.startsWith("sunos")) {
            return "sunos";
        }
        if (value.startsWith("windows")) {
            return "windows";
        }
        if (value.startsWith("zos")) {
            return "zos";
        }

        return "unknown";
    }

    private static String normalizeArch(String value) {
        value = normalize(value);
        if (value.matches("^(x8664|amd64|ia32e|em64t|x64)$")) {
            return "x86_64";
        }
        if (value.matches("^(x8632|x86|i[3-6]86|ia32|x32)$")) {
            return "x86_32";
        }
        if (value.matches("^(ia64w?|itanium64)$")) {
            return "itanium_64";
        }
        if ("ia64n".equals(value)) {
            return "itanium_32";
        }
        if (value.matches("^(sparc|sparc32)$")) {
            return "sparc_32";
        }
        if (value.matches("^(sparcv9|sparc64)$")) {
            return "sparc_64";
        }
        if (value.matches("^(arm|arm32)$")) {
            return "arm_32";
        }
        if ("aarch64".equals(value)) {
            return "aarch_64";
        }
        if (value.matches("^(mips|mips32)$")) {
            return "mips_32";
        }
        if (value.matches("^(mipsel|mips32el)$")) {
            return "mipsel_32";
        }
        if ("mips64".equals(value)) {
            return "mips_64";
        }
        if ("mips64el".equals(value)) {
            return "mipsel_64";
        }
        if (value.matches("^(ppc|ppc32)$")) {
            return "ppc_32";
        }
        if (value.matches("^(ppcle|ppc32le)$")) {
            return "ppcle_32";
        }
        if ("ppc64".equals(value)) {
            return "ppc_64";
        }
        if ("ppc64le".equals(value)) {
            return "ppcle_64";
        }
        if ("s390".equals(value)) {
            return "s390_32";
        }
        if ("s390x".equals(value)) {
            return "s390_64";
        }
        if ("riscv".equals(value)) {
            return "riscv";
        }
        return "unknown";
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
    }
}
