LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := bufmaker
LOCAL_SRC_FILES := bufmaker.cpp Sneginka.cpp Vector2D.cpp Indexer.cpp SnowSystem.cpp SimplexNoise.cpp


include $(BUILD_SHARED_LIBRARY)
