#include <io_kemtoa_glibcmallocstats_jni_MallocStatsJni.h>

#include <malloc.h>

JNIEXPORT jobject JNICALL Java_io_kemtoa_glibcmallocstats_jni_MallocStatsJni_mallinfo(JNIEnv *jenv, jclass class) {
    struct mallinfo2 info = mallinfo2();

    jclass resultClass = (*jenv)->FindClass(jenv, "io/kemtoa/glibcmallocstats/MallocStats");
    if (!resultClass) {
        return NULL;
    }

    jmethodID resultConstructor = (*jenv)->GetMethodID(jenv, resultClass, "<init>", "(JJJJJJJJ)V");
    if (!resultConstructor) {
        return NULL;
    }

    return (*jenv)->NewObject(jenv, resultClass, resultConstructor, info.arena, info.ordblks, info.smblks, info.hblks, info.hblkhd, info.uordblks, info.fordblks, info.keepcost);
}
