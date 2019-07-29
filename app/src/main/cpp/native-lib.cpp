#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_htetznaing_unitoolkit_Constants_getAdmobBanner(
        JNIEnv* env,
        jobject /* this */) {
    std::string key = "Y2EtYXBwLXB1Yi0yNzgwOTg0MTU2MzU5Mjc0LzgzMTMwOTI0MTM=";
    return env->NewStringUTF(key.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_htetznaing_unitoolkit_Constants_getAdmobInterstitial(
        JNIEnv* env,
        jobject /* this */) {
    std::string key = "Y2EtYXBwLXB1Yi0yNzgwOTg0MTU2MzU5Mjc0LzgxMjE1MjA3MjA=";
    return env->NewStringUTF(key.c_str());
}