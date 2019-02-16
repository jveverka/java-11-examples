#include <jni.h>        // JNI header provided by JDK
#include <stdio.h>      // C Standard IO Header
#include "Display.h"   // Generated
 
// Implementation of the native method sayHello()
JNIEXPORT void JNICALL Java_Display_setPixel(JNIEnv *env, jobject thisObj, jint x, jint y, jint r, jint g, jint b) {
   printf("setting pixel\n");
   return;
}

