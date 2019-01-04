#!/bin/bash

GRADLE_CACHE=~/.gradle/caches/modules-2/files-2.1
RUNTIME_IMAGE_PATH=application/build/app-runtime-distro

MODULE_PATH=$JAVA_HOME/jmods:application/build/libs:service-compute/build/libs:service-compute-async/build/libs:service-tasks/build/libs:\
$GRADLE_CACHE/org.slf4j/slf4j-api/1.8.0-beta2/ba136e771a794f77ab41fa879706e5cbd5b20f39:\
$GRADLE_CACHE/org.slf4j/slf4j-simple/1.8.0-beta2/17687308d20646a7ee9f9f8b9f0d376959248639

ADD_MODULES=java.base,jdk.jshell,itx.examples.java.eleven.compute,\
itx.examples.java.eleven.tasks,itx.examples.java.eleven.application,\
org.slf4j,org.slf4j.simple

rm -rf $RUNTIME_IMAGE_PATH

jlink --module-path $MODULE_PATH --add-modules $ADD_MODULES --limit-modules $ADD_MODULES \
--output $RUNTIME_IMAGE_PATH --launcher launch=itx.examples.java.eleven.application/itx.examples.java.eleven.application.Main
