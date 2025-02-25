#!/bin/bash

# 设置字符编码
export LANG=en_US.UTF-8

# 设置初始和最大内存
INITIAL_MEMORY="-Xms32m"
MAX_MEMORY="-Xmx128m"

# 获取脚本目录
SCRIPT_DIR="$(dirname "$0")"

# 设置JRE路径
JRE_PATH="$SCRIPT_DIR/jdk/jdk-mac/Contents/Home/bin/java"

# 设置库文件路径
LIBS_PATH="$SCRIPT_DIR/libs"

# 设置主类
MAIN_CLASS="com.t13max.suyaclock.SuyaClockApplication"

# 设置类路径
CLASSPATH="$LIBS_PATH/*"

# 执行 Java 程序
"$JRE_PATH" -DSCRIPT_DIR=$SCRIPT_DIR $INITIAL_MEMORY $MAX_MEMORY $ATTACH_PARAM -cp "$CLASSPATH" "$MAIN_CLASS" -outputEncoding utf-8
